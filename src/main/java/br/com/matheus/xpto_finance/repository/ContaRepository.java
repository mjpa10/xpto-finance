package br.com.matheus.xpto_finance.repository;

import br.com.matheus.xpto_finance.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository
        extends JpaRepository<Conta, Long> {

    //alem dos varios metodos herdados do jparepository, esse metodo Busca todas as contas pertencentes a determinado cliente.
    List<Conta> findAllByClienteId(Long clienteId);

    //Busca uma conta que tenha simultaneamente: o ID informado e o cliente informado.
    //O Optional evita retornar diretamente null e permite tratar o resultado
    Optional<Conta> findByIdAndClienteId(Long id, Long clienteId);

    //Verifica se já existe uma conta com aquela combinação de agência e número., porque so pode existir uma conta no db
    boolean existsByAgenciaAndNumero(String agencia, String numero);
}