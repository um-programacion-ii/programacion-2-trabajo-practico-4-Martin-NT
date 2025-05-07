package com.example.TP4.controller;
import com.example.TP4.model.Libro;
import com.example.TP4.service.LibroService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController // Marca una clase como controlador REST (retorna JSON por defecto).
@RequestMapping("/api/libros") // Define el path base para todos los métodos del controlador
public class LibroController {
    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public List<Libro> obtenerTodos() {
        return libroService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Libro obtenerPorId(@PathVariable Long id) { // @PathVariable: Extrae valores de la URL, como /libros/{id}.
        return libroService.buscarPorId(id);
    }

    @GetMapping("/isbn/{isbn}")
    public Libro obtenerPorIsbn(@PathVariable String isbn) { // @PathVariable: Extrae valores de la URL, como /libros/{isbn}.
        return libroService.buscarPorIsbn(isbn);
    }

    @PostMapping
    public Libro crear(@RequestBody Libro libro) { // @RequestBody: Permite recibir un objeto JSON en el cuerpo del request.
        return libroService.guardar(libro);
    }

    @PutMapping("/{id}")
    public Libro actualizar(@PathVariable Long id, @RequestBody Libro libro) {
        return libroService.actualizar(id, libro);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        libroService.eliminar(id);
    }
}