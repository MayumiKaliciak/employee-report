package de.home.mayumi.practice.persistence;

import de.home.mayumi.practice.domain.SearchCriteria;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
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

    @Test
    void findAllEmployees() {

        EmployeeDocument document = setUpTestEmployeeDocument();
        EmployeeDocument saved = repository.save(document);

        List<EmployeeDocument> employees = repository.findAll();
        Assertions.assertThat(employees).hasSize(1);
        Assertions.assertThat(saved.getId()).isEqualTo(employees.get(0).getId());
    }

    @Test
    void findEmployeesByTextSearch() {
        SearchCriteria searchCriteria = SearchCriteria.builder().textSearch("Be").build();

        EmployeeDocument document = setUpTestEmployeeDocument();
        EmployeeDocument saved = repository.save(document);

        List<EmployeeDocument> employees = repository.findEmployees(searchCriteria);

        Assertions.assertThat(employees).hasSize(1);
        Assertions.assertThat(saved.getId()).isEqualTo(employees.get(0).getId());
    }

    @Test
    void findEmployeesByAgeMin() {
        SearchCriteria searchCriteria = SearchCriteria.builder().ageMin(18).build();

        EmployeeDocument document = setUpTestEmployeeDocument();
        EmployeeDocument saved = repository.save(document);

        List<EmployeeDocument> employees = repository.findEmployees(searchCriteria);

        Assertions.assertThat(employees).hasSize(1);
        Assertions.assertThat(saved.getId()).isEqualTo(employees.get(0).getId());
    }

    @Test
    void findEmployeesByAgeMax() {
        SearchCriteria searchCriteria = SearchCriteria.builder().ageMax(40).build();

        EmployeeDocument document = setUpTestEmployeeDocument();
        EmployeeDocument saved = repository.save(document);

        List<EmployeeDocument> employees = repository.findEmployees(searchCriteria);

        Assertions.assertThat(employees).hasSize(1);
        Assertions.assertThat(saved.getId()).isEqualTo(employees.get(0).getId());
    }

    @Test
    void findNoEmployeesByAgeMin() {
        SearchCriteria searchCriteria = SearchCriteria.builder().ageMin(40).build();

        EmployeeDocument document = setUpTestEmployeeDocument();
        EmployeeDocument saved = repository.save(document);

        List<EmployeeDocument> employees = repository.findEmployees(searchCriteria);

        Assertions.assertThat(employees).hasSize(0);
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
