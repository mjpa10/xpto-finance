package br.com.matheus.xpto_finance.service;

import br.com.matheus.xpto_finance.dto.MovimentacaoDTO;
import br.com.matheus.xpto_finance.dto.MovimentacaoResponseDTO;
import br.com.matheus.xpto_finance.entity.Conta;
import br.com.matheus.xpto_finance.entity.Movimentacao;
import br.com.matheus.xpto_finance.enums.TipoMovimentacao;
import br.com.matheus.xpto_finance.exception.ResourceNotFoundException;
import br.com.matheus.xpto_finance.mapper.MovimentacaoMapper;
import br.com.matheus.xpto_finance.repository.ContaRepository;
import br.com.matheus.xpto_finance.repository.MovimentacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class MovimentacaoService {

    private final MovimentacaoRepository repository;
    private final ContaRepository contaRepository;
    private final MovimentacaoMapper mapper;
    private final TarifaService tarifaService;

    public MovimentacaoResponseDTO registrar(Long contaId, MovimentacaoDTO dto) {
        Conta conta = contaRepository.findById(contaId)
                .filter(c -> Boolean.TRUE.equals(c.getAtivo()))
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada"));

        LocalDateTime agora = LocalDateTime.now();
        //calcula o novo valor da tarifa
        BigDecimal tarifa = tarifaService.calcularTarifa(conta.getCliente().getId(), agora);

        Movimentacao movimentacao = Movimentacao.builder()
                .tipo(dto.getTipo())
                .valor(dto.getValor())
                .descricao(dto.getDescricao())
                .valorTarifa(tarifa)
                .build();

        conta.adicionarMovimentacao(movimentacao);

        BigDecimal novoSaldo = dto.getTipo() == TipoMovimentacao.CREDITO
                ? conta.getSaldo().add(dto.getValor())
                : conta.getSaldo().subtract(dto.getValor());
        conta.setSaldo(novoSaldo);

        contaRepository.save(conta);

        return mapper.toResponseDTO(movimentacao);
    }

    public List<MovimentacaoResponseDTO> listarPorConta(Long contaId) {
        return repository.findAllByContaIdOrderByDataMovimentacaoDesc(contaId).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }
}