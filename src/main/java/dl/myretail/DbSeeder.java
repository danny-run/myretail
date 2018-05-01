package dl.myretail;

import dl.myretail.model.Price;
import dl.myretail.model.Product;
import dl.myretail.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * A data feeder that initialize data for dev env.
 */
@Profile("dev")
@Component
public class DbSeeder implements CommandLineRunner {
    @Autowired
    ProductRepository productRepository;

    @Override
    public void run(final String... args) throws Exception {
        Price price = new Price(13.49D, "USD");
        Product product = new Product(13860428L, price);

        //drop all products
        productRepository.deleteAll();

        //add products
        productRepository.save(product);

    }
}
