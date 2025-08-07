package com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
