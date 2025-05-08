package com.example.TP4.controller;
import com.example.TP4.model.Libro;
import com.example.TP4.model.Prestamo;
import com.example.TP4.model.Usuario;
import com.example.TP4.service.PrestamoService;
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
        return prestamoService.buscarPorId(id);
    }

    @GetMapping("/libro")
    public Prestamo obtenerPorLibro(@RequestBody Libro libro) {
        return prestamoService.buscarPorLibro(libro);
    }

    @GetMapping("/usuario")
    public Prestamo obtenerPorUsuario(@RequestBody Usuario usuario) {
        return prestamoService.buscarPorUsuario(usuario);
    }

    @PostMapping
    public Prestamo crear (@RequestBody Prestamo prestamo) {
        return prestamoService.guardar(prestamo);
    }

    @PutMapping("/{id}")
    public Prestamo actualizar (@PathVariable Long id, @RequestBody Prestamo prestamo) {
        return prestamoService.actualizar(id, prestamo);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        prestamoService.eliminar(id);
    }
}
