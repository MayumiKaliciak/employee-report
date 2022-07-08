package de.home.mayumi.practice.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository {

    private final MongoTemplate mongoTemplate;
}
