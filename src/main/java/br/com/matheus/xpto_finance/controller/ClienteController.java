package br.com.matheus.xpto_finance.controller;

import br.com.matheus.xpto_finance.dto.ClienteDTO;
import br.com.matheus.xpto_finance.dto.ClienteResponseDTO;
import br.com.matheus.xpto_finance.entity.Cliente;
import br.com.matheus.xpto_finance.mapper.ClienteMapper;
import br.com.matheus.xpto_finance.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;
    private final ClienteMapper mapper;

    @GetMapping
    public List<ClienteResponseDTO> listar() {
        return service.listar().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO buscarPorId(@PathVariable Long id) {
        return mapper.toResponseDTO(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> salvar(@Valid @RequestBody ClienteDTO dto) {
        Cliente cliente = service.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponseDTO(cliente));
    }

//so atualiza nome e telefone, nada que altere nos dados de historico e etc
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar( @PathVariable Long id,@Valid @RequestBody ClienteDTO dto) {

        Cliente cliente = service.atualizar(id, dto);
        return ResponseEntity.ok(mapper.toResponseDTO(cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}