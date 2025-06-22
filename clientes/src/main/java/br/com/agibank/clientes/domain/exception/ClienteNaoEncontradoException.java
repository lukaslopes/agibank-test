package br.com.agibank.clientes.domain.exception;

import java.util.UUID;

public class ClienteNaoEncontradoException extends EntidadeNaoEncontradaException {
    
    private static final long serialVersionUID = 1L;

    public ClienteNaoEncontradoException(UUID id) {
        this(String.format("Não existe um cadastro de cliente com código %s", id));
    }
    
    public ClienteNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
