package br.com.agibank.clientes.api.controller;

import br.com.agibank.clientes.api.dto.ClienteDTO;
import br.com.agibank.clientes.api.mapper.ClienteMapper;
import br.com.agibank.clientes.domain.usecase.ClienteUseCase;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteUseCase clienteUseCase;
    private final ClienteMapper clienteMapper;

    @GetMapping
    public List<ClienteDTO> listar() {
        return clienteMapper.toDTOList(clienteUseCase.listarClientes());
    }

    @GetMapping("/{clienteId}")
    public ClienteDTO buscar(@PathVariable UUID clienteId) {
        return clienteMapper.toDTO(clienteUseCase.buscarClientePorId(clienteId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDTO adicionar(@RequestBody @Valid ClienteDTO clienteDTO) {
        var cliente = clienteMapper.toDomain(clienteDTO);
        var clienteSalvo = clienteUseCase.cadastrarCliente(cliente);
        return clienteMapper.toDTO(clienteSalvo);
    }

    @PutMapping("/{clienteId}")
    public ClienteDTO atualizar(
            @PathVariable UUID clienteId,
            @RequestBody @Valid ClienteDTO clienteDTO) {
        var cliente = clienteUseCase.buscarClientePorId(clienteId);
        clienteMapper.updateFromDTO(cliente, clienteDTO);
        var clienteAtualizado = clienteUseCase.atualizarCliente(cliente);
        return clienteMapper.toDTO(clienteAtualizado);
    }

    @DeleteMapping("/{clienteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable UUID clienteId) {
        clienteUseCase.removerCliente(clienteId);
    }
}
