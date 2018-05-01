package dl.myretail.exception;

/**
 * An exception that wraps up product service activities.
 */
public class ProductServiceException extends Exception{

    public ProductServiceException() {
    }

    public ProductServiceException(final String message) {
        super(message);
    }

    public ProductServiceException(final Throwable cause) {
        super(cause);
    }

    public ProductServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
