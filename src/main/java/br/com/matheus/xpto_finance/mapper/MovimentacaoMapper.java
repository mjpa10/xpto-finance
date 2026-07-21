package br.com.matheus.xpto_finance.mapper;

import br.com.matheus.xpto_finance.dto.MovimentacaoResponseDTO;
import br.com.matheus.xpto_finance.entity.Movimentacao;
import org.springframework.stereotype.Component;

@Component
public class MovimentacaoMapper {
    public MovimentacaoResponseDTO toResponseDTO(Movimentacao movimentacao) {
        return MovimentacaoResponseDTO.builder()
                .id(movimentacao.getId())
                .tipo(movimentacao.getTipo())
                .valor(movimentacao.getValor())
                .valorTarifa(movimentacao.getValorTarifa())
                .descricao(movimentacao.getDescricao())
                .dataMovimentacao(movimentacao.getDataMovimentacao())
                .build();
    }
}