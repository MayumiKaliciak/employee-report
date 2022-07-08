package de.home.mayumi.practice.domain;

import de.home.mayumi.practice.common.ResultState;
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
