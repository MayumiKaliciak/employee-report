package controller;

import common.ResultState;
import domain.CreateResponseMessage;
import domain.EmployeeData;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.context.WebApplicationContext;
import service.EmployeeService;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.webAppContextSetup;
import static org.mockito.Mockito.when;
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
    void createEmployee() {

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

    }

    @Test
    void updateEmployee() {
    }

    @Test
    void getEmployees() {
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