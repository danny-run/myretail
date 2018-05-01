package dl.myretail.repository;

import dl.myretail.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * A repository that access product data.
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, Long>{

}
