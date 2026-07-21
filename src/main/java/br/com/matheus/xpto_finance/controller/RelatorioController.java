package br.com.matheus.xpto_finance.controller;

import br.com.matheus.xpto_finance.dto.RelatorioSaldoClienteDTO;
import br.com.matheus.xpto_finance.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientes/{clienteId}/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService service;

    @GetMapping("/saldo")
    public RelatorioSaldoClienteDTO relatorioSaldo(@PathVariable Long clienteId) {
        return service.gerarRelatorioSaldoCliente(clienteId);
    }
}