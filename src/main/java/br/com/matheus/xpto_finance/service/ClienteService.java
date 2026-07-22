package br.com.matheus.xpto_finance.service;

import br.com.matheus.xpto_finance.dto.cliente.ClienteDTO;
import br.com.matheus.xpto_finance.dto.cliente.ClienteResponseDTO;
import br.com.matheus.xpto_finance.dto.Endereco.EnderecoDTO;
import br.com.matheus.xpto_finance.dto.cliente.ClienteUpdateDTO;
import br.com.matheus.xpto_finance.dto.conta.ContaDTO;
import br.com.matheus.xpto_finance.entity.Cliente;
import br.com.matheus.xpto_finance.entity.Conta;
import br.com.matheus.xpto_finance.entity.Endereco;
import br.com.matheus.xpto_finance.entity.Movimentacao;
import br.com.matheus.xpto_finance.enums.TipoMovimentacao;
import br.com.matheus.xpto_finance.exception.OperacaoNaoPermitidaException;
import br.com.matheus.xpto_finance.exception.ResourceNotFoundException;
import br.com.matheus.xpto_finance.mapper.ClienteMapper;
import br.com.matheus.xpto_finance.repository.ClienteRepository;
import br.com.matheus.xpto_finance.repository.ContaRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//O @Transactional é importante porque garante:
//ou salva Cliente + Endereços + Contas + Movimentações
//ou não salva nada Se ocorrer erro ao criar a movimentação inicial, o cliente não fica incompleto no banco.
@Transactional
@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;
    private final ContaRepository contaRepository;
    private final ClienteMapper mapper;
    private final EntityManager entityManager;

    public ClienteResponseDTO salvar(ClienteDTO dto) {

        Cliente cliente = Cliente.builder()
                .nome(dto.getNome())
                .tipoPessoa(dto.getTipoPessoa())
                .cpf(dto.getCpf())
                .cnpj(dto.getCnpj())
                .telefone(dto.getTelefone())
                .ativo(true)
                .build();

        for (EnderecoDTO enderecoDTO : dto.getEnderecos()) {

            Endereco endereco = Endereco.builder()
                    .logradouro(enderecoDTO.getLogradouro())
                    .numero(enderecoDTO.getNumero())
                    .complemento(enderecoDTO.getComplemento())
                    .bairro(enderecoDTO.getBairro())
                    .cidade(enderecoDTO.getCidade())
                    .uf(enderecoDTO.getUf())
                    .cep(enderecoDTO.getCep())
                    .build();

            cliente.adicionarEndereco(endereco);
        }

        for (ContaDTO contaDTO : dto.getContas()) {

            // checa se ja existe conta
            if (contaRepository.existsByAgenciaAndNumero(contaDTO.getAgencia(), contaDTO.getNumero())) {
                throw new OperacaoNaoPermitidaException(
                        "Já existe uma conta com agência " + contaDTO.getAgencia()
                                + " e número " + contaDTO.getNumero());
            }

            Conta conta = Conta.builder()
                    .agencia(contaDTO.getAgencia())
                    .numero(contaDTO.getNumero())
                    .tipoConta(contaDTO.getTipoConta())
                    .saldo(contaDTO.getSaldo())
                    .ativo(true)
                    .build();

            Movimentacao movimentacaoInicial = Movimentacao.builder()
                    .tipo(TipoMovimentacao.CREDITO)
                    .valor(contaDTO.getSaldo())
                    .descricao("Movimentação inicial")
                    .build();

            conta.adicionarMovimentacao(movimentacaoInicial);
            cliente.adicionarConta(conta);
        }

        Cliente clienteSalvo = repository.save(cliente);

        // força o banco a "devolver" os valores gerados por ele mesmo
        // (dataCadastro do cliente, dataAbertura de cada conta)
        entityManager.flush();
        entityManager.refresh(clienteSalvo);
        for (Conta conta : clienteSalvo.getContas()) {
            entityManager.refresh(conta);
        }

        return mapper.toResponseDTO(clienteSalvo);
    }

    public List<ClienteResponseDTO> listar() {
        return repository.findAll().stream()
                .filter(cliente -> Boolean.TRUE.equals(cliente.getAtivo()))
                .map(mapper::toResponseDTO)
                .toList();
    }

    public ClienteResponseDTO buscarPorId(Long id) {

        Cliente cliente = repository.findById(id)
                .filter(c -> Boolean.TRUE.equals(c.getAtivo()))
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente não encontrado")
                );

        return mapper.toResponseDTO(cliente);
    }

    public ClienteResponseDTO atualizar(Long id, ClienteUpdateDTO dto) {

        Cliente cliente = repository.findById(id)
                .filter(c -> Boolean.TRUE.equals(c.getAtivo()))
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente não encontrado")
                );

        cliente.setNome(dto.getNome());
        cliente.setTelefone(dto.getTelefone());
        // tipoPessoa, cpf e cnpj ficam de fora — imutáveis após o cadastro

        Cliente clienteAtualizado = repository.save(cliente);

        return mapper.toResponseDTO(clienteAtualizado);
    }

    public void excluir(Long id) {

        Cliente cliente = repository.findById(id)
                .filter(c -> Boolean.TRUE.equals(c.getAtivo()))
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cliente não encontrado")
                );

        cliente.setAtivo(false);

        repository.save(cliente);
        /// repository.delete(cliente);
    }
}
