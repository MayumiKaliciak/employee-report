package de.home.mayumi.practice.persistence;

import de.home.mayumi.practice.domain.SearchCriteria;

import java.util.List;

public interface EmployeeCustomRepository {
    List<EmployeeDocument> findEmployees(SearchCriteria searchCriteria);
}
