package br.com.agibank.clientes.domain.usecase;

import br.com.agibank.clientes.domain.exception.ClienteNaoEncontradoException;
import br.com.agibank.clientes.domain.exception.ClienteException;
import br.com.agibank.clientes.domain.model.Cliente;
import br.com.agibank.clientes.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteUseCase {

    private final ClienteRepository clienteRepository;

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.listarTodos();
    }

    @Override
    public Cliente buscarClientePorId(UUID id) {
        return clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }

    @Override
    @Transactional
    public Cliente cadastrarCliente(Cliente cliente) {
        if (clienteRepository.existePorCpf(cliente.getCpf())) {
            throw new ClienteException(String.format("Já existe um cliente cadastrado com o CPF %s", cliente.getCpf()));
        }
        
        return clienteRepository.salvar(cliente);
    }

    @Override
    @Transactional
    public Cliente atualizarCliente(Cliente cliente) {
        UUID id = cliente.getId();
        Cliente clienteExistente = buscarClientePorId(id);
        
        if (!clienteExistente.getCpf().equals(cliente.getCpf()) && 
            clienteRepository.existePorCpf(cliente.getCpf())) {
            throw new ClienteException(String.format("Já existe um cliente cadastrado com o CPF %s", cliente.getCpf()));
        }
        
        return clienteRepository.salvar(cliente);
    }

    @Override
    @Transactional
    public void removerCliente(UUID id) {
        Cliente cliente = buscarClientePorId(id);
        clienteRepository.deletar(cliente);
    }
    
    @Override
    public Cliente buscarClientePorCpf(String cpf) {
        return clienteRepository.buscarPorCpf(cpf)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado para o CPF: " + cpf));
    }
}
