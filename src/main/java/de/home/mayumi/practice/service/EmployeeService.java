package de.home.mayumi.practice.service;

import de.home.mayumi.practice.common.ResultState;
import de.home.mayumi.practice.domain.CreateResponseMessage;
import de.home.mayumi.practice.domain.EmployeeData;
import de.home.mayumi.practice.domain.SearchCriteria;
import de.home.mayumi.practice.persistence.EmployeeDocument;
import de.home.mayumi.practice.persistence.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    public CreateResponseMessage createNewEmployee(EmployeeData employeeData) {

        EmployeeDocument employeeDocument = mapper.mapToDocument(employeeData);
        repository.save(employeeDocument);

        employeeData.setId(employeeDocument.getId());

        return CreateResponseMessage.builder()
                .employee(employeeData)
                .resultState(ResultState.OK)
                .build();
    }

    public CreateResponseMessage updateEmployee(String employeeId, EmployeeData employeeData) {

        Optional<EmployeeDocument> document = repository.findById(employeeId);

        if (document.isEmpty()) {
            return CreateResponseMessage.builder()
                    .resultState(ResultState.NOT_FOUND)
                    .build();
        } else {
            EmployeeDocument employeeDocument = updateDocument(document.get(), employeeData);
            repository.save(employeeDocument);
            EmployeeData updatedEmployee = mapper.mapToDto(employeeDocument);
            return CreateResponseMessage.builder()
                    .employee(updatedEmployee)
                    .resultState(ResultState.OK)
                    .build();
        }
    }

    public List<EmployeeData> getEmployees(SearchCriteria searchCriteria) {

        List<EmployeeDocument> employees;

        if (searchCriteria == null) {
            employees = repository.findAll();
        } else {
            employees = repository.findEmployees(searchCriteria);
        }
        return mapper.mapFromDocToDto(employees);
    }

    private EmployeeDocument updateDocument(EmployeeDocument document, EmployeeData employeeData) {
        document.setName(employeeData.getName());
        document.setAge(employeeData.getAge());
        document.setEmail(employeeData.getEmail());
        document.setFunction(employeeData.getFunction());
        document.setSalary(employeeData.getSalary());
        document.setLastModifiedDate(LocalDateTime.now());

        return document;
    }
}

