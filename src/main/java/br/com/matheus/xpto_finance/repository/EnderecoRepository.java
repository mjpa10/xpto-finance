package br.com.matheus.xpto_finance.repository;

import br.com.matheus.xpto_finance.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository
        extends JpaRepository<Endereco, Long> {
}