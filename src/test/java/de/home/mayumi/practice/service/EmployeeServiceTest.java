package de.home.mayumi.practice.service;

import de.home.mayumi.practice.common.ResultState;
import de.home.mayumi.practice.domain.CreateResponseMessage;
import de.home.mayumi.practice.domain.EmployeeData;
import de.home.mayumi.practice.persistence.EmployeeDocument;
import de.home.mayumi.practice.persistence.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import javax.xml.transform.OutputKeys;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@RequiredArgsConstructor
class EmployeeServiceTest {

    private EmployeeRepository repository;
    private EmployeeMapper mapper;
    private EmployeeService service;

    @BeforeEach
    void setUp() {
        repository = mock(EmployeeRepository.class);
        mapper = mock(EmployeeMapper.class);

        service = new EmployeeService(repository, mapper);
    }

    @Test
    void createNewEmployee() {
        EmployeeData employee = setUpTestEmployee();
        EmployeeDocument employeeDocument = setUpTestEmployeeDocument();

        when(mapper.mapToDocument(employee)).thenReturn(employeeDocument);

        CreateResponseMessage result = service.createNewEmployee(employee);

        InOrder inOrder = Mockito.inOrder(mapper, repository);
        inOrder.verify(mapper).mapToDocument(employee);
        inOrder.verify(repository).save(employeeDocument);

        assertThat(result).isNotNull();
        assertThat(result.getEmployee().getAge()).isEqualTo(employee.getAge());
    }

    @Test
    void updateEmployeeOK() {
        EmployeeData employee = setUpTestEmployee();
        Optional<EmployeeDocument> employeeOpt = Optional.of(setUpTestEmployeeDocument());

        when(repository.findById("123")).thenReturn(employeeOpt);

        CreateResponseMessage response = service.updateEmployee("123", employee);

        InOrder inOrder = Mockito.inOrder(repository);
        inOrder.verify(repository).findById("123");
        inOrder.verify(repository).save(employeeOpt.get());

        assertThat(response).isNotNull();
        assertThat(response.getResultState()).isEqualTo(ResultState.OK);
    }

    @Test
    void updateEmployeeNotFound() {
        EmployeeData employee = setUpTestEmployee();

        when(repository.findById("123")).thenReturn(Optional.empty());

        CreateResponseMessage response = service.updateEmployee("123", employee);

        InOrder inOrder = Mockito.inOrder(repository);
        inOrder.verify(repository).findById("123");
        inOrder.verifyNoMoreInteractions();

        assertThat(response).isNotNull();
        assertThat(response.getResultState()).isEqualTo(ResultState.NOT_FOUND);
    }

    private EmployeeData setUpTestEmployee() {
        return EmployeeData.builder()
                .id("123")
                .name("Bernd")
                .age(30)
                .email("Bernd@Brot.de")
                .function("Java-Developer")
                .salary(55000)
                .build();
    }

    private EmployeeDocument setUpTestEmployeeDocument() {
        return EmployeeDocument.builder()
                .name("Bernd")
                .age(30)
                .email("Bernd@Brot.de")
                .function("Java-Developer")
                .salary(55000)
                .build();
    }
}