package com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.output.jpa.adapter;

import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.model.Usuario;
import com.plazoletadecomidas.plazoleta_ms_usuarios.domain.spi.UsuarioPersistencePort;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.exception.UsuarioNotFindedException;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.output.jpa.entity.UsuarioEntity;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.output.jpa.mapper.UsuarioEntityMapper;
import com.plazoletadecomidas.plazoleta_ms_usuarios.infrastructure.output.jpa.repository.UsuarioRepository;

import java.util.Optional;
import java.util.UUID;

public class UsuarioJpaAdapter implements UsuarioPersistencePort {

    private final UsuarioRepository repository;
    private final UsuarioEntityMapper mapper;

    public UsuarioJpaAdapter(UsuarioRepository repository, UsuarioEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Usuario saveUsuario(Usuario usuario) {
        UsuarioEntity entity = mapper.toEntity(usuario);
        UsuarioEntity saved = repository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public boolean existsEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Usuario findByEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toModel)
                .orElseThrow(() -> new UsuarioNotFindedException("Usuario no encontrado con email: " + email));
    }

    @Override
    public Usuario createEmployee(Usuario usuario) {
        return saveUsuario(usuario);
    }

    @Override
    public Optional<Usuario> findById(UUID id) {
        return repository.findById(id)
                .map(entity -> new Usuario(
                        entity.getId(),
                        entity.getFirstName(),
                        entity.getLastName(),
                        entity.getDocumentId(),
                        entity.getPhone(),
                        entity.getBirthDate(),
                        entity.getEmail(),
                        entity.getPasswordHash(),
                        entity.getRole()
                ));
    }

}
