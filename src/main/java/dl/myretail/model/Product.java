package dl.myretail.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Products")
public class Product {
    @Id
    private Long id;
    private Price currentPrice;

    @Transient
    private String name;

    public Product() {
    }

    public Product(final Long id, final Price currentPrice) {
        this.id = id;
        this.currentPrice = currentPrice;
    }

    public Product(final Long id, final Price currentPrice, final String name) {
        this.id = id;
        this.currentPrice = currentPrice;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Price getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(final Price currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
