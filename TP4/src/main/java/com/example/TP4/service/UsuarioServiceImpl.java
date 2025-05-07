package com.example.TP4.service;
import com.example.TP4.exception.UsuarioNoEncontradoException;
import com.example.TP4.model.Usuario;
import com.example.TP4.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;

    // Inyección de dependencias por constructor
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario con ID " + id + " no encontrado."));
    }

    @Override
    public Usuario buscarPorNombreCompleto(String nombre, String apellido){
        return usuarioRepository.findByNombreCompleto(nombre, apellido)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario: " + nombre + " " + apellido + " no encontrado."));
    }

    @Override
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNoEncontradoException("No se encontró el usuario con ID " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuario) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNoEncontradoException("No se encontró el usuario con ID " + id);
        }
        usuario.setId(id);
        return usuarioRepository.save(usuario);
    }
}
