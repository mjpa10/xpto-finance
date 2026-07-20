package br.com.matheus.xpto_finance.entity;

import br.com.matheus.xpto_finance.enums.TipoConta;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
//Cria uma regra no banco dizendo que a combinação entre AGENCIA e NUMERO não pode se repetir.
//Ou seja, o número da conta pode até se repetir, desde que seja em outra agência.
@Table(name = "CONTA", uniqueConstraints = {@UniqueConstraint(name = "UK_CONTA_AGENCIA_NUMERO", columnNames = {"AGENCIA", "NUMERO"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conta {

    @Id
    @SequenceGenerator(name = "conta_seq", sequenceName = "CONTA_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conta_seq")
    private Long id;

    @Column(name = "NUMERO", nullable = false, length = 20)
    private String numero;

    @Column(name = "AGENCIA", nullable = false, length = 10)
    private String agencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_CONTA", nullable = false)
    private TipoConta tipoConta;

    @Column(name = "SALDO", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    @CreationTimestamp
    @Column(name = "DATA_ABERTURA", nullable = false, updatable = false)
    private LocalDateTime dataAbertura;

    @Column(nullable = false)
    private Boolean ativo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CLIENTE_ID", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "conta", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private List<Movimentacao> movimentacoes = new ArrayList<>();

    //Adiciona metodo para vincular movimentação à conta
    public void adicionarMovimentacao(Movimentacao movimentacao) {
        movimentacoes.add(movimentacao);
        movimentacao.setConta(this);
    }
}