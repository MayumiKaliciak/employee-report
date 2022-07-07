package persistence;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Jacksonized
@Document
@Builder
public class EmployeeDocument {

    @Id
    String id;

    @Indexed
    @NotEmpty
    String name;

    @NotNull
    Integer age;

    @Indexed(unique = true)
    @Email
    String email;

    String function;
    Integer salary;

    @CreatedDate
    LocalDateTime timeOfRegistration;

    @LastModifiedDate
    LocalDateTime lastModifiedDate;
}
