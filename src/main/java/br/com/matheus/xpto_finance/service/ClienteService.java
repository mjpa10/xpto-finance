package br.com.matheus.xpto_finance.service;

import br.com.matheus.xpto_finance.dto.ClienteDTO;
import br.com.matheus.xpto_finance.entity.Cliente;
import br.com.matheus.xpto_finance.exception.ResourceNotFoundException;
import br.com.matheus.xpto_finance.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public Cliente salvar(ClienteDTO dto){
        Cliente cliente = Cliente.builder()
                .nome(dto.getNome())
                .tipoPessoa(dto.getTipoPessoa())
                .cpf(dto.getCpf())
                .cnpj(dto.getCnpj())
                .telefone(dto.getTelefone())
                .saldoInicial(dto.getSaldoInicial())
                .ativo(true)
                .build();
        return repository.save(cliente);
    }

    public List<Cliente> listar() {
        return repository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    public Cliente atualizar(Long id, ClienteDTO dto){

        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        cliente.setNome(dto.getNome());
        cliente.setTelefone(dto.getTelefone());
        // tipoPessoa, cpf e cnpj ficam de fora — imutáveis após o cadastro

        return repository.save(cliente);
    }
    
    public void excluir(Long id){

        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        cliente.setAtivo(false);

        repository.save(cliente);
        ///repository.delete(cliente);
    }
}
