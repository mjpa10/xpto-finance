package br.com.matheus.xpto_finance.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class TarifaService {

    // Usado para executar diretamente a função PL/SQL criada no Oracle.
    private final JdbcTemplate jdbcTemplate;

    public TarifaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //é utilizado datamovimentacao e nao IDmoviemntacao pq a movimentação ainda não existe no banco no momento em que a tarifa precisa ser calculada
    public BigDecimal calcularTarifa(Long clienteId, LocalDateTime dataMovimentacao) {

        // DUAL é uma tabela especial do Oracle usada para chamar funções
        // sem precisar consultar uma tabela real.
        return jdbcTemplate.queryForObject(
                "SELECT calcular_tarifa_movimentacao(?, ?) FROM DUAL",
                BigDecimal.class,
                clienteId,
                // Converte LocalDateTime para um tipo compatível com o Oracle.
                Timestamp.valueOf(dataMovimentacao)
        );
    }
}