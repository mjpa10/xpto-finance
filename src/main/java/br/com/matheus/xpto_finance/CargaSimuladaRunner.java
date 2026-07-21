package br.com.matheus.xpto_finance;

import br.com.matheus.xpto_finance.dto.*;
import br.com.matheus.xpto_finance.enums.TipoConta;
import br.com.matheus.xpto_finance.enums.TipoMovimentacao;
import br.com.matheus.xpto_finance.enums.TipoPessoa;
import br.com.matheus.xpto_finance.repository.ClienteRepository;
import br.com.matheus.xpto_finance.service.ClienteService;
import br.com.matheus.xpto_finance.service.MovimentacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

// Faz o Spring detectar e criar automaticamente esta classe.
@Component
// O Lombok cria o construtor para injetar os atributos final.
@RequiredArgsConstructor
public class CargaSimuladaRunner implements CommandLineRunner {

    private final ClienteService clienteService;
    private final MovimentacaoService movimentacaoService;
    private final ClienteRepository clienteRepository;

    // CPF fixo usado para identificar se a carga simulada já foi executada.
    private static final String CPF_SIMULADO = "98765432100";

    // O CommandLineRunner executa este metodo automaticamente
    // depois que a aplicação Spring termina de iniciar.
    @Override
    public void run(String... args) throws Exception {

        // Se já rodou antes (esse CPF já existe), não roda de novo.
        boolean jaExiste = clienteRepository.findAll().stream()
                .anyMatch(c -> CPF_SIMULADO.equals(c.getCpf()));

        if (jaExiste) {
            return;
        }

        ClienteDTO clienteY = ClienteDTO.builder()
                .nome("Cliente Y Simulado")
                .tipoPessoa(TipoPessoa.PF)
                .cpf(CPF_SIMULADO)
                .telefone("81988887777")
                .enderecos(List.of(EnderecoDTO.builder()
                        .logradouro("Av. Boa Viagem")
                        .numero("500")
                        .bairro("Boa Viagem")
                        .cidade("Recife")
                        .uf("PE")
                        .cep("51020000")
                        .build()))
                .contas(List.of(contaDTO("0001", "555000", TipoConta.CORRENTE, "2000.00")))
                .build();

        ClienteResponseDTO clienteSalvo = clienteService.salvar(clienteY);
        Long contaId = clienteSalvo.contas().getFirst().id();

        registrarMovimentacao(contaId, TipoMovimentacao.DEBITO, "50.00", "Compra simulada 1");
        registrarMovimentacao(contaId, TipoMovimentacao.DEBITO, "30.00", "Compra simulada 2");
        registrarMovimentacao(contaId, TipoMovimentacao.CREDITO, "200.00", "Depósito simulado");
    }

    private ContaDTO contaDTO(String agencia, String numero, TipoConta tipo, String saldo) {
        return ContaDTO.builder()
                .agencia(agencia)
                .numero(numero)
                .tipoConta(tipo)
                .saldo(new BigDecimal(saldo))
                .build();
    }

    private void registrarMovimentacao(Long contaId, TipoMovimentacao tipo, String valor, String descricao) {
        MovimentacaoDTO dto = MovimentacaoDTO.builder()
                .tipo(tipo)
                .valor(new BigDecimal(valor))
                .descricao(descricao)
                .build();
        movimentacaoService.registrar(contaId, dto);
    }
}