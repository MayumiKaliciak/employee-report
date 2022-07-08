package de.home.mayumi.practice.end2endTest;

import com.jayway.jsonpath.TypeRef;
import com.tngtech.jgiven.annotation.BeforeScenario;
import com.tngtech.jgiven.integration.spring.JGivenStage;
import de.home.mayumi.practice.common.ResponseMessage;
import de.home.mayumi.practice.common.ResultState;
import de.home.mayumi.practice.domain.EmployeeData;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.context.WebApplicationContext;

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
}
