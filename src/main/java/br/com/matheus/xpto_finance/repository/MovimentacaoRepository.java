package br.com.matheus.xpto_finance.repository;

import br.com.matheus.xpto_finance.entity.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoRepository
        extends JpaRepository<Movimentacao, Long> {

    // Busca todas as movimentações de uma conta, ordenando da mais recente para a mais antiga.
    List<Movimentacao> findAllByContaIdOrderByDataMovimentacaoDesc(
            Long contaId
    );
}