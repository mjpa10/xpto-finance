package br.com.matheus.xpto_finance.exception;

import tools.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "erro", ex.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> erros = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() == null ? "Campo inválido" : fe.getDefaultMessage(),
                        (msg1, msg2) -> msg1
                ));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erros);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleMessageNotReadable(
            HttpMessageNotReadableException ex) {

        Throwable causa = ex;

        while (causa != null && !(causa instanceof InvalidFormatException)) {
            causa = causa.getCause();
        }

        if (causa instanceof InvalidFormatException ife
                && ife.getTargetType().isEnum()) {

            String campo = ife.getPath().isEmpty()
                    ? "campo"
                    : ife.getPath().get(0).getPropertyName();

            String valoresAceitos =
                    Arrays.toString(ife.getTargetType().getEnumConstants());

            return ResponseEntity.badRequest().body(Map.of(
                    campo,
                    "Valor inválido: '" + ife.getValue()
                            + "'. Valores aceitos: " + valoresAceitos
            ));
        }

        return ResponseEntity.badRequest().body(Map.of(
                "erro",
                "JSON inválido ou malformado"
        ));
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    public ResponseEntity<Map<String, String>> handleOperacaoNaoPermitida(OperacaoNaoPermitidaException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("erro", ex.getMessage()));
    }
}