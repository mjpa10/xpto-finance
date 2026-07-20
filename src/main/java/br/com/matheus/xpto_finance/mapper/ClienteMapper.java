package br.com.matheus.xpto_finance.mapper;

import br.com.matheus.xpto_finance.dto.ClienteResponseDTO;
import br.com.matheus.xpto_finance.entity.Cliente;
import br.com.matheus.xpto_finance.enums.TipoPessoa;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        String documento = cliente.getTipoPessoa() == TipoPessoa.PF
                ? cliente.getCpf()
                : cliente.getCnpj();

        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getTipoPessoa(),
                documento,
                cliente.getTelefone(),
                cliente.getSaldoInicial(),
                cliente.getDataCadastro(),
                cliente.getAtivo()
        );
    }
}