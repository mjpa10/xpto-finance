package br.com.matheus.xpto_finance.dto.cliente;

import br.com.matheus.xpto_finance.dto.conta.ContaResponseDTO;
import br.com.matheus.xpto_finance.dto.Endereco.EnderecoResponseDTO;
import br.com.matheus.xpto_finance.enums.TipoPessoa;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ClienteResponseDTO(
        Long id,
        String nome,
        TipoPessoa tipoPessoa,
        String documento,      // cpf ou cnpj, o que valer pra esse cliente
        String telefone,
        LocalDateTime dataCadastro,
        Boolean ativo,
        List<EnderecoResponseDTO> enderecos,
        List<ContaResponseDTO> contas
) {
}