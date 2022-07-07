package controller;

import common.ResponseMessage;
import common.ResultState;
import domain.CreateResponseMessage;
import domain.EmployeeData;
import domain.SearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.EmployeeService;

import java.util.List;

import static common.ResultState.NOT_FOUND;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    EmployeeService employeeService;

    @PostMapping("/employee")
    public ResponseEntity<ResponseMessage<EmployeeData>> createEmployee(@RequestBody EmployeeData employeeData) {

        CreateResponseMessage result = employeeService.createNewEmployee(employeeData);

        EmployeeData employee = result.getEmployee();

        ResponseMessage<EmployeeData> message = ResponseMessage.<EmployeeData>builder()
                .message("Created")
                .data(employee)
                .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PatchMapping("/employee/{employeeId}")
    public ResponseEntity<ResultState> updateEmployee(@PathVariable String employeeId, @RequestBody EmployeeData employeeData) {

        ResultState resultState = employeeService.updateEmployee(employeeId, employeeData);

        if (NOT_FOUND.equals(resultState)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/employee")
    public ResponseEntity<List<EmployeeData>> getEmployees(SearchCriteria searchCriteria) {

        List<EmployeeData> employees = employeeService.getEmployees(searchCriteria);

        return ResponseEntity.ok(employees);
    }

}
