package com.example.TP4.repository;
import com.example.TP4.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario save(Usuario usuario);
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByNombreCompleto(String nombre, String apellido);
    List<Usuario> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}
