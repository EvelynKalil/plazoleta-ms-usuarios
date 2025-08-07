package com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.exception;

public class UsuarioNotFindedException extends RuntimeException {
        public UsuarioNotFindedException(String mensaje) {
            super(mensaje);
        }
}
