package br.com.matheus.xpto_finance.controller;

import br.com.matheus.xpto_finance.dto.conta.ContaDTO;
import br.com.matheus.xpto_finance.dto.conta.ContaResponseDTO;
import br.com.matheus.xpto_finance.service.ContaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes/{clienteId}/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService service;

    @GetMapping
    public List<ContaResponseDTO> listar(@PathVariable Long clienteId) {
        return service.listarPorCliente(clienteId);
    }

    @GetMapping("/{id}")
    public ContaResponseDTO buscarPorId(@PathVariable Long clienteId, @PathVariable Long id) {
        return service.buscarPorId(clienteId, id);
    }

    @PostMapping
    public ResponseEntity<ContaResponseDTO> criar(@PathVariable Long clienteId, @Valid @RequestBody ContaDTO dto) {
        ContaResponseDTO response = service.criar(clienteId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping("/{id}")
    public ContaResponseDTO atualizar(@PathVariable Long clienteId, @PathVariable Long id, @Valid @RequestBody ContaDTO dto) {
        return service.atualizar(clienteId, id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long clienteId, @PathVariable Long id) {
        service.excluir(clienteId, id);
        return ResponseEntity.noContent().build();
    }
}