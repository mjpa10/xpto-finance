package br.com.matheus.xpto_finance.service;

import br.com.matheus.xpto_finance.dto.EnderecoDTO;
import br.com.matheus.xpto_finance.dto.EnderecoResponseDTO;
import br.com.matheus.xpto_finance.entity.Cliente;
import br.com.matheus.xpto_finance.entity.Endereco;
import br.com.matheus.xpto_finance.exception.ResourceNotFoundException;
import br.com.matheus.xpto_finance.mapper.EnderecoMapper;
import br.com.matheus.xpto_finance.repository.ClienteRepository;
import br.com.matheus.xpto_finance.repository.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository repository;
    private final ClienteRepository clienteRepository;
    private final EnderecoMapper mapper;

    public EnderecoResponseDTO criar(Long clienteId, EnderecoDTO dto) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .filter(c -> Boolean.TRUE.equals(c.getAtivo()))
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Endereco endereco = Endereco.builder()
                .logradouro(dto.getLogradouro())
                .numero(dto.getNumero())
                .complemento(dto.getComplemento())
                .bairro(dto.getBairro())
                .cidade(dto.getCidade())
                .uf(dto.getUf())
                .cep(dto.getCep())
                .cliente(cliente)
                .build();

        Endereco enderecoSalvo = repository.save(endereco);
        return mapper.toResponseDTO(enderecoSalvo);
    }

    public List<EnderecoResponseDTO> listarPorCliente(Long clienteId) {
        return repository.findAllByClienteId(clienteId).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    public EnderecoResponseDTO buscarPorId(Long clienteId, Long id) {
        Endereco endereco = repository.findByIdAndClienteId(id, clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));
        return mapper.toResponseDTO(endereco);
    }

    public EnderecoResponseDTO atualizar(Long clienteId, Long id, EnderecoDTO dto) {
        Endereco endereco = repository.findByIdAndClienteId(id, clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));

        endereco.setLogradouro(dto.getLogradouro());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setUf(dto.getUf());
        endereco.setCep(dto.getCep());

        Endereco enderecoAtualizado = repository.save(endereco);
        return mapper.toResponseDTO(enderecoAtualizado);
    }

    public void excluir(Long clienteId, Long id) {
        Endereco endereco = repository.findByIdAndClienteId(id, clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));
        repository.delete(endereco);
    }
}