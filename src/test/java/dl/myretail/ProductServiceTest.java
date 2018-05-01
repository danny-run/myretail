package dl.myretail;


import dl.myretail.exception.ProductServiceException;
import dl.myretail.model.Price;
import dl.myretail.model.Product;
import dl.myretail.repository.ProductRepository;
import dl.myretail.service.ProductServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    RestTemplate restTemplate;

    @Mock
    ResponseEntity<String> responseEntity;

    @InjectMocks
    ProductServiceImpl productService;


    private static String productInfoUrl = "http://redsky.target.com/v2/pdp/tcin";
    private static String productInfoExcludes = "excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";

    private static final Long ID = 13860428L;
    private static final Long ID2 = 13860429L;
    private static final Long ID3 = 13860429L;
    private static final Double VALUE = 15D;
    private static final String CURRENCYCODE = "USD";
    private static final Price PRICE = new Price(VALUE, CURRENCYCODE);
    private static final String NAME = "The Big Lebowski (Blu-ray)";
    private static final Product PRODUCT = new Product(ID, PRICE, NAME);
    private static final Optional<Product> OPTIONAL_WITH_VALUE = Optional.of(PRODUCT);
    private static final Optional<Product> OPTIONAL_EMPTY = Optional.empty();

    private static final String RESPONSE = "{\"product\":{\"deep_red_labels\":{\"total_count\":2,\"labels\":[{\"id\":\"gqwm8i\",\"name\":\"TAC\",\"type\":\"relationship type\",\"priority\":0,\"count\":1},{\"id\":\"twbl94\",\"name\":\"Movies\",\"type\":\"merchandise type\",\"priority\":0,\"count\":1}]},\"available_to_promise_network\":{\"product_id\":\"13860428\",\"id_type\":\"TCIN\",\"available_to_promise_quantity\":14.0,\"street_date\":\"2011-11-15T06:00:00.000Z\",\"availability\":\"AVAILABLE\",\"online_available_to_promise_quantity\":14.0,\"stores_available_to_promise_quantity\":0.0,\"availability_status\":\"IN_STOCK\",\"multichannel_options\":[\"HOLD\",\"SHIPGUEST\"],\"is_infinite_inventory\":false,\"loyalty_availability_status\":\"IN_STOCK\",\"loyalty_purchase_start_date_time\":\"1970-01-01T00:00:00.000Z\",\"is_loyalty_purchase_enabled\":false},\"item\":{\"tcin\":\"13860428\",\"bundle_components\":{\"is_assortment\":false,\"is_kit_master\":false,\"is_standard_item\":true,\"is_component\":false},\"dpci\":\"058-34-0436\",\"upc\":\"025192110306\",\"product_description\":{\"title\":\"The Big Lebowski (Blu-ray)\",\"bullet_description\":[\"<B>Movie Studio:</B> Universal Studios\",\"<B>Software Format:</B> Blu-ray\",\"<B>Movie Genre - MMBV:</B> Comedy\"],\"general_description\":\"Blu-ray BIG LEBOWSKI, THE Movies\"},\"parent_items\":\"46767107\",\"buy_url\":\"https://www.target.com/p/the-big-lebowski-blu-ray/-/A-13860428\",\"variation\":{},\"enrichment\":{\"images\":[{\"base_url\":\"https://target.scene7.com/is/image/Target/\",\"primary\":\"13860428\"}],\"sales_classification_nodes\":[{\"node_id\":\"5xswx\"},{\"node_id\":\"yzuww\"},{\"node_id\":\"ieagq\"},{\"node_id\":\"55ayu\"},{\"node_id\":\"5t0ak\"}]},\"return_method\":\"This item can be returned to any Target store or Target.com.\",\"handling\":{},\"recall_compliance\":{\"is_product_recalled\":false},\"tax_category\":{\"tax_class\":\"G\",\"tax_code_id\":99999,\"tax_code\":\"99999\"},\"display_option\":{\"is_size_chart\":false,\"is_warranty\":false},\"fulfillment\":{\"is_po_box_prohibited\":true,\"po_box_prohibited_message\":\"We regret that this item cannot be shipped to PO Boxes.\"},\"package_dimensions\":{\"weight\":\"0.18\",\"weight_unit_of_measure\":\"POUND\",\"width\":\"5.33\",\"depth\":\"6.65\",\"height\":\"0.46\",\"dimension_unit_of_measure\":\"INCH\"},\"environmental_segmentation\":{\"is_lead_disclosure\":false},\"manufacturer\":{},\"product_vendors\":[{\"id\":\"4667999\",\"manufacturer_style\":\"61119422\",\"vendor_name\":\"UNIVERSAL HOME VIDEO\"},{\"id\":\"1258738\",\"manufacturer_style\":\"025192110306\",\"vendor_name\":\"BAKER AND TAYLOR\"},{\"id\":\"1979650\",\"manufacturer_style\":\"61119422\",\"vendor_name\":\"Universal Home Ent PFS\"}],\"product_classification\":{\"product_type\":\"542\",\"product_type_name\":\"ELECTRONICS\",\"item_type_name\":\"Movies\",\"item_type\":{\"category_type\":\"Item Type: MMBV\",\"type\":300752,\"name\":\"Movies\"}},\"product_brand\":{\"brand\":\"Universal Home Video\"},\"item_state\":\"READY_FOR_LAUNCH\",\"specifications\":[],\"attributes\":{\"gift_wrapable\":\"N\",\"has_prop65\":\"N\",\"is_hazmat\":\"N\",\"max_order_qty\":10,\"street_date\":\"2011-11-15\",\"media_format\":\"Blu-ray\",\"merch_class\":\"MOVIES\",\"merch_subclass\":34,\"return_method\":\"This item can be returned to any Target store or Target.com.\"},\"country_of_origin\":\"US\",\"relationship_type_code\":\"Title Authority Child\",\"subscription_eligible\":false,\"ribbons\":[],\"tags\":[],\"estore_item_status_code\":\"A\",\"eligibility_rules\":{\"hold\":{\"is_active\":true},\"ship_to_guest\":{\"is_active\":true}},\"return_policies\":{\"user\":\"Regular Guest\",\"policyDays\":\"30\",\"guestMessage\":\"This item must be returned within 30 days of the ship date. See return policy for details.\"},\"gifting_enabled\":false}}}";

    @Before
    public void  setup() {

        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testFindById_Success() throws Exception{

        String externalUrl = productInfoUrl + "/" + ID + "?" + productInfoExcludes;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        when(productRepository.findById(ID)).thenReturn(OPTIONAL_WITH_VALUE);
        when(restTemplate.exchange(externalUrl, HttpMethod.GET, entity, String.class)).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(RESPONSE);

        productService.setProductInfoUrl(productInfoUrl);
        productService.setProductInfoExcludes(productInfoExcludes);
        Product product = productService.findById(ID);

        Assert.assertNotNull(product);
        Assert.assertEquals(product.getId(), ID);
        Assert.assertEquals(product.getCurrentPrice().getValue(), VALUE);
    }

    @Test
    public void testFindById_Failure() throws Exception{

        String externalUrl = productInfoUrl + "/" + ID2 + "?" + productInfoExcludes;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        when(productRepository.findById(ID2)).thenReturn(OPTIONAL_EMPTY);

        productService.setProductInfoUrl(productInfoUrl);
        productService.setProductInfoExcludes(productInfoExcludes);
        Product product = productService.findById(ID2);

        Assert.assertNull(product);

    }

    @Test (expected = ProductServiceException.class)
    public void testFindById_Exception() throws Exception{

        String externalUrl = productInfoUrl + "/" + ID3 + "?" + productInfoExcludes;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        when(productRepository.findById(ID3)).thenReturn(OPTIONAL_WITH_VALUE);
        when(restTemplate.exchange(externalUrl, HttpMethod.GET, entity, String.class)).thenThrow(new RuntimeException());

        productService.setProductInfoUrl(productInfoUrl);
        productService.setProductInfoExcludes(productInfoExcludes);
        Product product = productService.findById(ID3);

    }


}

