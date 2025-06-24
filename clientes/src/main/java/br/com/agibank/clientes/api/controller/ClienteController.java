package br.com.agibank.clientes.api.controller;

import br.com.agibank.clientes.api.dto.ClienteDTO;
import br.com.agibank.clientes.api.dto.ClienteDTOResponse;
import br.com.agibank.clientes.api.mapper.ClienteMapper;
import br.com.agibank.clientes.domain.usecase.ClienteUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para operações relacionadas a clientes.
 */
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "API para gerenciamento de clientes")
public class ClienteController {

    private final ClienteUseCase clienteUseCase;
    private final ClienteMapper clienteMapper;

    @Operation(summary = "Lista todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ClienteDTOResponse> listar() {
        return clienteMapper.toDTOList(clienteUseCase.listarClientes());
    }

    @Operation(summary = "Busca um cliente por ID", description = "Retorna um cliente específico com base no ID fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    @GetMapping(value = "/{clienteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ClienteDTOResponse buscar(
            @Parameter(description = "ID do cliente a ser buscado", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID clienteId) {
        return clienteMapper.toDTO(clienteUseCase.buscarClientePorId(clienteId));
    }
    
    @Operation(summary = "Busca um cliente por CPF", description = "Retorna um cliente específico com base no CPF fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    @GetMapping(value = "/cpf/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ClienteDTOResponse buscarPorCpf(
            @Parameter(description = "CPF do cliente a ser buscado", required = true, example = "12345678901")
            @PathVariable String cpf) {
        return clienteMapper.toDTO(clienteUseCase.buscarClientePorCpf(cpf));
    }

    @Operation(summary = "Cadastra um novo cliente", description = "Cria um novo cliente com os dados fornecidos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDTOResponse adicionar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do cliente a ser cadastrado", required = true)
            @RequestBody @Valid ClienteDTO clienteDTO) {
        var cliente = clienteMapper.toDomain(clienteDTO);
        var clienteSalvo = clienteUseCase.cadastrarCliente(cliente);
        return clienteMapper.toDTO(clienteSalvo);
    }

    @Operation(summary = "Atualiza um cliente existente", description = "Atualiza os dados de um cliente com base no ID fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    @PutMapping(value = "/{clienteId}", 
               consumes = MediaType.APPLICATION_JSON_VALUE, 
               produces = MediaType.APPLICATION_JSON_VALUE)
    public ClienteDTOResponse atualizar(
            @Parameter(description = "ID do cliente a ser atualizado", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID clienteId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Novos dados do cliente", required = true)
            @RequestBody @Valid ClienteDTO clienteDTO) {
        var cliente = clienteUseCase.buscarClientePorId(clienteId);
        clienteMapper.updateFromDTO(cliente, clienteDTO);
        var clienteAtualizado = clienteUseCase.atualizarCliente(cliente);
        return clienteMapper.toDTO(clienteAtualizado);
    }

    @Operation(summary = "Remove um cliente", description = "Remove um cliente com base no ID fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
    })
    @DeleteMapping("/{clienteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(
            @Parameter(description = "ID do cliente a ser removido", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID clienteId) {
        clienteUseCase.removerCliente(clienteId);
    }
}
