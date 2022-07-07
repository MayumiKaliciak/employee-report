package domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;

@Data
@Jacksonized
@Builder
public class EmployeeData {

    String id;
    String name;
    Integer age;
    String email;
    String function;
    Integer salary;
}
