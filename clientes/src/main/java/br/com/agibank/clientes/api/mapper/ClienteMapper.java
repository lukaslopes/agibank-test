package br.com.agibank.clientes.api.mapper;

import br.com.agibank.clientes.api.dto.ClienteDTO;
import br.com.agibank.clientes.domain.model.Cliente;
import br.com.agibank.clientes.domain.model.Endereco;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper responsável por converter entre entidades de domínio e DTOs.
 */
@Component
public class ClienteMapper {

    public Cliente toDomain(ClienteDTO dto) {
        if (dto == null) {
            return null;
        }

        return Cliente.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .dataNascimento(dto.getDataNascimento())
                .telefone(dto.getTelefone())
                .endereco(toEnderecoDomain(dto.getEndereco()))
                .build();
    }
    
    public ClienteDTO toDTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        
        return ClienteDTO.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .cpf(cliente.getCpf())
                .dataNascimento(cliente.getDataNascimento())
                .telefone(cliente.getTelefone())
                .endereco(toEnderecoDTO(cliente.getEndereco()))
                .build();
    }
    
    public List<ClienteDTO> toDTOList(List<Cliente> clientes) {
        if (clientes == null) {
            return null;
        }
        return clientes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public void updateFromDTO(Cliente cliente, ClienteDTO dto) {
        if (dto == null) {
            return;
        }
        
        if (dto.getNome() != null) {
            cliente.setNome(dto.getNome());
        }
        if (dto.getCpf() != null) {
            cliente.setCpf(dto.getCpf());
        }
        if (dto.getDataNascimento() != null) {
            cliente.setDataNascimento(dto.getDataNascimento());
        }
        if (dto.getTelefone() != null) {
            cliente.setTelefone(dto.getTelefone());
        }
        
        // Atualiza endereço se fornecido
        if (dto.getEndereco() != null) {
            if (cliente.getEndereco() == null) {
                cliente.setEndereco(Endereco.builder().build());
            }
            updateEnderecoFromDTO(cliente.getEndereco(), dto.getEndereco());
        }
    }
    
    private Endereco toEnderecoDomain(ClienteDTO.EnderecoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return Endereco.builder()
                .cep(dto.getCep())
                .logradouro(dto.getLogradouro())
                .numero(dto.getNumero())
                .complemento(dto.getComplemento())
                .bairro(dto.getBairro())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .build();
    }
    
    private ClienteDTO.EnderecoDTO toEnderecoDTO(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        
        return ClienteDTO.EnderecoDTO.builder()
                .cep(endereco.getCep())
                .logradouro(endereco.getLogradouro())
                .numero(endereco.getNumero())
                .complemento(endereco.getComplemento())
                .bairro(endereco.getBairro())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .build();
    }
    
    private void updateEnderecoFromDTO(Endereco endereco, ClienteDTO.EnderecoDTO dto) {
        if (dto.getCep() != null) {
            endereco.setCep(dto.getCep());
        }
        if (dto.getLogradouro() != null) {
            endereco.setLogradouro(dto.getLogradouro());
        }
        if (dto.getNumero() != null) {
            endereco.setNumero(dto.getNumero());
        }
        if (dto.getComplemento() != null) {
            endereco.setComplemento(dto.getComplemento());
        }
        if (dto.getBairro() != null) {
            endereco.setBairro(dto.getBairro());
        }
        if (dto.getCidade() != null) {
            endereco.setCidade(dto.getCidade());
        }
        if (dto.getEstado() != null) {
            endereco.setEstado(dto.getEstado());
        }
    }
}
