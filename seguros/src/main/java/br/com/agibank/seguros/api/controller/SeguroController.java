package br.com.agibank.seguros.api.controller;

import br.com.agibank.seguros.api.dto.request.SimulacaoSeguroRequest;
import br.com.agibank.seguros.api.dto.request.ContratarSeguroRequest;
import br.com.agibank.seguros.api.dto.response.ContratacaoSeguroResponse;
import br.com.agibank.seguros.api.dto.response.SimulacaoTodosSegurosResponse;
import br.com.agibank.seguros.api.mapper.SeguroMapper;
import br.com.agibank.seguros.domain.model.Seguro;
import br.com.agibank.seguros.domain.usecase.SeguroUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seguros")
@RequiredArgsConstructor
@Tag(name = "Seguros", description = "API para gerenciamento de seguros")
public class SeguroController {

    private final SeguroUseCase seguroUseCase;
    private final SeguroMapper seguroMapper;

    @Operation(summary = "Simula todos os tipos de seguros", 
               description = "Simula os valores para todos os tipos de seguros disponíveis para um cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Simulação realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping("/simular")
    public ResponseEntity<SimulacaoTodosSegurosResponse> simularSeguros(
            @Valid @RequestBody SimulacaoSeguroRequest request) {
        List<Seguro> seguros = seguroUseCase.simularTodosSeguros(request.getCpf());
        return ResponseEntity.ok(seguroMapper.toSimulacaoTodosResponse(request.getCpf(), seguros));
    }

    @Operation(summary = "Contratar seguro", 
               description = "Realiza a contratação de um seguro para o cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seguro contratado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "409", description = "Já existe um seguro ativo deste tipo para o cliente")
    })
    @PostMapping("/contratar")
    @ResponseStatus(HttpStatus.CREATED)
    public ContratacaoSeguroResponse contratarSeguro(@Valid @RequestBody ContratarSeguroRequest request) {
        Seguro seguro = seguroMapper.toModel(request);
        Seguro seguroSalvo = seguroUseCase.contratarSeguro(seguro);
        return seguroMapper.toContratacaoResponse(seguroSalvo);
    }
}
