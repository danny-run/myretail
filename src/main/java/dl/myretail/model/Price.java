package dl.myretail.model;

public class Price {
    private Double value;
    private String currencyCode;

    public Price() {
    }

    public Price(final Double value, final String currencyCode) {
        this.value = value;
        this.currencyCode = currencyCode;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(final Double value) {
        this.value = value;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(final String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
