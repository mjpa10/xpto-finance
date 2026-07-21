package br.com.matheus.xpto_finance.controller;

import br.com.matheus.xpto_finance.dto.Endereco.EnderecoDTO;
import br.com.matheus.xpto_finance.dto.Endereco.EnderecoResponseDTO;
import br.com.matheus.xpto_finance.service.EnderecoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes/{clienteId}/enderecos")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService service;

    @GetMapping
    public List<EnderecoResponseDTO> listar(@PathVariable Long clienteId) {
        return service.listarPorCliente(clienteId);
    }

    @GetMapping("/{id}")
    public EnderecoResponseDTO buscarPorId(@PathVariable Long clienteId, @PathVariable Long id) {
        return service.buscarPorId(clienteId, id);
    }

    @PostMapping
    public ResponseEntity<EnderecoResponseDTO> criar(@PathVariable Long clienteId, @Valid @RequestBody EnderecoDTO dto) {
        EnderecoResponseDTO responseDTO = service.criar(clienteId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public EnderecoResponseDTO atualizar(@PathVariable Long clienteId, @PathVariable Long id, @Valid @RequestBody EnderecoDTO dto) {
        return service.atualizar(clienteId, id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long clienteId, @PathVariable Long id) {
        service.excluir(clienteId, id);
        return ResponseEntity.noContent().build();
    }
}