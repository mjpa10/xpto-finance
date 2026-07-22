package br.com.matheus.xpto_finance.service;

import br.com.matheus.xpto_finance.dto.conta.ContaDTO;
import br.com.matheus.xpto_finance.dto.conta.ContaResponseDTO;
import br.com.matheus.xpto_finance.entity.Cliente;
import br.com.matheus.xpto_finance.entity.Conta;
import br.com.matheus.xpto_finance.entity.Movimentacao;
import br.com.matheus.xpto_finance.enums.TipoMovimentacao;
import br.com.matheus.xpto_finance.exception.OperacaoNaoPermitidaException;
import br.com.matheus.xpto_finance.exception.ResourceNotFoundException;
import br.com.matheus.xpto_finance.mapper.ContaMapper;
import br.com.matheus.xpto_finance.repository.ClienteRepository;
import br.com.matheus.xpto_finance.repository.ContaRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class ContaService {

    private final ContaRepository repository;
    private final ClienteRepository clienteRepository;
    private final ContaMapper mapper;
    //estava com problema de sempre que criava um cliente, a data vinha vazia, apesar de no banco ter savo, esse entity manager serve para resolver isso
    private final EntityManager entityManager;

    public ContaResponseDTO criar(Long clienteId, ContaDTO dto) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .filter(c -> Boolean.TRUE.equals(c.getAtivo()))
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        if (repository.existsByAgenciaAndNumero(dto.getAgencia(), dto.getNumero())) {
            throw new OperacaoNaoPermitidaException("Já existe uma conta com essa agência e número");
        }

        Conta conta = Conta.builder()
                .agencia(dto.getAgencia())
                .numero(dto.getNumero())
                .tipoConta(dto.getTipoConta())
                .saldo(dto.getSaldo())
                .ativo(true)
                .cliente(cliente)
                .build();

        Movimentacao movimentacaoInicial = Movimentacao.builder()
                .tipo(TipoMovimentacao.CREDITO)
                .valor(dto.getSaldo())
                .descricao("Movimentação inicial")
                .build();

        conta.adicionarMovimentacao(movimentacaoInicial);

        Conta contaSalva = repository.save(conta);

        // força o banco a "devolver" os valores gerados por ele mesmo
        // (dataCadastro do cliente, dataAbertura de cada conta)
        entityManager.flush();
        entityManager.refresh(contaSalva);

        return mapper.toResponseDTO(contaSalva);
    }

    public List<ContaResponseDTO> listarPorCliente(Long clienteId) {
        return repository.findAllByClienteId(clienteId).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public ContaResponseDTO buscarPorId(Long clienteId, Long id) {
        Conta conta = repository.findByIdAndClienteId(id, clienteId)
                .filter(c -> Boolean.TRUE.equals(c.getAtivo()))
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada"));
        return mapper.toResponseDTO(conta);
    }

    /// nunca vai funcionar, visto que para cadastrar um novo cliente é preciso ter uma movimentação inicial
    public ContaResponseDTO atualizar(Long clienteId, Long id, ContaDTO dto) {
        Conta conta = repository.findByIdAndClienteId(id, clienteId)
                .filter(c -> Boolean.TRUE.equals(c.getAtivo()))
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada"));

        if (!conta.getMovimentacoes().isEmpty()) {
            throw new OperacaoNaoPermitidaException(
                    "Conta possui movimentações associadas; alteração não permitida, apenas exclusão lógica");
        }

        conta.setAgencia(dto.getAgencia());
        conta.setNumero(dto.getNumero());
        conta.setTipoConta(dto.getTipoConta());

        Conta contaAtualizada = repository.save(conta);
        return mapper.toResponseDTO(contaAtualizada);
    }

    public void excluir(Long clienteId, Long id) {
        Conta conta = repository.findByIdAndClienteId(id, clienteId)
                .filter(c -> Boolean.TRUE.equals(c.getAtivo()))
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada"));

        conta.setAtivo(false);
        repository.save(conta);
    }
}