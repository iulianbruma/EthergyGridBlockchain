package ro.utcluj.blockchain.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ro.utcluj.blockchain.exception.GridContractDeployException;
import ro.utcluj.blockchain.exception.UserContractDeployException;
import ro.utcluj.blockchain.exception.ServiceException;
import ro.utcluj.blockchain.exception.UserNotFoundException;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ GridContractDeployException.class, UserContractDeployException.class })
    public ResponseEntity<Object> handleGridContractDeployException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return new ResponseEntity<Object>(
                ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ UserNotFoundException.class })
    public ResponseEntity<Object> handleUserNotFoundException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return new ResponseEntity<Object>(
                ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ ServiceException.class })
    public ResponseEntity<Object> handleServiceException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return new ResponseEntity<Object>(
                ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return new ResponseEntity<Object>(
                ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
