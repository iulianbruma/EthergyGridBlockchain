package ro.utcluj.blockchain.exception;

public class UserContractDeployException extends RuntimeException {
    public UserContractDeployException(String message) {
        super(message);
    }

    public UserContractDeployException(String message, Throwable cause) {
        super(message, cause);
    }
}
