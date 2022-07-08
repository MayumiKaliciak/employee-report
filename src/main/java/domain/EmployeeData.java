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

    private String id;
    private String name;
    private Integer age;
    private String email;
    private String function;
    private Integer salary;
}
