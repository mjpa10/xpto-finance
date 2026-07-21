package br.com.matheus.xpto_finance.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record RelatorioSaldoPeriodoDTO(
        LocalDate periodoInicio,
        LocalDate periodoFim,
        String cliente,
        LocalDateTime clienteDesde,
        String endereco,
        long movimentacoesCredito,
        long movimentacoesDebito,
        long totalMovimentacoes,
        BigDecimal valorPagoMovimentacoes,
        BigDecimal saldoInicial,
        BigDecimal saldoAtual
) {
}