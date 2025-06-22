package br.com.agibank.clientes.domain.repository;

import br.com.agibank.clientes.domain.model.Cliente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository {
    List<Cliente> listarTodos();
    Optional<Cliente> buscarPorId(UUID id);
    Optional<Cliente> buscarPorCpf(String cpf);
    Cliente salvar(Cliente cliente);
    void deletar(Cliente cliente);
    boolean existePorCpf(String cpf);
}
