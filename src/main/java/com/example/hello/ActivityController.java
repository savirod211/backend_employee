package com.example.hello;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivityController {

    private final ActivityEventRepository activityEventRepository;

    public ActivityController(ActivityEventRepository activityEventRepository) {
        this.activityEventRepository = activityEventRepository;
    }

    @GetMapping("/api/activity")
    public List<ActivityEvent> getAllEvents() {
        return activityEventRepository.findAll();
    }

    @PostMapping("/api/activity")
    public ActivityEvent createEvent(@RequestBody ActivityMessage body) {
        return activityEventRepository.save(new ActivityEvent(body.message()));
    }

    // Minimal request body: {"message": "something happened"}
    public record ActivityMessage(String message) {
    }
}
