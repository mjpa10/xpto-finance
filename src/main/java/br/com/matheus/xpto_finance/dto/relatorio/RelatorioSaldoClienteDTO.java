package br.com.matheus.xpto_finance.dto.relatorio;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RelatorioSaldoClienteDTO(
        String cliente,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDateTime clienteDesde,
        String endereco,
        long movimentacoesCredito,
        long movimentacoesDebito,
        long totalMovimentacoes,
        BigDecimal valorPagoMovimentacoes,
        BigDecimal saldoInicial,
        BigDecimal saldoAtual
) {
}