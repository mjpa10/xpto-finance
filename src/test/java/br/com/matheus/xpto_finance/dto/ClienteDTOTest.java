package br.com.matheus.xpto_finance.dto;

import br.com.matheus.xpto_finance.enums.TipoConta;
import br.com.matheus.xpto_finance.enums.TipoPessoa;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteDTOTest {

    // Factory cria o mecanismo de validação.
    // Validator executa as anotações como @NotBlank, @NotNull e @AssertTrue.
    private static ValidatorFactory factory;
    private static Validator validator;

    // Executa uma vez antes de todos os testes desta classe.
    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Executa uma vez depois de todos os testes para liberar recursos.
    @AfterAll
    static void tearDown() {
        factory.close();
    }

    @Test
    void PermitirClientePFComCpfPreenchido() {
        ClienteDTO dto = clienteBase();
        dto.setTipoPessoa(TipoPessoa.PF);
        dto.setCpf("12345678900");

        Set<ConstraintViolation<ClienteDTO>> violacoes = validator.validate(dto);

        assertThat(violacoes).isEmpty();
    }

    @Test
    void RejeitarClientePFSemCpf() {
        ClienteDTO dto = clienteBase();
        dto.setTipoPessoa(TipoPessoa.PF);
        dto.setCpf("");

        Set<ConstraintViolation<ClienteDTO>> violacoes = validator.validate(dto);

        assertThat(violacoes)
                .extracting(ConstraintViolation::getMessage)
                .contains("CPF é obrigatório para pessoa física");
    }

    @Test
    void RejeitarClientePJSemCnpj() {
        ClienteDTO dto = clienteBase();
        dto.setTipoPessoa(TipoPessoa.PJ);
        dto.setCnpj(null);

        Set<ConstraintViolation<ClienteDTO>> violacoes = validator.validate(dto);

        assertThat(violacoes)
                .extracting(ConstraintViolation::getMessage)
                .contains("CNPJ é obrigatório para pessoa jurídica");
    }

    private ClienteDTO clienteBase() {
        return ClienteDTO.builder()
                .nome("Cliente Teste")
                .telefone("81999999999")
                .enderecos(List.of(enderecoBase()))
                .contas(List.of(contaBase()))
                .build();
    }

    private EnderecoDTO enderecoBase() {
        return EnderecoDTO.builder()
                .logradouro("Rua Teste")
                .numero("1")
                .bairro("Centro")
                .cidade("Recife")
                .uf("PE")
                .cep("50000000")
                .build();
    }

    private ContaDTO contaBase() {
        return ContaDTO.builder()
                .agencia("0001")
                .numero("123456")
                .tipoConta(TipoConta.CORRENTE)
                .saldo(new BigDecimal("100.00"))
                .build();

    }
}