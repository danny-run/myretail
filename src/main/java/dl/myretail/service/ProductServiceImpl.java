package dl.myretail.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dl.myretail.exception.ProductServiceException;
import dl.myretail.model.Product;
import dl.myretail.repository.ProductRepository;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * A implementation of product service API.
 */

@Service
public class ProductServiceImpl implements  ProductService{

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private ProductRepository productRepository;

    private RestTemplate restTemplate;

    @Value("${product.info.url}")
    String productInfoUrl;

    @Value("${product.info.excludes}")
    String productInfoExcludes;

    public ProductServiceImpl(final ProductRepository productRepository, final RestTemplate restTemplate) {
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Product> findAllProducts() throws ProductServiceException {
        try {
            return this.productRepository.findAll();
        } catch (Exception ex) {
            throw new ProductServiceException(ex);
        }
    }

    @Override
    public Product findById(final Long id) throws ProductServiceException {
        Product product = null;
        try {
            Optional<Product> dbProduct = this.productRepository.findById(id);

            if (dbProduct.isPresent()) {
                product = dbProduct.get();

                String productName = getProductName(productInfoUrl, id, productInfoExcludes);

                product.setName(productName);


            }


        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new ProductServiceException(ex);

        }

        return product;

    }

    @Override
    public void createProduct(final Product product) throws ProductServiceException {
        try {
            this.productRepository.insert(product);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new ProductServiceException(ex);
        }
    }

    @Override
    public void updateProduct(final Product product) throws ProductServiceException {
        try {
            this.productRepository.save(product);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new ProductServiceException(ex);
        }
    }

    @Override
    public void deleteProductById(final Long id) throws ProductServiceException {
        try {
            this.productRepository.deleteById(id);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new ProductServiceException(ex);
        }
    }

    private String getProductName(String productInfoUrl, Long id, String productInfoExcludes) throws Exception{
        String externalUrl = productInfoUrl + "/" + id + "?" + productInfoExcludes;
        logger.info("externalUrl=" + externalUrl);

        String productInfo = fetchDataFromURL(externalUrl);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree((String)productInfo);
        JsonNode fieldNode = node.path("product").path("item").path("product_description").path("title");

        return fieldNode.textValue();

    }

    private String fetchDataFromURL(String url) {

        //GET request to perform Rest Call
        ResponseEntity<String> response = performRestCall (url);

//        //handle Redirects since HttpUrlConnection by design wont automatically redirect from Htpp to Https
//        HttpHeaders httpHeaders = response.getHeaders();
//        HttpStatus statusCode = response.getStatusCode();
//        logger.info("statusCode=" + statusCode);
//
//        if (statusCode.equals(HttpStatus.MOVED_PERMANENTLY) || statusCode.equals(HttpStatus.FOUND) || statusCode.equals(HttpStatus.SEE_OTHER)) {
//            if (httpHeaders.getLocation() != null) {
//                response = performRestCall (httpHeaders.getLocation().toString());
//            } else {
//                throw new RuntimeException("External URL is not accessible");
//            }
//        }

        return response.getBody();

    }

    private ResponseEntity<String> performRestCall(String url) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
/*            headers.add("Referrer Policy", "no-referrer-when-downgrade");
            headers.add("Upgrade-Insecure-Requests", "1");*/
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            //ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            return response;
        } catch (Exception ex) {
            throw new RuntimeException("External URL " + url + " is not accessible");
        }
    }

    public void setProductInfoUrl(final String productInfoUrl) {
        this.productInfoUrl = productInfoUrl;
    }

    public void setProductInfoExcludes(final String productInfoExcludes) {
        this.productInfoExcludes = productInfoExcludes;
    }
}
