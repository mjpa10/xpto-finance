package br.com.matheus.xpto_finance.mapper;

import br.com.matheus.xpto_finance.dto.ClienteResponseDTO;
import br.com.matheus.xpto_finance.dto.ContaResponseDTO;
import br.com.matheus.xpto_finance.dto.EnderecoResponseDTO;
import br.com.matheus.xpto_finance.dto.MovimentacaoResponseDTO;
import br.com.matheus.xpto_finance.entity.Cliente;
import br.com.matheus.xpto_finance.entity.Conta;
import br.com.matheus.xpto_finance.entity.Endereco;
import br.com.matheus.xpto_finance.entity.Movimentacao;
import br.com.matheus.xpto_finance.enums.TipoMovimentacao;
import br.com.matheus.xpto_finance.enums.TipoPessoa;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ClienteMapper {

    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        String documento = cliente.getTipoPessoa() == TipoPessoa.PF
                ? cliente.getCpf()
                : cliente.getCnpj();

        List<EnderecoResponseDTO> enderecos = cliente.getEnderecos().stream()
                .map(this::toEnderecoResponseDTO)
                .toList();

        List<ContaResponseDTO> contas = cliente.getContas().stream()
                .map(this::toContaResponseDTO)
                .toList();

        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .tipoPessoa(cliente.getTipoPessoa())
                .documento(documento)
                .telefone(cliente.getTelefone())
                .dataCadastro(cliente.getDataCadastro())
                .ativo(cliente.getAtivo())
                .enderecos(enderecos)
                .contas(contas)
                .build();
    }

    private EnderecoResponseDTO toEnderecoResponseDTO(Endereco endereco) {
        return EnderecoResponseDTO.builder()
                .id(endereco.getId())
                .logradouro(endereco.getLogradouro())
                .numero(endereco.getNumero())
                .complemento(endereco.getComplemento())
                .bairro(endereco.getBairro())
                .cidade(endereco.getCidade())
                .uf(endereco.getUf())
                .cep(endereco.getCep())
                .build();
    }

    private ContaResponseDTO toContaResponseDTO(Conta conta) {
        return ContaResponseDTO.builder()
                .id(conta.getId())
                .agencia(conta.getAgencia())
                .numero(conta.getNumero())
                .tipoConta(conta.getTipoConta())
                .saldo(conta.getSaldo())
                .dataAbertura(conta.getDataAbertura())
                .ativo(conta.getAtivo())
                .build();
    }
}