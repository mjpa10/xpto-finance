package br.com.matheus.xpto_finance.mapper;

import br.com.matheus.xpto_finance.dto.ContaResponseDTO;
import br.com.matheus.xpto_finance.dto.MovimentacaoResponseDTO;
import br.com.matheus.xpto_finance.entity.Conta;
import br.com.matheus.xpto_finance.entity.Movimentacao;
import org.springframework.stereotype.Component;

@Component
public class ContaMapper {
    public ContaResponseDTO toResponseDTO(Conta conta) {
        return ContaResponseDTO.builder()
                .id(conta.getId())
                .agencia(conta.getAgencia())
                .numero(conta.getNumero())
                .tipoConta(conta.getTipoConta())
                .saldo(conta.getSaldo())
                .dataAbertura(conta.getDataAbertura())
                .ativo(conta.getAtivo())
                .build();
    }
}