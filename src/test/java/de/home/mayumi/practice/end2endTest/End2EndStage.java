package de.home.mayumi.practice.end2endTest;

import com.jayway.jsonpath.TypeRef;
import com.tngtech.jgiven.annotation.BeforeScenario;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import de.home.mayumi.practice.common.ResponseMessage;
import de.home.mayumi.practice.common.ResultState;
import de.home.mayumi.practice.domain.EmployeeData;
import de.home.mayumi.practice.domain.SearchCriteria;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.webAppContextSetup;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@JGivenStage
@RequiredArgsConstructor
public class End2EndStage {

    private EmployeeData employee;
    private String employeesId;
    private List<EmployeeData> searchedEmployees;

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeScenario
    public void setup() {
        mongoTemplate.getDb().drop();
        webAppContextSetup(context);
    }

    public End2EndStage an_employeeData_object_is_filled_out_in_frontEnd() {

        this.employee = setUpTestEmployee();

        return this;
    }

    public End2EndStage an_employeesData_object_is_created_in_backEnd() {

        MockMvcResponse response = RestAssuredMockMvc.given()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .body(employee)
                .post("/employee");

        response.then().statusCode(200);

        TypeRef<ResponseMessage<EmployeeData>> typeRef = new TypeRef<>() {
        };
        ResponseMessage<EmployeeData> result = response.as(typeRef.getType());

        this.employeesId = result.getData().getId();

        return this;
    }

    public End2EndStage an_employeesData_object_can_be_updated() {

        EmployeeData updatedEmployee = EmployeeData.builder()
                .name("Bernd")
                .age(30)
                .email("Bernd@Brot.de")
                .function("Java-Developer")
                .salary(70000)
                .build();

        MockMvcResponse response = RestAssuredMockMvc.given()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .body(updatedEmployee)
                .patch("/employee/"+ employeesId);

        response.then().statusCode(200);

        TypeRef<ResponseMessage<EmployeeData>> typeRef = new TypeRef<>() {
        };
        ResponseMessage<EmployeeData> result = response.as(typeRef.getType());

        Integer updatedSalary = result.getData().getSalary();

        assertThat(updatedSalary).isEqualTo(70000);

        return this;
    }

    private EmployeeData setUpTestEmployee() {
        return EmployeeData.builder()
                .name("Bernd")
                .age(30)
                .email("Bernd@Brot.de")
                .function("Java-Developer")
                .salary(55000)
                .build();
    }

    public End2EndStage an_employee_is_searched_by_text_$_(String nameSearch) {

        SearchCriteria searchCriteria = SearchCriteria.builder()
                .textSearch(nameSearch)
                .build();

        MockMvcResponse response = RestAssuredMockMvc.given()
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(ACCEPT, APPLICATION_JSON_VALUE)
                .body(searchCriteria)
                .get("/employees");

        response.then().statusCode(200);

        this.searchedEmployees = response.body().jsonPath()
                .getList(".", EmployeeData.class);

        return this;
    }

    public End2EndStage the_employees_data_object_can_be_found() {

        assertThat(searchedEmployees).isNotNull();
        assertThat(searchedEmployees.get(0).getName()).isEqualTo("Bernd");

        return this;
    }
}
