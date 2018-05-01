package dl.myretail.model;

/**
 * A Java Bean that used to send an error message to client.
 */
public class CustomError {

    private String errorMessage;

    public CustomError(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
