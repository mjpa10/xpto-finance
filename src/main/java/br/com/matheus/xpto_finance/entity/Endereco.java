package br.com.matheus.xpto_finance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ENDERECO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "endereco_seq")
    @SequenceGenerator(name = "endereco_seq",sequenceName = "ENDERECO_SEQ",allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String logradouro;

    @Column(nullable = false, length = 20)
    private String numero;

    private String complemento;

    @Column(nullable = false)
    private String bairro;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false, length = 2)
    private String uf;

    @Column(nullable = false, length = 8)
    private String cep;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) //muitos enderecos para um so cliente //optional = false Informa ao JPA que esse relacionamento é obrigatório.
    @JoinColumn(name = "CLIENTE_ID", nullable = false) //Significa que, no banco de dados, a coluna CLIENTE_ID não pode ser NULL.
    private Cliente cliente;
}