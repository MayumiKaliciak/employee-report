package de.home.mayumi.practice.controller;

import de.home.mayumi.practice.common.ResultState;
import de.home.mayumi.practice.domain.CreateResponseMessage;
import de.home.mayumi.practice.domain.EmployeeData;
import de.home.mayumi.practice.domain.SearchCriteria;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.context.WebApplicationContext;
import de.home.mayumi.practice.service.EmployeeService;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.webAppContextSetup;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest
class EmployeeControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private EmployeeService service;

    @BeforeEach
    void setUp() {
        webAppContextSetup(context);
    }

    @Test
    void createEmployeeOK() {

        EmployeeData employee = setUpTestEmployee();
        CreateResponseMessage responseMessage = CreateResponseMessage.builder()
                .resultState(ResultState.OK)
                .employee(employee)
                .build();

        when(service.createNewEmployee(employee)).thenReturn(responseMessage);

        MockMvcResponse response = RestAssuredMockMvc.given()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .body(employee)
                .post("/employee");

        response.then().statusCode(200);

        verify(service).createNewEmployee(eq(employee));
    }

    @Test
    void createEmployeeValidationFailed() {

        EmployeeData employee = EmployeeData.builder().age(70).build();

        MockMvcResponse response = RestAssuredMockMvc.given()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .body(employee)
                .post("/employee");

        response.then().statusCode(400);

        verifyNoInteractions(service);

    }

    @Test
    void updateEmployeeOK() {

        EmployeeData employee = setUpTestEmployee();

        CreateResponseMessage responseMessage = CreateResponseMessage.builder()
                .resultState(ResultState.OK)
                .employee(employee)
                .build();

        when(service.updateEmployee("123", employee)).thenReturn(responseMessage);

        MockMvcResponse response = RestAssuredMockMvc.given()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .body(employee)
                .patch("/employee/123");

        response.then().statusCode(200);

        verify(service).updateEmployee(eq("123"), eq(employee));
    }

    @Test
    void updateEmployeeNotFound() {

        EmployeeData employee = setUpTestEmployee();

        CreateResponseMessage responseMessage = CreateResponseMessage.builder()
                .resultState(ResultState.NOT_FOUND)
                .build();

        when(service.updateEmployee("456", employee)).thenReturn(responseMessage);

        MockMvcResponse response = RestAssuredMockMvc.given()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .body(employee)
                .patch("/employee/456");

        response.then().statusCode(404);

        verify(service).updateEmployee(eq("456"), eq(employee));
    }

    @Test
    void getEmployees(){

        EmployeeData employeeData = setUpTestEmployee();

        SearchCriteria searchCriteria = SearchCriteria.builder().textSearch("Be").build();
        List<EmployeeData> employees = List.of(employeeData);

        when(service.getEmployees(searchCriteria)).thenReturn(employees);

        MockMvcResponse response = RestAssuredMockMvc.given()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .body(searchCriteria)
                .get("/employees");

        response.then().statusCode(200);
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
}