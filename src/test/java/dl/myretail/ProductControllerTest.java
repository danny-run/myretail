package dl.myretail;

import dl.myretail.controller.ProductControlloer;
import dl.myretail.controller.RestResponseEntityExceptionHandler;
import dl.myretail.exception.ProductServiceException;
import dl.myretail.model.Price;
import dl.myretail.model.Product;
import dl.myretail.repository.ProductRepository;
import dl.myretail.service.ProductService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyRetailApplication.class)
@ActiveProfiles("test")
public class ProductControllerTest {

    @Mock
    ProductService productService;

    @InjectMocks
    ProductControlloer productControlloer;

    private MockMvc mvc;


    private static final Long ID = 13860428L;
    private static final Double VALUE = 15D;
    private static final String CURRENCYCODE = "USD";
    private static final Price PRICE = new Price(VALUE, CURRENCYCODE);
    private static final String NAME = "The Big Lebowski (Blu-ray)";
    private static final Product PRODUCT = new Product(ID, PRICE, NAME);

    private static String productInfoUrl = "http://redsky.target.com/v2/pdp/tcin";
    private static String productInfoExcludes = "excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";

    @Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(productControlloer).setControllerAdvice(new RestResponseEntityExceptionHandler()).build();
    }


    @Test
    public void testFindById_Success() throws Exception{

        String externalUrl = productInfoUrl + "/" + ID + "?" + productInfoExcludes;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        when(productService.findById(ID)).thenReturn(PRODUCT);

        mvc.perform(get("/products/" + ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(13860428)));

    }

    @Test
    public void testFindById_Failure() throws Exception{

        String externalUrl = productInfoUrl + "/" + ID + "?" + productInfoExcludes;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        when(productService.findById(ID)).thenThrow(new ProductServiceException(new RuntimeException("External URL " + externalUrl + " is not accessible")));

        mvc.perform(get("/products/" + ID)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

    }


}

