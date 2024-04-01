package ar.edu.utn.frc.tup.lciii.exceptions;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.utn.frc.tup.lciii.dtos.common.ErrorApi;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorApi> handleError(Exception exception) {
  ErrorApi error = buildError(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
}

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorApi> handleError(IllegalArgumentException exception) {
    ErrorApi error = buildError(exception.getMessage(), HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorApi> handleError(MethodArgumentNotValidException exception) {
    BindingResult result = exception.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();
    ErrorApi error = buildError(fieldErrors.get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ErrorApi> handleError(ResponseStatusException exception) {
    ErrorApi error = buildError(exception.getReason(), HttpStatus.valueOf(exception.getStatusCode().value()));
    return ResponseEntity.status(exception.getStatusCode()).body(error);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorApi> handleError(EntityNotFoundException exception) {
    ErrorApi error = buildError(exception.getMessage(), HttpStatus.NOT_FOUND);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  private ErrorApi buildError(String message, HttpStatus status) {
    return ErrorApi.builder()
            .timestamp(String.valueOf(Timestamp.from(ZonedDateTime.now().toInstant())))
            .error(status.getReasonPhrase())
            .status(status.value())
            .message(message)
            .build();
  }


  @ExceptionHandler(InvalidJwtException.class)
  public ResponseEntity<Map<String, List<String>>> handleJwtErrors(InvalidJwtException ex) {

    List<String> errors = List.of(ex.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsMap(errors));
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Map<String, List<String>>> handleBadCredentialsError(BadCredentialsException ex) {

    List<String> errors = List.of(ex.getMessage());

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorsMap(errors));
  }

  private Map<String, List<String>> errorsMap(List<String> errors) {
    Map<String, List<String>> errorResponse = new HashMap<>();
    errorResponse.put("errors", errors);
    return errorResponse;
  }

}