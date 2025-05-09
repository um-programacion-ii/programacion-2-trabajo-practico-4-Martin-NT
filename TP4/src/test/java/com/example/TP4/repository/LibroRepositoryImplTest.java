package com.example.TP4.repository;
import com.example.TP4.enums.EstadoLibro;
import com.example.TP4.model.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class LibroRepositoryImplTest {
    private LibroRepository libroRepository;
    private Libro libro1;
    private Libro libro2;
    private Libro libro3;

    @BeforeEach
    void setUp() {
        libroRepository = new LibroRepositoryImpl();
        libro1 = new Libro(1L, "123-456-789", "Percy Jackson y El ladrón del rayo", "Rick Riordan", EstadoLibro.DISPONIBLE);
        libro2 = new Libro(2L, "987-654-321", "Harry Potter y la piedra filosofal", "J.K. Rowling", EstadoLibro.DISPONIBLE);
        libro3 = new Libro(3L, "111-222-333", "El señor de los anillos", "J.R.R. Tolkien", EstadoLibro.DISPONIBLE);
    }

    @Test
    void saveLibroTest() {
        Libro libroGuardado = libroRepository.save(libro1);

        assertNotNull(libroGuardado);
        assertEquals(libro1.getTitulo(), libroGuardado.getTitulo());
        assertEquals(libro1 , libroGuardado);
    }

    @Test
    void findByIdTest() {
        Libro libroGuardado = libroRepository.save(libro2);
        Optional<Libro> libroEncontrado = libroRepository.findById(libroGuardado.getId());

        assertFalse(libroEncontrado.isEmpty());
        assertEquals(libroGuardado.getId(), libroEncontrado.get().getId());
        assertEquals(libroGuardado, libroEncontrado.get());
    }

    @Test
    void findByIdNotFoundTest() {
        assertTrue(libroRepository.findById(10L).isEmpty());
    }

    @Test
    void findByIsbnTest() {
        Libro libroGuardado = libroRepository.save(libro3);
        Optional<Libro> libroEncontrado = libroRepository.findByIsbn(libroGuardado.getIsbn());

        assertFalse(libroEncontrado.isEmpty());
        assertEquals(libroGuardado.getIsbn(), libroEncontrado.get().getIsbn());
        assertEquals(libroGuardado, libroEncontrado.get());
    }

    @Test
    void findByIsbnNotFoundTest() {
        assertTrue(libroRepository.findByIsbn("234-432-234").isEmpty());
    }

    @Test
    void testFindAll() {
        libroRepository.save(libro1);
        libroRepository.save(libro2);
        libroRepository.save(libro3);
        List<Libro> libros = libroRepository.findAll();

        assertEquals(3, libros.size());
        assertTrue(libros.contains(libro1));
        assertTrue(libros.contains(libro2));
        assertTrue(libros.contains(libro3));
    }

    @Test
    void testDeleteById() {
        Libro libroGuardado = libroRepository.save(libro1);
        libroRepository.deleteById(libroGuardado.getId());
        assertTrue(libroRepository.findById(libroGuardado.getId()).isEmpty());
    }

    @Test
    void testDeleteByIdNotFound() {
        libroRepository.deleteById(999L);  // ID que no ha sido guardado previamente
        assertTrue(libroRepository.findById(999L).isEmpty());
    }


    @Test
    void testExistsById() {
        Libro libroGuardado = libroRepository.save(libro2);
        assertTrue(libroRepository.existsById(libroGuardado.getId()));
    }

    @Test
    void testExistsByIdFalse() {
        assertFalse(libroRepository.existsById(999L));
    }

}
