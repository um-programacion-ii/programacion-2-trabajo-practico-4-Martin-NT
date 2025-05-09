package com.example.TP4.controller;
import com.example.TP4.exception.UsuarioNoEncontradoException;
import com.example.TP4.model.Usuario;
import com.example.TP4.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con ID " + id);
        }
        return usuario;
    }

    @GetMapping("/nombre/{nombre}/apellido/{apellido}")
    public Usuario obtenerPorNombreCompleto(@PathVariable String nombre, @PathVariable String apellido) {
        Usuario usuario = usuarioService.buscarPorNombreCompleto(nombre, apellido);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con nombre " + nombre + " y apellido " + apellido);
        }
        return usuario;
    }

    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        return usuarioService.guardar(usuario);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario actualizado = usuarioService.actualizar(id, usuario);
        if (actualizado == null) {
            throw new UsuarioNoEncontradoException("No se pudo actualizar. Usuario no encontrado con ID " + id);
        }
        return actualizado;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("No se pudo eliminar. Usuario no encontrado con ID " + id);
        }
        usuarioService.eliminar(id);
    }
}
