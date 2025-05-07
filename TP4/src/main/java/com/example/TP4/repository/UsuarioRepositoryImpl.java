package com.example.TP4.repository;
import com.example.TP4.model.Usuario;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {
    private final Map<Long, Usuario> usuarios = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public Usuario save(Usuario usuario) {
        if (usuario.getId() == null) {
            usuario.setId(nextId++); // Asigna ID automático si es nuevo
        }
        usuarios.put(usuario.getId(), usuario); // Lo guarda o actualiza
        return usuario;
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        // Retorna un Optional con el usuario o vacío si no existe.
        return Optional.ofNullable(usuarios.get(id));
    }

    @Override
    public Optional<Usuario> findByNombreCompleto(String nombre, String apellido) {
        return usuarios.values().stream()
                .filter(usuario -> usuario.getNombre().equals(nombre) && usuario.getApellido().equals(apellido))
                .findFirst();
    }

    @Override
    public List<Usuario> findAll() {
        return new ArrayList<>(usuarios.values());
    }

    @Override
    public void deleteById(Long id) {
        usuarios.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return usuarios.containsKey(id);
    }
}
