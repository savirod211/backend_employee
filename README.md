# backend-employee
#
Minimal Spring Boot 4.1.0 "Hello World" service, meant to sit in the backend
container slot of the employee-app pod (frontend NodeJS + backend Java +
monitoring, per the earlier cluster design).

- **Java 21**, **Spring Boot 4.1.0**, **Spring Data JPA**, **PostgreSQL**, **Spring Data MongoDB**
- `GET /` → plain text greeting
- `GET /api/hello` → `{"message": "Hello Employee!", "source": "backend-employee"}` — this is the endpoint the `frontend-employee` container calls
- `GET /api/employees` → all employees (PostgreSQL)
- `GET /api/employees/{id}` → one employee by numeric ID (PostgreSQL)
- `GET /api/employees/number/{employeeNumber}` → one employee by employee number (PostgreSQL)
- `GET /api/activity` → all activity events (MongoDB)
- `POST /api/activity` → create an activity event, body `{"message": "..."}` (MongoDB)
- `GET /actuator/health` → for Kubernetes liveness/readiness probes

Two sample rows (`EMP001`, `EMP002`) are inserted automatically into Postgres
on first startup by `DataSeeder`. One sample event is inserted into MongoDB
by `MongoDataSeeder`, so there's something to query immediately on both
sides.

## Database config

`spring.datasource.*` in `application.properties` reads from env vars
(`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`,
`SPRING_DATASOURCE_PASSWORD`), each with a local-only default so it also
runs standalone. **In Kubernetes, don't rely on the defaults** — set these
same env var names from a Secret (this is exactly what the Secrets Store
CSI driver from the earlier addon work is for) rather than hardcoding
credentials into the Deployment spec or the image.

`spring.jpa.hibernate.ddl-auto=update` auto-creates the `employees` table
from the `Employee` entity — convenient for this learning setup, but a real
production app would use migrations (Flyway/Liquibase) instead.

## MongoDB config

`spring.data.mongodb.uri` follows the same env-var pattern:
`SPRING_DATA_MONGODB_URI`, with a local-only default. In Kubernetes this
points at a `mongodb` Service backed by the external `mongodb-db` VM (see
the `mongodb-vm` files from earlier) via the same selector-less
Service + Endpoints pattern used for Postgres — just pointed at a different
VM and port. MongoDB is deliberately kept as a separate external machine
rather than a container, same blast-radius reasoning as Postgres was
before it moved into the cluster.

## Run locally with a real database

The easiest way to run this end-to-end, including Postgres and MongoDB, is:
```bash
docker compose up --build
```
Then hit:
```bash
curl http://localhost:8080/api/employees
curl http://localhost:8080/api/employees/1
curl http://localhost:8080/api/employees/number/EMP001
curl http://localhost:8080/api/activity
curl -X POST http://localhost:8080/api/activity -H "Content-Type: application/json" -d '{"message":"testing from curl"}'
```

## Run locally without Docker (needs your own Postgres)

```bash
mvn spring-boot:run
```
This uses the defaults in `application.properties`
(`localhost:5432/employeedb`), so point a local Postgres at those, or export
the three `SPRING_DATASOURCE_*` env vars to point elsewhere first.

## Build and run in Docker (app only, no compose)

```bash
docker build -t backend-employee:latest .
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/employeedb \
  -e SPRING_DATASOURCE_USERNAME=employee_app \
  -e SPRING_DATASOURCE_PASSWORD=changeme \
  backend-employee:latest
```

## Next steps for the Kubernetes pod

- Push the image to your registry (Docker Hub, per the earlier interview-lab
  design) and reference it in the Helm chart's `values-<env>.yaml`.
- Point the pod's `livenessProbe` / `readinessProbe` at
  `GET /actuator/health` on port `8080`.
- Wire `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD` to the
  `SecretProviderClass` from the CSI addon instead of plain env vars in the
  Deployment spec.
- CI (GitHub Actions) builds this image on every push, tags it with the
  commit SHA, and pushes it — same flow as the frontend repo.
