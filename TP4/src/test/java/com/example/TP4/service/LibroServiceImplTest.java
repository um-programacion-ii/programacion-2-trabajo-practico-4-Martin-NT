package com.example.TP4.service;
import com.example.TP4.enums.EstadoLibro;
import com.example.TP4.exception.LibroNoEncontradoException;
import com.example.TP4.model.Libro;
import com.example.TP4.repository.LibroRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // Extiende el contexto de prueba para usar Mockito.
class LibroServiceImplTest {
    // Libros comunes que se usan en varias pruebas
    private Libro libro1;
    private Libro libro2;
    private Libro libro3;

    @Mock  // Crea un objeto simulado del repositorio para que no se use uno real.
    private LibroRepository libroRepository;

    @InjectMocks  // Inyecta el mock del repositorio en el servicio que estamos probando.
    private LibroServiceImpl libroService;

    @BeforeEach
    void setUp() {
        // Inicializo los libros que se van a usar en las pruebas
        libro1 = new Libro(1L, "123-456-789", "Percy Jackson y El ladrón del rayo", "Rick Riordan", EstadoLibro.DISPONIBLE);
        libro2 = new Libro(2L, "987-654-321", "Harry Potter y la piedra filosofal", "J.K. Rowling", EstadoLibro.PRESTADO);
        libro3 = new Libro(3L, "111-222-333", "El señor de los anillos", "J.R.R. Tolkien", EstadoLibro.DISPONIBLE);
    }
    @Test
    void cuandoBuscarPorIdExiste_entoncesRetornaLibro() {
        // Arrange (Prepara los datos para la prueba)
        Long id = libro1.getId();

        // Se indica que, cuando se llame a findById con el ID 1L, se debe devolver el libro esperado.
        when(libroRepository.findById(id)).thenReturn(Optional.of(libro1));

        // Act
        Libro resultado = libroService.buscarPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(libroRepository).findById(id);
    }

    @Test
    void cuandoBuscarPorIdNoExiste_entoncesLanzaExcepcion() {
        // Arrange (Prepara los datos para la prueba)
        Long id = libro1.getId();
        // Se indica que, cuando se llame a findById con el ID del libro1, no se debe encontrar el libro.
        when(libroRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert (Ejecuta el metodo y verifica que lanza la excepción esperada)
        assertThrows(LibroNoEncontradoException.class, () ->  // Verifica que se lanza la excepción adecuada si no se encuentra el libro.
                libroService.buscarPorId(id)  // Llama al metodo que debe lanzar la excepción.
        );
    }

    @Test
    void cuandoBuscarPorIsbnExiste_entoncesRetornaLibro() {
        // Arrange (Prepara los datos para la prueba)
        String isbn = libro2.getIsbn();

        // Se indica que, cuando se llame a findByIsbn con el ISBN del libro2, se debe devolver el libro esperado.
        when(libroRepository.findByIsbn(isbn)).thenReturn(Optional.of(libro2));

        // Act (Ejecuta el metodo que queremos probar)
        Libro resultado = libroService.buscarPorIsbn(isbn);  // Llama al metodo que estamos probando.

        // Assert (Verifica que el resultado sea correcto)
        assertNotNull(resultado);  // Verifica que el libro no sea null.
        assertEquals(isbn, resultado.getIsbn());  // Verifica que el ISBN del libro encontrado sea el esperado.
        verify(libroRepository).findByIsbn(isbn);  // Verifica que se haya llamado correctamente al repositorio.
    }

    @Test
    void cuandoBuscarPorIsbnNoExiste_entoncesLanzaExcepcion() {
        // Arrange (Prepara los datos para la prueba)
        String isbn = libro3.getIsbn();
        // Se indica que, cuando se llame a findByIsbn con el ISBN del libro3, no se debe encontrar el libro.
        when(libroRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        // Act & Assert (Ejecuta el metodo y verifica que lanza la excepción esperada)
        assertThrows(LibroNoEncontradoException.class, () ->  // Verifica que se lanza la excepción adecuada si no se encuentra el libro.
                libroService.buscarPorIsbn(isbn)  // Llama al metodo que debe lanzar la excepción.
        );
    }

    @Test
    void testObtenerTodosLosLibros() {
        // Arrange
        when(libroRepository.findAll()).thenReturn(List.of(libro1, libro2, libro3));

        // Act
        List<Libro> todos = libroService.obtenerTodos();

        // Assert
        assertNotNull(todos);
        assertEquals(3, todos.size());
        verify(libroRepository).findAll();
    }


    @Test
    void testGuardarLibro() {
        // Arrange
        when(libroRepository.save(libro1)).thenReturn(libro1);

        // Act
        Libro resultado = libroService.guardar(libro1);

        // Assert
        assertNotNull(resultado);
        assertEquals(libro1.getIsbn(), resultado.getIsbn());
        assertEquals(libro1, resultado);
        verify(libroRepository).save(libro1);
    }

    @Test
    void cuandoEliminarExiste_entoncesLibroEsEliminado() {
        // Arrange
        Long id = libro1.getId();
        when(libroRepository.existsById(id)).thenReturn(true);

        // Act
        libroService.eliminar(id);

        // Assert
        verify(libroRepository).deleteById(id);
    }

    @Test
    void cuandoEliminarNoExiste_entoncesLanzaExcepcion() {
        // Arrange
        Long id = libro1.getId();
        when(libroRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(LibroNoEncontradoException.class, () -> libroService.eliminar(id));
    }

    @Test
    void cuandoActualizarExiste_entoncesLibroEsActualizado() {
        // Arrange
        Long id = libro3.getId();
        when(libroRepository.existsById(id)).thenReturn(true);
        when(libroRepository.save(libro3)).thenReturn(libro3);

        // Act
        Libro resultado = libroService.actualizar(id, libro3);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(libro3, resultado);
        verify(libroRepository).save(libro3);
    }

    @Test
    void cuandoActualizarNoExiste_entoncesLanzaExcepcion() {
        // Arrange
        Long id = libro3.getId();
        when(libroRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(LibroNoEncontradoException.class, () -> libroService.actualizar(id, libro3));
    }

}
