package com.xbbg.springlab.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> notFound(NotFoundException exception, HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, exception.getMessage(), request, null);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> conflict(ConflictException exception, HttpServletRequest request) {
        return error(HttpStatus.CONFLICT, exception.getMessage(), request, null);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> badRequest(BadRequestException exception, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, exception.getMessage(), request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> invalid(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(fieldError -> fieldErrors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage()));
        return error(HttpStatus.BAD_REQUEST, "Request validation failed", request, fieldErrors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> unreadable(HttpMessageNotReadableException exception, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "Request body is not valid JSON", request, null);
    }

    private ResponseEntity<ApiError> error(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            Map<String, String> fieldErrors
    ) {
        ApiError body = new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                fieldErrors
        );
        return ResponseEntity.status(status).body(body);
    }
}
