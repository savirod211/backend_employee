package com.example.hello;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;

    public DataSeeder(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) {
        if (employeeRepository.count() == 0) {
            employeeRepository.save(new Employee("EMP001", "Alex Johnson", 29, "12 River Street"));
            employeeRepository.save(new Employee("EMP002", "Priya Nair", 34, "45 Oak Avenue"));
        }
    }
}
