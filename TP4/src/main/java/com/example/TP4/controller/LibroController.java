package com.example.TP4.controller;
import com.example.TP4.exception.LibroNoEncontradoException;
import com.example.TP4.model.Libro;
import com.example.TP4.service.LibroService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroController {
    private final LibroService libroService;

    // Constructor que inyecta el servicio
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    // Endpoint para obtener todos los libros
    @GetMapping
    public List<Libro> obtenerTodos() {
        return libroService.obtenerTodos();
    }

    // Endpoint para obtener un libro por su ID
    @GetMapping("/{id}")
    public Libro obtenerPorId(@PathVariable Long id) {
        Libro libro = libroService.buscarPorId(id);
        if (libro == null) {
            // Lanzamos la excepción personalizada si el libro no se encuentra
            throw new LibroNoEncontradoException("Libro no encontrado con ID " + id);
        }
        return libro;
    }

    // Endpoint para obtener un libro por su ISBN
    @GetMapping("/isbn/{isbn}")
    public Libro obtenerPorIsbn(@PathVariable String isbn) {
        Libro libro = libroService.buscarPorIsbn(isbn);
        if (libro == null) {
            // Lanzamos la excepción si el libro no se encuentra por ISBN
            throw new LibroNoEncontradoException("Libro no encontrado con ISBN " + isbn);
        }
        return libro;
    }

    // Endpoint para crear un nuevo libro
    @PostMapping
    public Libro crear(@RequestBody Libro libro) {
        return libroService.guardar(libro);
    }

    // Endpoint para actualizar un libro
    @PutMapping("/{id}")
    public Libro actualizar(@PathVariable Long id, @RequestBody Libro libro) {
        Libro actualizado = libroService.actualizar(id, libro);
        if (actualizado == null) {
            throw new LibroNoEncontradoException("No se pudo actualizar. Libro no encontrado con ID " + id);
        }
        return actualizado;
    }

    // Endpoint para eliminar un libro
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        Libro libro = libroService.buscarPorId(id);
        if (libro == null) {
            throw new LibroNoEncontradoException("No se pudo eliminar. Libro no encontrado con ID " + id);
        }
        libroService.eliminar(id);
    }
}
