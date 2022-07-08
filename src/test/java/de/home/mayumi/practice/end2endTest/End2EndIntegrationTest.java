package de.home.mayumi.practice.end2endTest;

import com.tngtech.jgiven.integration.spring.EnableJGiven;
import com.tngtech.jgiven.integration.spring.junit5.SimpleSpringScenarioTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableJGiven
public class End2EndIntegrationTest extends SimpleSpringScenarioTest<End2EndStage> {

    @Test
    public void New_Employee_Is_Created_And_Can_Be_Updated() {
        given().an_employeeData_object_is_filled_out_in_frontEnd();
        when().an_employeesData_object_is_created_in_backEnd();
        then().an_employeesData_object_can_be_updated();
    }

    @Test
    public void Employee_Can_Be_Searched_By_Name(){
        given().an_employeeData_object_is_filled_out_in_frontEnd();
        given().an_employeesData_object_is_created_in_backEnd();
        when().an_employee_is_searched_by_text_$_("Be");
        then().the_employees_data_object_can_be_found();
    }
}
