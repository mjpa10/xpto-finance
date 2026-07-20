package br.com.matheus.xpto_finance.entity;

import br.com.matheus.xpto_finance.enums.TipoPessoa;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "CLIENTE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_seq")
    @SequenceGenerator(name = "cliente_seq", sequenceName = "SEQ_CLIENTE", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPessoa tipoPessoa;

    @Column(length = 11)
    private String cpf;

    @Column(length = 14)
    private String cnpj;

    private String telefone;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoInicial;

    @CreationTimestamp
    private LocalDateTime dataCadastro;

    private Boolean ativo;
}
