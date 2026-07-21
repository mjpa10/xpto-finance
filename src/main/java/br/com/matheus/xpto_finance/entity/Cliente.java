package br.com.matheus.xpto_finance.entity;

import br.com.matheus.xpto_finance.enums.TipoPessoa;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    
    @CreationTimestamp
    private LocalDateTime dataCadastro;

    private Boolean ativo;

    // Um cliente pode possuir vários endereços.
    // Todas as operações feitas no cliente também serão aplicadas aos endereços.
    // Endereços removidos da lista serão excluídos do banco.
    @OneToMany(
            mappedBy = "cliente", //Indica que a entidade Endereco é responsável pela chave estrangeira.
            fetch = FetchType.LAZY, //Os endereços só serão carregados quando forem realmente acessados.
            cascade = CascadeType.ALL, //Ao salvar o cliente, os novos endereços adicionados também podem ser salvos.
            orphanRemoval = true // quando um endereço for removido da lista do cliente, ele será excluído da tabela ENDERECO
    )
    @Builder.Default
    private List<Endereco> enderecos = new ArrayList<>();

    // Um cliente pode possuir várias contas.
    // Ao cadastrar ou atualizar o cliente, suas contas também serão cadastradas ou atualizadas.
    @OneToMany(
            mappedBy = "cliente",
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @Builder.Default
    private List<Conta> contas = new ArrayList<>();

    /// / Adiciona um endereço ao cliente e mantém o relacionamento dos dois lados.
    public void adicionarEndereco(Endereco endereco) {
        enderecos.add(endereco);
        endereco.setCliente(this);
    }

    /// / Remove um endereço do cliente e desfaz o relacionamento.
    public void removerEndereco(Endereco endereco) {
        enderecos.remove(endereco);
        endereco.setCliente(null);
    }

    // Adiciona uma conta ao cliente e mantém o relacionamento dos dois lados.
    public void adicionarConta(Conta conta) {
        contas.add(conta);
        conta.setCliente(this);
    }
}
