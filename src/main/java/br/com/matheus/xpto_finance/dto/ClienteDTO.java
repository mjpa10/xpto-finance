package br.com.matheus.xpto_finance.dto;

import br.com.matheus.xpto_finance.enums.TipoPessoa;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Tipo de pessoa é obrigatório")
    private TipoPessoa tipoPessoa;

    @Size(max = 11, message = "CPF deve possuir no máximo 11 caracteres")
    private String cpf;

    @Size(max = 14, message = "CNPJ deve possuir no máximo 14 caracteres")
    private String cnpj;

    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;

    @NotEmpty(message = "O cliente deve possuir pelo menos um endereço")
    private List<EnderecoDTO> enderecos;

    @NotEmpty(message = "O cliente deve possuir pelo menos uma conta")
    private List<ContaDTO> contas;

    @AssertTrue(message = "CPF é obrigatório para pessoa física")
    @JsonIgnore
    public boolean isCpfPreenchidoSeAPF() {
        return tipoPessoa != TipoPessoa.PF || (cpf != null && !cpf.isBlank());
    }

    @AssertTrue(message = "CNPJ é obrigatório para pessoa jurídica")
    @JsonIgnore
    public boolean isCnpjPreenchidoSeAPJ() {
        return tipoPessoa != TipoPessoa.PJ || (cnpj != null && !cnpj.isBlank());
    }
}