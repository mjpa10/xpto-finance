package br.com.matheus.xpto_finance.service;

import br.com.matheus.xpto_finance.dto.RelatorioSaldoClienteDTO;
import br.com.matheus.xpto_finance.entity.Cliente;
import br.com.matheus.xpto_finance.entity.Conta;
import br.com.matheus.xpto_finance.entity.Endereco;
import br.com.matheus.xpto_finance.entity.Movimentacao;
import br.com.matheus.xpto_finance.enums.TipoMovimentacao;
import br.com.matheus.xpto_finance.exception.ResourceNotFoundException;
import br.com.matheus.xpto_finance.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final ClienteRepository clienteRepository;

    // readOnly = true: nenhum dado é alterado aqui, só leitura.
    @Transactional(readOnly = true)
    public RelatorioSaldoClienteDTO gerarRelatorioSaldoCliente(Long clienteId) {

        // .filter(ativo) garante que um cliente desativado (soft delete)
        // não gera relatório, mesmo que o ID ainda exista fisicamente no banco.
        Cliente cliente = clienteRepository.findById(clienteId)
                .filter(c -> Boolean.TRUE.equals(c.getAtivo()))
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        long credito = 0;
        long debito = 0;
        BigDecimal valorPagoMovimentacoes = BigDecimal.ZERO;
        BigDecimal saldoInicial = BigDecimal.ZERO;
        BigDecimal saldoAtual = BigDecimal.ZERO;

        ///para cada Conta existente na lista de contas do cliente, execute o bloco abaixo.
        for (Conta conta : cliente.getContas()) {
            //Ignorando contas inativas
            if (!Boolean.TRUE.equals(conta.getAtivo())) continue;

            saldoAtual = saldoAtual.add(conta.getSaldo());

            // Não existe uma flag "é a movimentação inicial" na entity —
            // então identificamos ela pegando a movimentação com a data
            // mais antiga daquela conta (foi a primeira gravada, na criação
            // da conta). É o valor que vira o "saldo inicial" do relatório.
            Movimentacao primeira = conta.getMovimentacoes().stream()
                    .min(Comparator.comparing(Movimentacao::getDataMovimentacao))
                    .orElse(null);
            if (primeira != null) {
                saldoInicial = saldoInicial.add(primeira.getValor());
            }

            // Conta quantas movimentações são crédito vs débito, e soma o
            // valor de tarifa pago em cada uma (valorTarifa vem da function
            // PL/SQL, calculado no momento em que a movimentação foi criada).
            for (Movimentacao mov : conta.getMovimentacoes()) {
                if (mov.getTipo() == TipoMovimentacao.CREDITO) {
                    credito++;
                } else {
                    debito++;
                }
                // null-safe: a movimentação inicial, por exemplo, pode não
                // ter tarifa calculada, dependendo de como foi criada.
                if (mov.getValorTarifa() != null) {
                    valorPagoMovimentacoes = valorPagoMovimentacoes.add(mov.getValorTarifa());
                }
            }
        }

        // O enunciado pega uma linha de endereço no relatório, então
        // pegamos o primeiro cadastrado (cliente pode ter mais de um).
        String endereco = cliente.getEnderecos().stream()
                .findFirst()
                .map(this::formatarEndereco)
                .orElse("Sem endereço cadastrado");

        return new RelatorioSaldoClienteDTO(
                cliente.getNome(),
                cliente.getDataCadastro(),
                endereco,
                credito,
                debito,
                credito + debito, // total = soma simples de crédito + débito
                valorPagoMovimentacoes,
                saldoInicial,
                saldoAtual
        );
    }

    // Monta a string "Rua, número, complemento, bairro, cidade, UF, CEP"
    // quando ele não existir (evita "Rua X, 100, , Bairro..." com vírgula sobrando).
    private String formatarEndereco(Endereco e) {

        // Adiciona o complemento com vírgula apenas quando estiver preenchido.
        String complemento = (e.getComplemento() == null || e.getComplemento().isBlank())
                ? "" : ", " + e.getComplemento();

        // Substitui cada %s(espaço reservado para texto dentro do String.format()) pelos dados do endereço na mesma ordem.
        return String.format("%s, %s%s, %s, %s, %s, %s",
                e.getLogradouro(), e.getNumero(), complemento,
                e.getBairro(), e.getCidade(), e.getUf(), e.getCep());
    }
}