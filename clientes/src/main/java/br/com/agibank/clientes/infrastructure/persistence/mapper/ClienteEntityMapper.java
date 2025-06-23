package br.com.agibank.clientes.infrastructure.persistence.mapper;

import br.com.agibank.clientes.domain.model.Cliente;
import br.com.agibank.clientes.domain.model.Endereco;
import br.com.agibank.clientes.infrastructure.persistence.entity.ClienteEntity;
import br.com.agibank.clientes.infrastructure.persistence.entity.EnderecoEmbeddable;
import org.springframework.stereotype.Component;

@Component
public class ClienteEntityMapper {

    public Cliente toDomain(ClienteEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Cliente.builder()
            .id(entity.getId())
            .cpf(entity.getCpf())
            .nome(entity.getNome())
            .dataNascimento(entity.getDataNascimento())
            .telefone(entity.getTelefone())
            .endereco(toEndereco(entity.getEndereco()))
            .build();
    }

    public ClienteEntity toEntity(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        
        return ClienteEntity.builder()
            .id(cliente.getId())
            .cpf(cliente.getCpf())
            .nome(cliente.getNome())
            .dataNascimento(cliente.getDataNascimento())
            .telefone(cliente.getTelefone())
            .endereco(toEnderecoEmbeddable(cliente.getEndereco()))
            .build();
    }

    protected Endereco toEndereco(EnderecoEmbeddable endereco) {
        if (endereco == null) {
            return null;
        }
        
        return Endereco.builder()
            .logradouro(endereco.getLogradouro())
            .numero(endereco.getNumero())
            .complemento(endereco.getComplemento())
            .bairro(endereco.getBairro())
            .cidade(endereco.getCidade())
            .estado(endereco.getEstado())
            .cep(endereco.getCep())
            .build();
    }

    protected EnderecoEmbeddable toEnderecoEmbeddable(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        
        return EnderecoEmbeddable.builder()
            .logradouro(endereco.getLogradouro())
            .numero(endereco.getNumero())
            .complemento(endereco.getComplemento())
            .bairro(endereco.getBairro())
            .cidade(endereco.getCidade())
            .estado(endereco.getEstado())
            .cep(endereco.getCep())
            .build();
    }
}
