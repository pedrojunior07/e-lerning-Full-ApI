package com.elearning.e_learning_core.exceptions;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return new ApiResponse<>("error", "Já existe um usuário com este Email!", 400, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        Throwable cause = ex.getCause();

        // Verifica se o erro foi causado por enum inválido
        if (cause instanceof InvalidFormatException invalidFormatException) {
            String targetType = invalidFormatException.getTargetType().getSimpleName();

            if ("LevelCurseType".equals(targetType)) {
                String invalidValue = invalidFormatException.getValue().toString();

                // Pode personalizar a mensagem de erro aqui
                String message = "Nível inválido fornecido: " + invalidValue
                        + ". Valores válidos são: BEGINNER, INTERMEDIATE, ADVANCED, EXPERT.";

                // Retorna Bad Request com mensagem customizada
                return new ApiResponse<>(
                        "error",
                        message,
                        HttpStatus.BAD_REQUEST.value(),
                        null);
            }
        }

        return new ApiResponse<>(
                "error",
                cause.getMessage() != null ? cause.getMessage() : "Erro de formatação",
                HttpStatus.BAD_REQUEST.value(),
                null);
    }

    @ExceptionHandler(InvalidRoleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleInvalidRole(InvalidRoleException ex) {
        return new ApiResponse<>("error", ex.getMessage(), 400, null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleBadCredentials(BadCredentialsException ex) {
        return new ApiResponse<>("error", "Email ou senha inválidos", 401, null);
    }

    @ExceptionHandler(InvalidCourseDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleInvalidCourseData(InvalidCourseDataException ex) {
        return new ApiResponse<>("error", ex.getMessage(), 400, null);
    }

    @ExceptionHandler(CourseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleCourseNotFound(CourseNotFoundException ex) {
        return new ApiResponse<>("error", ex.getMessage(), 404, null);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleAuthenticationException(AuthenticationException ex) {
        return new ApiResponse<>("error", ex.getMessage(), 401, null);
    }

    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<ApiResponse<?>> handleEmailSendingException(EmailSendingException ex) {
        ApiResponse<?> response = new ApiResponse<>(
                "error",
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                null);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleGenericException(Exception ex, HttpServletRequest request) {
        return new ApiResponse<>("error", "Erro interno: " + ex.getMessage(), 500, request.getRequestURI());
    }
}