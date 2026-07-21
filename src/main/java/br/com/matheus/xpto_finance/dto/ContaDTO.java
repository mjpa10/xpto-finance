package br.com.matheus.xpto_finance.dto;

import br.com.matheus.xpto_finance.enums.TipoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaDTO {

    @NotBlank(message = "Agência é obrigatória")
    private String agencia;

    @NotBlank(message = "Número da conta é obrigatório")
    private String numero;

    @NotNull(message = "Tipo da conta é obrigatório")
    private TipoConta tipoConta;

    @NotNull(message = "Saldo é obrigatório")
    @PositiveOrZero(message = "Saldo não pode ser negativo")
    private BigDecimal saldo;
}
