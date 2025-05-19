package br.com.cooperativa.votacao.api.exception;

import br.com.cooperativa.votacao.infrastructure.client.CPFClientFake;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CPFClientFake.CPFNotFoundException.class)
    public ResponseEntity<ErroDTO> handleCpfNotFound(CPFClientFake.CPFNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "CPF não encontrado no serviço externo.");
    }

    @ExceptionHandler(CPFClientFake.CPFUnableToVoteException.class)
    public ResponseEntity<ErroDTO> handleUnableToVote(CPFClientFake.CPFUnableToVoteException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "O CPF não está autorizado a votar.");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroDTO> handleEntityNotFound(EntityNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroDTO> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroDTO> handleIllegalState(IllegalStateException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDTO> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return buildResponse(HttpStatus.BAD_REQUEST, "Erro de validação", errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroDTO> handleInvalidFormat(HttpMessageNotReadableException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Formato do corpo da requisição inválido.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroDTO> handleDataIntegrity(DataIntegrityViolationException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Violação de integridade de dados. Possivelmente dados duplicados ou inválidos.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroDTO> handleGeneric(Exception ex) {
        log.error("Erro inesperado", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor. Contate o administrador.");
    }

    private ResponseEntity<ErroDTO> buildResponse(HttpStatus status, String message) {
        return buildResponse(status, message, null);
    }

    private ResponseEntity<ErroDTO> buildResponse(HttpStatus status, String message, Map<String, String> detalhes) {
        ErroDTO erro = ErroDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .erro(status.getReasonPhrase())
                .mensagem(message)
                .detalhes(detalhes)
                .build();

        return new ResponseEntity<>(erro, status);
    }

}
