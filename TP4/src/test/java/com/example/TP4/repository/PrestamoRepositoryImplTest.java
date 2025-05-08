package com.example.TP4.repository;
import com.example.TP4.enums.*;
import com.example.TP4.model.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class PrestamoRepositoryImplTest {
    private PrestamoRepository prestamoRepository;
    private Usuario usuario1;
    private Usuario usuario2;
    private Libro libro1;
    private Libro libro2;
    private Prestamo prestamo1;
    private Prestamo prestamo2;


    @BeforeEach
    void setUp() {
        prestamoRepository = new PrestamoRepositoryImpl();
        libro1 = new Libro(1L, "123-456-789", "Percy Jackson y El ladrón del rayo", "Rick Riordan", EstadoLibro.DISPONIBLE);
        libro2 = new Libro(2L, "987-654-321", "Harry Potter y la piedra filosofal", "J.K. Rowling", EstadoLibro.DISPONIBLE);
        usuario1 = new Usuario(1L, "Martina", "Rizzotti", "belen@example.com", EstadoUsuario.ACTIVO);
        usuario2 = new Usuario(2L, "Valentina", "Rosales", "paz@example.com", EstadoUsuario.ACTIVO);
        prestamo1 = new Prestamo(1L, libro1, usuario1, LocalDate.now(), LocalDate.now().plusDays(7));
        prestamo2 = new Prestamo(2L, libro2, usuario2, LocalDate.now(), LocalDate.now().plusDays(7));
    }

    @Test
    void savePrestamoTest() {
        Prestamo prestamoGuardado = prestamoRepository.save(prestamo1);

        assertNotNull(prestamoGuardado);
        assertEquals(prestamo1.getId(), prestamoGuardado.getId());
        assertEquals(prestamo1, prestamoGuardado);
    }

    @Test
    void findByIdTest() {
        Prestamo prestamoGuardado = prestamoRepository.save(prestamo1);
        Optional<Prestamo> prestamoEncontrado = prestamoRepository.findById(prestamoGuardado.getId());

        assertFalse(prestamoEncontrado.isEmpty());
        assertEquals(prestamoGuardado.getId(), prestamoEncontrado.get().getId());
        assertEquals(prestamoGuardado, prestamoEncontrado.get());
    }

    @Test
    void findByIdNotFoundTest() {
        assertTrue(prestamoRepository.findById(10L).isEmpty());
    }

    @Test
    void findByLibroTest() {
        Prestamo prestamoGuardado = prestamoRepository.save(prestamo1);
        Optional<Prestamo> prestamoEncontrado = prestamoRepository.findByLibro(libro1);

        assertFalse(prestamoEncontrado.isEmpty());
        assertEquals(libro1, prestamoEncontrado.get().getLibro()); // Verifica el libro asociado
        assertEquals(prestamoGuardado, prestamoEncontrado.get()); // Verifica que el objeto sea el mismo
    }

    @Test
    void findByLibroNotFoundTest() {
        Libro otroLibro = new Libro(2L, "999-888-777", "Libro No Prestado", "Autor X", EstadoLibro.DISPONIBLE);
        assertTrue(prestamoRepository.findByLibro(otroLibro).isEmpty());
    }

    @Test
    void findByUsuarioTest() {
        Prestamo prestamoGuardado = prestamoRepository.save(prestamo1);
        Optional<Prestamo> prestamoEncontrado = prestamoRepository.findByUsuario(usuario1);

        assertFalse(prestamoEncontrado.isEmpty());
        assertEquals(usuario1, prestamoEncontrado.get().getUsuario()); // Verifica el usuario asociado
        assertEquals(prestamoGuardado, prestamoEncontrado.get()); // Verifica que el objeto sea el mismo
    }

    @Test
    void findByUsuarioNotFoundTest() {
        Usuario otroUsuario = new Usuario(9L, "Juan", "Pérez", "juan@example.com", EstadoUsuario.ACTIVO);
        assertTrue(prestamoRepository.findByUsuario(otroUsuario).isEmpty());
    }

    @Test
    void testFindAll() {
        prestamoRepository.save(prestamo1);
        prestamoRepository.save(prestamo2);
        List<Prestamo> prestamos = prestamoRepository.findAll();

        assertEquals(2, prestamos.size());
        assertTrue(prestamos.contains(prestamo1));
        assertTrue(prestamos.contains(prestamo2));

    }

    @Test
    void testDeleteById() {
        Prestamo prestamoGuardado = prestamoRepository.save(prestamo2);
        prestamoRepository.deleteById(prestamoGuardado.getId());
        assertTrue(prestamoRepository.findById(prestamoGuardado.getId()).isEmpty());
    }

    @Test
    void testDeleteByIdNotFound() {
        prestamoRepository.deleteById(999L);  // ID que no ha sido guardado previamente
        assertTrue(prestamoRepository.findById(999L).isEmpty());
    }

    @Test
    void testExistsById() {
        Prestamo prestamoGuardado = prestamoRepository.save(prestamo2);
        assertTrue(prestamoRepository.existsById(prestamoGuardado.getId()));
    }

    @Test
    void testExistsByIdFalse() {
        assertFalse(prestamoRepository.existsById(999L));
    }


}
