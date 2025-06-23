package br.com.agibank.clientes.domain.exception;

public class ClienteException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public ClienteException(String mensagem) {
        super(mensagem);
    }
    
    public ClienteException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
