package com.example.TP4.exception;

public class PrestamoNoEncontradoException extends RuntimeException {
    public PrestamoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
