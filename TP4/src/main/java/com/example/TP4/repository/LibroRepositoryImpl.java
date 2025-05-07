package com.example.TP4.repository;
import com.example.TP4.model.Libro;
import org.springframework.stereotype.Repository;
import java.util.*;
// @Repository le dice a Spring que esta clase es un componente de acceso a datos (DAO)
// Spring la detecta automáticamente para inyectarla donde se necesite
@Repository
public class LibroRepositoryImpl implements LibroRepository {
    // Mapa que actúa como "base de datos en memoria"
    private final Map<Long, Libro> libros = new HashMap<>();
    // Contador para asignar IDs automáticos
    private Long nextId = 1L;

    // Guarda un libro. Si no tiene ID, le asigna uno nuevo.
    @Override
    public Libro save(Libro libro) {
        if (libro.getId() == null) {
            libro.setId(nextId++); // Asigna ID automático si es nuevo
        }
        libros.put(libro.getId(), libro); // Lo guarda o actualiza
        return libro;
    }

    @Override
    public Optional<Libro> findById(Long id) {
        // Retorna un Optional con el libro o vacío si no existe.
        return Optional.ofNullable(libros.get(id));
    }

    @Override
    public Optional<Libro> findByIsbn(String isbn) {
        // Esto retorna un Optional<Libro>, que podría ser vacío si no encuentra el libro.
        return libros.values().stream()
                .filter(libro -> libro.getIsbn().equals(isbn))
                .findFirst();
    }

    @Override
    public List<Libro> findAll() {
        return new ArrayList<>(libros.values());
    }

    @Override
    public void deleteById(Long id) {
        libros.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return libros.containsKey(id);
    }
}
