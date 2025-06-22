package br.com.agibank.clientes.domain.usecase;

import br.com.agibank.clientes.domain.model.Cliente;

import java.util.List;
import java.util.UUID;

/**
 * Interface para os casos de uso de Cliente.
 */
public interface ClienteUseCase {
    
    /**
     * Lista todos os clientes cadastrados.
     * 
     * @return Lista de clientes
     */
    List<Cliente> listarClientes();
    
    /**
     * Busca um cliente pelo ID.
     * 
     * @param id ID do cliente
     * @return Cliente encontrado
     * @throws com.agibank.clientes.domain.exception.ClienteNaoEncontradoException se o cliente não for encontrado
     */
    Cliente buscarClientePorId(UUID id);
    
    /**
     * Cadastra um novo cliente.
     * 
     * @param cliente Cliente a ser cadastrado
     * @return Cliente cadastrado com ID gerado
     * @throws com.agibank.clientes.domain.exception.EntidadeEmUsoException se já existir um cliente com o mesmo CPF
     */
    Cliente cadastrarCliente(Cliente cliente);
    
    /**
     * Atualiza um cliente existente.
     * 
     * @param cliente Cliente com os dados atualizados
     * @return Cliente atualizado
     * @throws com.agibank.clientes.domain.exception.ClienteNaoEncontradoException se o cliente não for encontrado
     */
    Cliente atualizarCliente(Cliente cliente);
    
    /**
     * Remove um cliente pelo ID.
     * 
     * @param id ID do cliente a ser removido
     * @throws com.agibank.clientes.domain.exception.ClienteNaoEncontradoException se o cliente não for encontrado
     * @throws com.agibank.clientes.domain.exception.EntidadeEmUsoException se o cliente estiver associado a pedidos
     */
    void removerCliente(UUID id);
}
