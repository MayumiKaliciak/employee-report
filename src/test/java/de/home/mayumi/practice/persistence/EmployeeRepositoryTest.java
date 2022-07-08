package de.home.mayumi.practice.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void findEmployeeById() {
        EmployeeDocument document = setUpTestEmployeeDocument();
        EmployeeDocument savedDocument = repository.save(document);

        Optional<EmployeeDocument> employeeById = repository.findById("123");

        assertThat(employeeById).isPresent();
        assertThat(employeeById.get().getName()).isEqualTo("Bernd");
    }

    private EmployeeDocument setUpTestEmployeeDocument() {
        return EmployeeDocument.builder()
                .id("123")
                .name("Bernd")
                .age(30)
                .email("Bernd@Brot.de")
                .function("Java-Developer")
                .salary(55000)
                .build();
    }
}