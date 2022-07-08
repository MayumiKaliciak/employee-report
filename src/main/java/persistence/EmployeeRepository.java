package persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeRepository extends MongoRepository<EmployeeDocument, String>, EmployeeCustomRepository {


}
