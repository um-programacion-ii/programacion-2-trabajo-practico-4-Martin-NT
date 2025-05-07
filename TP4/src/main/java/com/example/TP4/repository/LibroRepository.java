package com.example.TP4.repository;
import com.example.TP4.model.Libro;
import java.util.List;
import java.util.Optional;

// Repository se encarga de acceder a los datos (base de datos, memoria, etc.).
// La interfaz define qué operaciones están disponibles, pero no cómo se hacen (eso lo hace una clase que la implementa).

public interface LibroRepository {
    Libro save(Libro libro);
    Optional<Libro> findById(Long id);
    Optional<Libro> findByIsbn(String isbn);
    List<Libro> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}
