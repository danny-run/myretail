package dl.myretail.controller;

import dl.myretail.model.CustomError;
import dl.myretail.model.Product;
import dl.myretail.repository.ProductRepository;
import dl.myretail.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * A Restful controller for products.
 */
@RestController
@RequestMapping("/products")
public class ProductControlloer {
    private static final Logger logger = LoggerFactory.getLogger(ProductControlloer.class);

    private ProductService productService;

    public ProductControlloer(final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> listAllProducts() throws Exception{
        List<Product> products = productService.findAllProducts();

        if (products.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) throws Exception{
        logger.info("Fetching Product with id {}", id);

        Product product = productService.findById(id);

        if (product == null) {
            logger.error("Product with id {} not found.", id);
            return new ResponseEntity(new CustomError("Product with id " + id + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Product>(product, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) throws Exception{
        Long id = product.getId();

        logger.info("Creating Product with id {}", id);

        Product currentProduct = productService.findById(id);

        if (currentProduct != null) {
            logger.error("Unable to create. Product with id {} found.", id);
            return new ResponseEntity(new CustomError("Unable to create. Product with id " + id + " found."),
                    HttpStatus.CONFLICT);
        }

        productService.createProduct(product);
        return new ResponseEntity<Product>(currentProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) throws Exception{
        logger.info("Updating Product with id {}", id);

        Product currentProduct = productService.findById(id);

        if (currentProduct == null) {
            logger.error("Unable to update. Product with id {} not found.", id);
            return new ResponseEntity(new CustomError("Unable to upate. Product with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        currentProduct.setCurrentPrice(product.getCurrentPrice());
        productService.updateProduct(currentProduct);

        return new ResponseEntity<Product>(currentProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) throws Exception{
        logger.info("Fetching & Deleting Product with id {}", id);

        Product currentProduct = productService.findById(id);
        if (currentProduct == null) {
            logger.error("Unable to delete. Product with id {} not found.", id);
            return new ResponseEntity(new CustomError("Unable to delete. Product with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        productService.deleteProductById(id);

        return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);

    }
}
