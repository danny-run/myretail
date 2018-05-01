package dl.myretail.controller;


import dl.myretail.model.CustomError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * An exception handler to capture and handle exceptions.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomError> internalError(final HttpServletRequest request, final Exception ex) {

        return new ResponseEntity<CustomError>(new CustomError(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
