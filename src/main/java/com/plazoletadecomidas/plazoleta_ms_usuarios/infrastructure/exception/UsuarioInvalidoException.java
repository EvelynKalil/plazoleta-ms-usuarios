package com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.exception;

public class UsuarioInvalidoException extends RuntimeException {
    public UsuarioInvalidoException(String mensaje) {
        super(mensaje);
    }
}
