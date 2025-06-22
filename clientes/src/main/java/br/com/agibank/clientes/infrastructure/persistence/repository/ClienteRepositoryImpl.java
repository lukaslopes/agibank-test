package br.com.agibank.clientes.infrastructure.persistence.repository;

import br.com.agibank.clientes.domain.model.Cliente;
import br.com.agibank.clientes.domain.repository.ClienteRepository;
import br.com.agibank.clientes.infrastructure.persistence.entity.ClienteEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ClienteRepositoryImpl implements ClienteRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Cliente> listarTodos() {
        return manager.createQuery("from ClienteEntity", ClienteEntity.class)
                .getResultList()
                .stream()
                .map(ClienteEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        ClienteEntity clienteEntity = manager.find(ClienteEntity.class, id);
        return Optional.ofNullable(clienteEntity).map(ClienteEntity::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        String jpql = "from ClienteEntity where cpf = :cpf";
        return manager.createQuery(jpql, ClienteEntity.class)
                .setParameter("cpf", cpf)
                .getResultStream()
                .findFirst()
                .map(ClienteEntity::toDomain);
    }

    @Override
    @Transactional
    public Cliente salvar(Cliente cliente) {
        ClienteEntity clienteEntity = ClienteEntity.fromDomain(cliente);
        if (clienteEntity.getId() == null) {
            manager.persist(clienteEntity);
        } else {
            clienteEntity = manager.merge(clienteEntity);
        }
        return clienteEntity.toDomain();
    }

    @Override
    @Transactional
    public void deletar(Cliente cliente) {
        ClienteEntity clienteEntity = manager.find(ClienteEntity.class, cliente.getId());
        if (clienteEntity != null) {
            manager.remove(clienteEntity);
        }
    }

    @Override
    public boolean existePorCpf(String cpf) {
        String jpql = "select case when count(1) > 0 then true else false end from ClienteEntity where cpf = :cpf";
        return manager.createQuery(jpql, Boolean.class)
                .setParameter("cpf", cpf)
                .getSingleResult();
    }
}
