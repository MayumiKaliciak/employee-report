package de.home.mayumi.practice.service;

import de.home.mayumi.practice.domain.EmployeeData;
import de.home.mayumi.practice.persistence.EmployeeDocument;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {

    public List<EmployeeData> mapFromDocToDto(List<EmployeeDocument> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    EmployeeData mapToDto(EmployeeDocument document) {
        return EmployeeData.builder()
                .id(document.getId())
                .name(document.getName())
                .age(document.getAge())
                .email(document.getEmail())
                .function(document.getFunction())
                .salary(document.getSalary())
                .build();
    }

    EmployeeDocument mapToDocument(EmployeeData dto) {
        return EmployeeDocument.builder()
                .name(dto.getName())
                .age(dto.getAge())
                .email(dto.getEmail())
                .function(dto.getFunction())
                .salary(dto.getSalary())
                .build();
    }
}
