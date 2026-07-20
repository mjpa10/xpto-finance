package br.com.matheus.xpto_finance.dto;

import br.com.matheus.xpto_finance.enums.TipoPessoa;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {

    private String nome;

    private TipoPessoa tipoPessoa;

    private String cpf;

    private String cnpj;

    private String telefone;

    private BigDecimal saldoInicial;
}