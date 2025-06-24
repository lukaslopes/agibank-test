package br.com.agibank.seguros.domain.exception;

import java.util.UUID;

public class SeguroNaoEncontradoException extends RuntimeException {
    
    public SeguroNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
    
    public SeguroNaoEncontradoException(UUID id) {
        this(String.format("Seguro não encontrado com o ID: %s", id));
    }
}
