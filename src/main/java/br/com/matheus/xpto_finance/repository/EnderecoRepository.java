package br.com.matheus.xpto_finance.repository;

import br.com.matheus.xpto_finance.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnderecoRepository
        extends JpaRepository<Endereco, Long> {

    // Busca todos os endereços pertencentes ao cliente informado
    List<Endereco> findAllByClienteId(Long clienteId);

    // Busca um endereço pelo seu ID e também pelo ID do cliente.
    // Retorna Optional porque o endereço pode não existir
    // ou pode não pertencer ao cliente informado.
    Optional<Endereco> findByIdAndClienteId(Long id, Long clienteId);
}