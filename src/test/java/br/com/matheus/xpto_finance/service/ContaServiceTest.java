package br.com.matheus.xpto_finance.service;

import br.com.matheus.xpto_finance.dto.ContaDTO;
import br.com.matheus.xpto_finance.entity.Conta;
import br.com.matheus.xpto_finance.entity.Movimentacao;
import br.com.matheus.xpto_finance.enums.TipoConta;
import br.com.matheus.xpto_finance.enums.TipoMovimentacao;
import br.com.matheus.xpto_finance.exception.OperacaoNaoPermitidaException;
import br.com.matheus.xpto_finance.exception.ResourceNotFoundException;
import br.com.matheus.xpto_finance.mapper.ContaMapper;
import br.com.matheus.xpto_finance.repository.ClienteRepository;
import br.com.matheus.xpto_finance.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//ativa o Mockito
@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    //aqui esta mockando os repositorios e etc, igual ao .net, para simular o ambiente de testes
    //Esse teste verifica as regras do ContaService sem acessar o Oracle. Ele usa o serviço real
    // , mas troca os repositórios e o mapper por objetos falsos criados pelo Mockito.
    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ContaMapper mapper;

    @InjectMocks
    private ContaService service;

    private Conta conta;

    //que executa um bloco de código automaticamente antes de cada teste individual
    //O @BeforeEach executa esse metodo antes de cada teste. Assim, cada teste começa com uma conta nova e independente.
    @BeforeEach
    void setUp() {
        conta = Conta.builder()
                .id(1L)
                .agencia("0001")
                .numero("123456")
                .tipoConta(TipoConta.CORRENTE)
                .saldo(new BigDecimal("1000.00"))
                .ativo(true)
                .build();
    }

    @Test
    void naoDevePermitirAtualizarContaComMovimentacoes() {
        Movimentacao mov = Movimentacao.builder()
                .tipo(TipoMovimentacao.CREDITO)
                .valor(new BigDecimal("1000.00"))
                .build();
        // adiciona essa movimentação na conta:
        conta.getMovimentacoes().add(mov);

        //Quando o service buscar a conta de ID 1, pertencente ao cliente 10, retorne a conta criada no teste.
        //optional vem da propria classe contarepository,evita retornar diretamente null e permite tratar o resultado
        when(contaRepository.findByIdAndClienteId(1L, 10L)).thenReturn(Optional.of(conta));

        //passa o valor do update na conta para testar
        ContaDTO dto = new ContaDTO();
        dto.setAgencia("0002");
        dto.setNumero("999999");
        dto.setTipoConta(TipoConta.CORRENTE);

        // Espera que a regra de negócio lance a exceção.
        assertThatThrownBy(() -> service.atualizar(10L, 1L, dto))
                .isInstanceOf(OperacaoNaoPermitidaException.class)

                .hasMessageContaining("Conta possui movimentações associadas; alteração não permitida, apenas exclusão lógica");
        // Como ocorreu erro, nenhuma conta deve ser salva.
        verify(contaRepository, never()).save(any());
    }

    @Test
    void devePermitirAtualizarContaSemMovimentacoes() {
        // Cenário hipotético: hoje toda conta nasce com movimentação inicial,
        // então esse caminho não ocorre no fluxo real da aplicação — mas o
        // teste garante que a regra de bloqueio funciona corretamente caso
        // uma conta sem movimentações venha a existir no futuro.
        when(contaRepository.findByIdAndClienteId(1L, 10L)).thenReturn(Optional.of(conta));

        // Faz o save falso devolver o mesmo objeto recebido.
        when(contaRepository.save(any(Conta.class))).thenAnswer(inv -> inv.getArgument(0));

        ContaDTO dto = new ContaDTO();
        dto.setAgencia("0002");
        dto.setNumero("999999");
        dto.setTipoConta(TipoConta.CORRENTE);

        service.atualizar(10L, 1L, dto);

        assertThat(conta.getAgencia()).isEqualTo("0002");
        // Confirma que a conta foi enviada para o save.
        verify(contaRepository).save(conta);
    }

    @Test
    void excluirDeveDesativarContaSemApagarDoBanco() {
        when(contaRepository.findByIdAndClienteId(1L, 10L)).thenReturn(Optional.of(conta));

        service.excluir(10L, 1L);

        assertThat(conta.getAtivo()).isFalse();

        // Exclusão lógica: salva como inativa, mas não chama delete.
        verify(contaRepository).save(conta);
        verify(contaRepository, never()).delete(any());
    }

    @Test
    void deveLancarExcecaoQuandoContaNaoExisteOuInativa() {
        when(contaRepository.findByIdAndClienteId(99L, 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(10L, 99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}