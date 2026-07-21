package br.com.matheus.xpto_finance.exception;

public class OperacaoNaoPermitidaException extends RuntimeException {

    public OperacaoNaoPermitidaException(String message) {
        super(message);
    }
}