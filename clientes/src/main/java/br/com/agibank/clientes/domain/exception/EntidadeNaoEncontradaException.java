package br.com.agibank.clientes.domain.exception;

public abstract class EntidadeNaoEncontradaException extends ClienteException {

    private static final long serialVersionUID = 1L;

    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
    
}
