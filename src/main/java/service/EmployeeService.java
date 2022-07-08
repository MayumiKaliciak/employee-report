package service;

import common.ResultState;
import domain.CreateResponseMessage;
import domain.EmployeeData;
import domain.SearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import persistence.EmployeeDocument;
import persistence.EmployeeRepository;

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

    public ResultState updateEmployee(String employeeId, EmployeeData employeeData) {

        Optional<EmployeeDocument> document = repository.findById(employeeId);

        if(document.isEmpty()){
            return ResultState.NOT_FOUND;
        } else{
            EmployeeDocument employeeDocument = updateDocument(document.get(), employeeData);
            repository.save(employeeDocument);
            return ResultState.OK;
        }
    }

    public List<EmployeeData> getEmployees(SearchCriteria searchCriteria) {

        List<EmployeeData> employees;
        
        if(searchCriteria == null){
            //TODO implement Search
        }
        
        return null;

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

