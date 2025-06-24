package br.com.agibank.seguros.domain.exception;

public class SeguroException extends RuntimeException {
    
    public SeguroException(String message) {
        super(message);
    }
    
    public SeguroException(String message, Throwable cause) {
        super(message, cause);
    }
}
