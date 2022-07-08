package de.home.mayumi.practice.persistence;

import de.home.mayumi.practice.domain.SearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<EmployeeDocument> findEmployees(SearchCriteria searchCriteria) {

        Criteria criteria = createSearchCriteria(searchCriteria);
        return mongoTemplate.find(Query.query(criteria), EmployeeDocument.class);
    }

    private Criteria createSearchCriteria(SearchCriteria searchCriteria) {
        Criteria criteria = new Criteria();
        applyNameAndFunctionSearch(searchCriteria, criteria);
        applyAgeMinFilter(searchCriteria, criteria);
        applyAgeMaxFilter(searchCriteria, criteria);
        return criteria;
    }

    private void applyNameAndFunctionSearch(SearchCriteria searchCriteria, Criteria criteria) {

        if (searchCriteria.getTextSearch() != null) {
            String textSearch = searchCriteria.getTextSearch();
            Criteria nameCriteria = Criteria.where("name").regex(createRegEx(textSearch), "i");
            Criteria functionCriteria = Criteria.where("function").regex(createRegEx(textSearch), "i");
            criteria.orOperator(nameCriteria, functionCriteria);
        }
    }

    private void applyAgeMinFilter(SearchCriteria searchCriteria, Criteria criteria) {

        if(searchCriteria.getAgeMin() != null){
            Integer ageSearch = searchCriteria.getAgeMin();
            Criteria ageMinCriteria = Criteria.where("age").gt(ageSearch);
            criteria.andOperator(ageMinCriteria);
        }
    }

    private void applyAgeMaxFilter(SearchCriteria searchCriteria, Criteria criteria) {

        if(searchCriteria.getAgeMax() != null) {
            Integer ageSearch = searchCriteria.getAgeMax();
            Criteria ageMaxCriteria = Criteria.where("age").lt(ageSearch);
            criteria.andOperator(ageMaxCriteria);
        }
    }

    private String createRegEx(String searchString) {
        return MongoRegexCreator.INSTANCE.toRegularExpression(searchString, MongoRegexCreator.MatchMode.CONTAINING);
    }

}
