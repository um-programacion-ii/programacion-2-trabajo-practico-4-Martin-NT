package com.example.TP4.service;
import com.example.TP4.exception.LibroNoEncontradoException;
import com.example.TP4.model.Libro;
import com.example.TP4.repository.LibroRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LibroServiceImpl implements LibroService {
    private final LibroRepository libroRepository;

    // Inyección de dependencias por constructor
    public LibroServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {
        return libroRepository.findByIsbn(isbn)
                .orElseThrow(() -> new LibroNoEncontradoException("Libro con ISBN " + isbn + " no encontrado."));
    }

    @Override
    public List<Libro> obtenerTodos() {
        return libroRepository.findAll();
    }

    @Override
    public Libro guardar(Libro libro) {
        return libroRepository.save(libro);
    }

    @Override
    public void eliminar(Long id) {
        if (!libroRepository.existsById(id)) {
            throw new LibroNoEncontradoException("No se encontró el libro con ID " + id);
        }
        libroRepository.deleteById(id);
    }

    @Override
    public Libro actualizar(Long id, Libro libro) {
        if (!libroRepository.existsById(id)) {
            throw new LibroNoEncontradoException("No se encontró el libro con ID " + id + " para actualizar.");
        }
        libro.setId(id);
        return libroRepository.save(libro);
    }
}
