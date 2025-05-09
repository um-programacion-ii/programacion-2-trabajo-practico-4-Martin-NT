package com.example.TP4.repository;
import com.example.TP4.model.Libro;
import com.example.TP4.model.Prestamo;
import com.example.TP4.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PrestamoRepositoryImpl implements PrestamoRepository {
    private final Map<Long, Prestamo> prestamos = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public Prestamo save(Prestamo prestamo) {
        if (prestamo.getId() == null){
            prestamo.setId(nextId++);
        }
        prestamos.put(prestamo.getId(), prestamo);
        return prestamo;
    }

    @Override
    public Optional<Prestamo> findById(Long id) {
        // Retorna un Optional con el prestamo o vacío si no existe.
        return Optional.ofNullable(prestamos.get(id));
    }

    @Override
    public Optional<Prestamo> findByLibro(Libro libro) {
        return prestamos.values().stream()
                .filter(p -> p.getLibro().equals(libro))
                .findFirst();
    }

    @Override
    public Optional<Prestamo> findByUsuario(Usuario usuario) {
        return prestamos.values().stream()
                .filter(prestamo -> prestamo.getUsuario().equals(usuario))
                .findFirst();
    }

    @Override
    public List<Prestamo> findAll() {
        return new ArrayList<>(prestamos.values());
    }

    @Override
    public void deleteById(Long id) {
        prestamos.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return prestamos.containsKey(id);
    }

}
