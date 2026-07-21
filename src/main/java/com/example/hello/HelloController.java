package com.example.hello;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Hello Employee! (try GET /api/hello for the JSON API)";
    }

    // This is the endpoint the frontend container calls.
    @GetMapping("/api/hello")
    public Map<String, String> apiHello() {
        return Map.of(
                "message", "Hello Employee!",
                "source", "backend-employee"
        );
    }
}
