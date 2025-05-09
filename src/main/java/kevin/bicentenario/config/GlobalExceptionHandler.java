package kevin.bicentenario.config;


import kevin.bicentenario.constants.MensajesError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        HttpStatus status;
        String mensaje;

        switch (ex.getClass().getSimpleName()) {
            case "IllegalArgumentException" -> {
                status = HttpStatus.BAD_REQUEST;
                mensaje = MensajesError.ARGUMENTO_NO_VALIDO;
            }
            case "NullPointerException" -> {
                status = HttpStatus.BAD_REQUEST;
                mensaje = MensajesError.VALOR_NULO;
            }
            case "IllegalStateException" -> {
                status = HttpStatus.CONFLICT;
                mensaje = MensajesError.ESTADO_ILEGAL;
            }
            case "AccessDeniedException" -> {
                status = HttpStatus.FORBIDDEN;
                mensaje = MensajesError.ACCESO_DENEGADO;
            }
            case "ResourceNotFoundException" -> {
                status = HttpStatus.NOT_FOUND;
                mensaje = MensajesError.RECURSO_NO_ENCONTRADO;
            }
            default -> {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                mensaje = MensajesError.ERROR_INTERNO;
            }
        }

        return ResponseEntity.status(status).body(mensaje);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}