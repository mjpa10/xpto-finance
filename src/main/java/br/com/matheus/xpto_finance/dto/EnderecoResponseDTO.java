package br.com.matheus.xpto_finance.dto;

import lombok.Builder;

@Builder
public record EnderecoResponseDTO(
        Long id,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String uf,
        String cep
) {
}