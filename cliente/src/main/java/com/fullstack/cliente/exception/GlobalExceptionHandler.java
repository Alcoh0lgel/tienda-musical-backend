package com.fullstack.cliente.exception;

import com.fullstack.cliente.dto.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        ApiErrorResponse error = new ApiErrorResponse();
        error.setFecha(LocalDateTime.now());
        error.setEstado(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        error.setMensaje("Ha ocurrido un error inesperado en el servidor");
        error.setRuta(request.getRequestURI());
        error.setErrores(null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        ApiErrorResponse error = new ApiErrorResponse();
        error.setFecha(LocalDateTime.now());
        error.setEstado(HttpStatus.BAD_REQUEST.value());
        error.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        error.setMensaje("Error de validación");
        error.setRuta(request.getRequestURI());
        error.setErrores(errores);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RunDuplicadoException.class)
    public org.springframework.http.ResponseEntity<Object> handleRunDuplicado(
            RunDuplicadoException ex,
            org.springframework.web.context.request.WebRequest request) {

        java.util.Map<String, Object> body = new java.util.LinkedHashMap<>();
        body.put("fecha", java.time.LocalDateTime.now().toString());
        body.put("estado", org.springframework.http.HttpStatus.BAD_REQUEST.value()); // 400
        body.put("error", "Bad Request");
        body.put("mensaje", ex.getMessage());
        body.put("ruta", ((org.springframework.web.context.request.ServletWebRequest) request).getRequest().getRequestURI());
        body.put("errores", null);

        return new org.springframework.http.ResponseEntity<>(body, org.springframework.http.HttpStatus.BAD_REQUEST);
    }
}