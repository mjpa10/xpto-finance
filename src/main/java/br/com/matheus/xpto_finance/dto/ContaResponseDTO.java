package br.com.matheus.xpto_finance.dto;

import br.com.matheus.xpto_finance.enums.TipoConta;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ContaResponseDTO(
        Long id,
        String agencia,
        String numero,
        TipoConta tipoConta,
        BigDecimal saldo,
        LocalDateTime dataAbertura,
        Boolean ativo
) {
}