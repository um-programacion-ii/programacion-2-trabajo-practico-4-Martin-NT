package com.example.TP4.service;
import com.example.TP4.model.Usuario;
import java.util.List;

public interface UsuarioService {
    Usuario buscarPorId(Long id);
    Usuario buscarPorNombreCompleto(String nombre, String apellido);
    List<Usuario>obtenerTodos();
    Usuario guardar(Usuario usuario);
    void eliminar(Long id);
    Usuario actualizar(Long id, Usuario usuario);
}
