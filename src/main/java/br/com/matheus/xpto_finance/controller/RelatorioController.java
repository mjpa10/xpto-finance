package br.com.matheus.xpto_finance.controller;

import br.com.matheus.xpto_finance.dto.relatorio.RelatorioSaldoClienteDTO;
import br.com.matheus.xpto_finance.dto.relatorio.RelatorioSaldoPeriodoDTO;
import br.com.matheus.xpto_finance.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/clientes/{clienteId}/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService service;

    @GetMapping("/saldo")
    public RelatorioSaldoClienteDTO relatorioSaldo(@PathVariable Long clienteId) {
        return service.gerarRelatorioSaldoCliente(clienteId);
    }

    //data enviada nesse formato yyyy-MM-dd, no service vai ser formatada em padrao br
    @GetMapping("/saldo-periodo")
    public RelatorioSaldoPeriodoDTO relatorioSaldoPeriodo(
            @PathVariable Long clienteId,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate inicio,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fim) {
        return service.gerarRelatorioSaldoPeriodo(clienteId, inicio, fim);
    }
}