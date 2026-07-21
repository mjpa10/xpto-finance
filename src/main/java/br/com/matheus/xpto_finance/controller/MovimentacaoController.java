package br.com.matheus.xpto_finance.controller;

import br.com.matheus.xpto_finance.dto.MovimentacaoDTO;
import br.com.matheus.xpto_finance.dto.MovimentacaoResponseDTO;
import br.com.matheus.xpto_finance.service.MovimentacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Rotas em /contas/{contaId}/movimentacoes. Sem PUT/DELETE de propósito: movimentação é registro histórico financeiro,
// não deve ser alterada nem apagada — só criada e listada.
@RestController
@RequestMapping("/contas/{contaId}/movimentacoes")
@RequiredArgsConstructor
public class MovimentacaoController {

    private final MovimentacaoService service;

    @PostMapping
    public ResponseEntity<MovimentacaoResponseDTO> registrar(@PathVariable Long contaId, @Valid @RequestBody MovimentacaoDTO dto) {
        MovimentacaoResponseDTO response = service.registrar(contaId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<MovimentacaoResponseDTO> listar(@PathVariable Long contaId) {
        return service.listarPorConta(contaId);
    }
}