package com.example.hello;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActivityEventRepository extends MongoRepository<ActivityEvent, String> {
}
