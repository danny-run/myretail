package dl.myretail.service;

import dl.myretail.exception.ProductServiceException;
import dl.myretail.model.Product;

import java.util.List;

/**
 * A Service API for Product.
 */
public interface ProductService {

    List<Product> findAllProducts() throws ProductServiceException;

    Product findById(Long id) throws ProductServiceException;

    void createProduct(Product product) throws ProductServiceException;

    void updateProduct(Product product) throws ProductServiceException;

    void deleteProductById(Long id) throws ProductServiceException;

}
