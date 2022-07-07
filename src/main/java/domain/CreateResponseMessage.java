package domain;

import common.ResultState;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Data
public class CreateResponseMessage {
    private EmployeeData employee;
    private ResultState resultState;
}
