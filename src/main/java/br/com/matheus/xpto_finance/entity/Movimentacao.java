package br.com.matheus.xpto_finance.entity;

import br.com.matheus.xpto_finance.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "MOVIMENTACAO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movimentacao {

    @Id
    @SequenceGenerator(
            name = "movimentacao_seq",
            sequenceName = "MOVIMENTACAO_SEQ",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "movimentacao_seq"
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_MOVIMENTACAO", nullable = false)
    private TipoMovimentacao tipo;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, length = 255)
    private String descricao;

    @CreationTimestamp
    @Column(
            name = "DATA_MOVIMENTACAO",
            nullable = false,
            updatable = false
    )
    private LocalDateTime dataMovimentacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CONTA_ID", nullable = false) //toda movimentacao precisa ser vinculada a uma conta
    private Conta conta;
}