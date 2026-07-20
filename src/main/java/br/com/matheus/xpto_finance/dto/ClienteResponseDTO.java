package br.com.matheus.xpto_finance.dto;

import br.com.matheus.xpto_finance.enums.TipoPessoa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ClienteResponseDTO(
        Long id,
        String nome,
        TipoPessoa tipoPessoa,
        String documento,      // cpf ou cnpj, o que valer pra esse cliente
        String telefone,
        BigDecimal saldoInicial,
        LocalDateTime dataCadastro,
        Boolean ativo
) {}