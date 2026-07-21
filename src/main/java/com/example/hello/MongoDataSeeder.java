package com.example.hello;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MongoDataSeeder implements CommandLineRunner {

    private final ActivityEventRepository activityEventRepository;

    public MongoDataSeeder(ActivityEventRepository activityEventRepository) {
        this.activityEventRepository = activityEventRepository;
    }

    @Override
    public void run(String... args) {
        if (activityEventRepository.count() == 0) {
            activityEventRepository.save(new ActivityEvent("backend-employee started up"));
        }
    }
}
