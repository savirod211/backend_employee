package com.example.hello;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Spring derives the query from the method name - no SQL written by hand.
    Optional<Employee> findByEmployeeNumber(String employeeNumber);
}
