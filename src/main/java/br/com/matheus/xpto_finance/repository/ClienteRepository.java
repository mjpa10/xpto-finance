package br.com.matheus.xpto_finance.repository;


import br.com.matheus.xpto_finance.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository
        extends JpaRepository<Cliente, Long> {
}

