package de.home.mayumi.practice.domain;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Data
public class SearchCriteria {

    private String id;

    private String textSearch;
    ;
    private Integer ageMin;
    private Integer ageMax;
}
