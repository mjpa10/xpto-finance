package br.com.matheus.xpto_finance.mapper;

import br.com.matheus.xpto_finance.dto.EnderecoResponseDTO;
import br.com.matheus.xpto_finance.entity.Endereco;
import org.springframework.stereotype.Component;

@Component
public class EnderecoMapper {
    public EnderecoResponseDTO toResponseDTO(Endereco endereco) {
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
}