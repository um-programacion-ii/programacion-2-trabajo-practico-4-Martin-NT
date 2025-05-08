package com.example.TP4.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice; // Permite manejar excepciones de forma global (para todos los controllers)
import org.springframework.web.bind.annotation.ExceptionHandler; // Especifica qué metodo manejará una excepción en particular

@ControllerAdvice // Indica que esta clase manejará excepciones lanzadas por cualquier controlador del proyecto
public class GlobalExceptionHandler {

    @ExceptionHandler(LibroNoEncontradoException.class) // Captura la excepción LibroNoEncontradoException
    public ResponseEntity<String> handleLibroNoEncontrado(LibroNoEncontradoException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); // Devuelve un 404 con el mensaje de error
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class) // Captura la excepción UsuarioNoEncontradoException
    public ResponseEntity<String> handleUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); // Devuelve un 404 con el mensaje de error
    }

    @ExceptionHandler(PrestamoNoEncontradoException.class) // Captura la excepción PrestamoNoEncontradoException
    public ResponseEntity<String> handlePrestamoNoEncontrado(PrestamoNoEncontradoException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); // Devuelve un 404 con el mensaje de error
    }
}
