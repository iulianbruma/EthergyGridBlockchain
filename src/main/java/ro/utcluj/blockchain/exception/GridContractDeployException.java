package ro.utcluj.blockchain.exception;

public class GridContractDeployException extends RuntimeException {
    public GridContractDeployException(String message) {
        super(message);
    }

    public GridContractDeployException(String message, Throwable cause) {
        super(message, cause);
    }
}
