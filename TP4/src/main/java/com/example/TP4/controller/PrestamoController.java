package com.example.TP4.controller;
import com.example.TP4.exception.PrestamoNoEncontradoException;
import com.example.TP4.model.*;
import com.example.TP4.service.PrestamoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {
    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping
    public List<Prestamo> obtenerTodos() {
        return prestamoService.buscarTodos();
    }

    @GetMapping("/{id}")
    public Prestamo obtenerPorId(@PathVariable Long id) {
        Prestamo prestamo = prestamoService.buscarPorId(id);
        if (prestamo == null) {
            throw new PrestamoNoEncontradoException("Préstamo no encontrado con ID " + id);
        }
        return prestamo;
    }

    @GetMapping("/libro")
    public Prestamo obtenerPorLibro(@RequestBody Libro libro) {
        Prestamo prestamo = prestamoService.buscarPorLibro(libro);
        if (prestamo == null) {
            throw new PrestamoNoEncontradoException("No se encontró un préstamo para el libro con ID " + libro.getId());
        }
        return prestamo;
    }

    @GetMapping("/usuario")
    public Prestamo obtenerPorUsuario(@RequestBody Usuario usuario) {
        Prestamo prestamo = prestamoService.buscarPorUsuario(usuario);
        if (prestamo == null) {
            throw new PrestamoNoEncontradoException("No se encontró un préstamo para el usuario con ID " + usuario.getId());
        }
        return prestamo;
    }

    @PostMapping
    public Prestamo crear(@RequestBody Prestamo prestamo) {
        return prestamoService.guardar(prestamo);
    }

    @PutMapping("/{id}")
    public Prestamo actualizar(@PathVariable Long id, @RequestBody Prestamo prestamo) {
        Prestamo actualizado = prestamoService.actualizar(id, prestamo);
        if (actualizado == null) {
            throw new PrestamoNoEncontradoException("No se pudo actualizar. Préstamo no encontrado con ID " + id);
        }
        return actualizado;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        Prestamo prestamo = prestamoService.buscarPorId(id);
        if (prestamo == null) {
            throw new PrestamoNoEncontradoException("No se pudo eliminar. Préstamo no encontrado con ID " + id);
        }
        prestamoService.eliminar(id);
    }
}
