package br.com.agibank.clientes.infrastructure.persistence.repository;

import br.com.agibank.clientes.domain.model.Cliente;
import br.com.agibank.clientes.domain.repository.ClienteRepository;
import br.com.agibank.clientes.infrastructure.persistence.entity.ClienteEntity;
import br.com.agibank.clientes.infrastructure.persistence.mapper.ClienteEntityMapper;
import br.com.agibank.clientes.infrastructure.persistence.repository.jpa.ClienteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClienteRepositoryImpl implements ClienteRepository {

    private final ClienteJpaRepository jpaRepository;
    private final ClienteEntityMapper clienteEntityMapper;

    @Override
    public List<Cliente> listarTodos() {
        return jpaRepository.findAll().stream()
                .map(clienteEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        return jpaRepository.findById(id)
                .map(clienteEntityMapper::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return jpaRepository.findByCpf(cpf)
                .map(clienteEntityMapper::toDomain);
    }

    @Override
    @Transactional
    public Cliente salvar(Cliente cliente) {
        ClienteEntity clienteEntity = clienteEntityMapper.toEntity(cliente);
        ClienteEntity savedEntity = jpaRepository.save(clienteEntity);
        return clienteEntityMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public void deletar(Cliente cliente) {
        jpaRepository.deleteById(cliente.getId());
    }

    @Override
    public boolean existePorCpf(String cpf) {
        return jpaRepository.existsByCpf(cpf);
    }
}
