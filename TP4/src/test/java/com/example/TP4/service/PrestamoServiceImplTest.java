package com.example.TP4.service;
import com.example.TP4.enums.*;
import com.example.TP4.exception.*;
import com.example.TP4.model.*;
import com.example.TP4.repository.PrestamoRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // Extiende el contexto de prueba para usar Mockito.
public class PrestamoServiceImplTest {
    private Libro libro1;
    private Libro libro2;
    private Usuario usuario1;
    private Usuario usuario2;
    private Prestamo prestamo1;
    private Prestamo prestamo2;

    @Mock  // Crea un objeto simulado del repositorio para que no se use uno real.
    private PrestamoRepository prestamoRepository;

    @InjectMocks  // Inyecta el mock del repositorio en el servicio que estamos probando.
    public PrestamoServiceImpl prestamoService;

    @BeforeEach
    void setUp() {
        libro1 = new Libro(1L, "123-456-789", "Percy Jackson y El ladrón del rayo", "Rick Riordan", EstadoLibro.DISPONIBLE);
        libro2 = new Libro(2L, "987-654-321", "Harry Potter y la piedra filosofal", "J.K. Rowling", EstadoLibro.DISPONIBLE);
        usuario1 = new Usuario(1L, "Martina", "Rizzotti", "belen@example.com", EstadoUsuario.ACTIVO);
        usuario2 = new Usuario(2L, "Valentina", "Rosales", "paz@example.com", EstadoUsuario.ACTIVO);
        prestamo1 = new Prestamo(1L, libro1, usuario1, LocalDate.now(), LocalDate.now().plusDays(7));
        prestamo2 = new Prestamo(2L, libro2, usuario2, LocalDate.now(), LocalDate.now().plusDays(7));
    }

    @Test
    void cuandoBuscarPorIdExiste_entoncesRetornaPrestamo() {
        Long id = prestamo1.getId();
        when(prestamoRepository.findById(id)).thenReturn(Optional.of(prestamo1));

        Prestamo resultado = prestamoService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(prestamoRepository).findById(id);
    }

    @Test
    void cuandoBuscarPorIdNoExiste_entoncesLanzaExcepcion() {
        Long id = prestamo1.getId();
        when(prestamoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PrestamoNoEncontradoException.class, () ->
                prestamoService.buscarPorId(id)
        );
    }

    @Test
    void cuandoBuscarPorLibroExiste_entoncesRetornaPrestamo() {
        when(prestamoRepository.findByLibro(libro1)).thenReturn(Optional.of(prestamo1));

        Prestamo resultado = prestamoService.buscarPorLibro(libro1);

        assertNotNull(resultado);
        assertEquals(libro1.getIsbn(), resultado.getLibro().getIsbn());
        verify(prestamoRepository).findByLibro(libro1);
    }

    @Test
    void cuandoBuscarPorLibroNoExiste_entoncesLanzaExcepcion() {
        when(prestamoRepository.findByLibro(libro1)).thenReturn(Optional.empty());

        assertThrows(LibroNoEncontradoException.class, () ->
                prestamoService.buscarPorLibro(libro1)
        );
    }

    @Test
    void cuandoBuscarPorUsuarioExiste_entoncesRetornaPrestamo() {
        when(prestamoRepository.findByUsuario(usuario1)).thenReturn(Optional.of(prestamo1));
        Prestamo resultado = prestamoService.buscarPorUsuario(usuario1);

        assertNotNull(resultado);
        assertEquals(usuario1, resultado.getUsuario());
        verify(prestamoRepository).findByUsuario(usuario1);

    }

    @Test
    void cuandoBuscarPorUsuarioNoExiste_entoncesLanzaExcepcion() {
        when(prestamoRepository.findByUsuario(usuario1)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () ->
                prestamoService.buscarPorUsuario(usuario1)
        );
    }

    @Test
    void testObtenerTodosLosPrestamos() {
        when(prestamoRepository.findAll()).thenReturn(List.of(prestamo1, prestamo2));

        List<Prestamo> todos = prestamoService.buscarTodos();

        assertNotNull(todos);
        assertEquals(2, todos.size());
        verify(prestamoRepository).findAll();
    }

    @Test
    void testGuardarPrestamo() {
        when(prestamoRepository.save(prestamo1)).thenReturn(prestamo1);

        Prestamo resultado = prestamoService.guardar(prestamo1);

        assertNotNull(resultado);
        assertEquals(prestamo1.getId(), resultado.getId());
        assertEquals(prestamo1, resultado); // funciona solo si la clase Prestamo tiene un metodo equals() (y hashCode()) con @Data
        verify(prestamoRepository).save(prestamo1);
    }

    @Test
    void cuandoEliminarExiste_entoncesPrestamoEsEliminado() {
        Long id = prestamo2.getId();
        when(prestamoRepository.existsById(id)).thenReturn(true);

        prestamoService.eliminar(id);

        verify(prestamoRepository).deleteById(id);
    }

    @Test
    void cuandoEliminarNoExiste_entoncesLanzaExcepcion() {
        Long id = prestamo2.getId();
        when(prestamoRepository.existsById(id)).thenReturn(false);

        assertThrows(PrestamoNoEncontradoException.class, () -> prestamoService.eliminar(id));
    }

    @Test
    void cuandoActualizarExiste_entoncesPrestamoEsActualizado() {
        Long id = prestamo2.getId();
        when(prestamoRepository.existsById(id)).thenReturn(true);
        when(prestamoRepository.save(prestamo2)).thenReturn(prestamo2);

        Prestamo resultado = prestamoService.actualizar(id, prestamo2);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(prestamo2, resultado); // funciona solo si la clase Prestamo tiene un metodo equals() (y hashCode()) con @Data
        verify(prestamoRepository).save(prestamo2);
    }

    @Test
    void cuandoActualizarNoExiste_entoncesLanzaExcepcion() {
        Long id = prestamo2.getId();
        when(prestamoRepository.existsById(id)).thenReturn(false);

        assertThrows(PrestamoNoEncontradoException.class, () -> prestamoService.actualizar(id, prestamo2));
    }

}
