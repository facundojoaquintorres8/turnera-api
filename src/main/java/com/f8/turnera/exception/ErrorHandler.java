package com.f8.turnera.exception;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

        // Esta excepcion es nativa de spring y respeta los tagueos de los DTOs
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ResponseDTO> methodArgumentNotValidException(HttpServletRequest request,
                        MethodArgumentNotValidException e) {
                // get spring errors
                BindingResult result = e.getBindingResult();
                List<FieldError> fieldErrors = result.getFieldErrors();

                // convert errors to standard string
                StringBuilder errorMessage = new StringBuilder();
                fieldErrors.forEach(f -> errorMessage.append(f.getDefaultMessage() + ". "));

                log.error(errorMessage.toString());
                // return error info object with standard json
                ResponseDTO response = new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Campos inválidos.",
                                request.getRequestURI());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ResponseDTO> constraintViolationException(HttpServletRequest request,
                        ConstraintViolationException e) {
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));

                // convert errors to standard string
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("Mensaje: ").append(e.getMessage()).append(" StackTrace: ")
                                .append(errors.toString());

                log.error(errorMessage.toString());
                // return error info object with standard json
                ResponseDTO response = new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Campos inválidos.",
                                request.getRequestURI());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ResponseDTO> exception(HttpServletRequest request, Exception e) {
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));

                // convert errors to standard string
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("Mensaje: ").append(e.getMessage()).append(" StackTrace: ")
                                .append(errors.toString());

                log.error(errorMessage.toString());
                // return error info object with standard json
                ResponseDTO response = new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "No se pudo realizar la operación. Intente más tarde.",
                                request.getRequestURI());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(ForbiddenException.class)
        public ResponseEntity<ResponseDTO> forbiddenException(HttpServletRequest request, ForbiddenException e) {
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));

                // convert errors to standard string
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("Mensaje: ").append(e.getMessage()).append(" StackTrace: ")
                                .append(errors.toString());

                log.error(errorMessage.toString());
                // return error info object with standard json
                ResponseDTO response = new ResponseDTO(HttpStatus.FORBIDDEN.value(), "Acceso no permitido.",
                                request.getRequestURI());
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
}
