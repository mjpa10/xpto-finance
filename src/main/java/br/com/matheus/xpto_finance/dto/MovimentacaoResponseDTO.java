package br.com.matheus.xpto_finance.dto;

import br.com.matheus.xpto_finance.enums.TipoMovimentacao;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record MovimentacaoResponseDTO(
        Long id,
        TipoMovimentacao tipo,
        BigDecimal valor,
        BigDecimal valorTarifa,
        String descricao,
        LocalDateTime dataMovimentacao
) {
}