package com.example.TP4.controller;
import com.example.TP4.enums.EstadoLibro;
import com.example.TP4.exception.LibroNoEncontradoException;
import com.example.TP4.model.Libro;
import com.example.TP4.service.LibroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibroController.class)
public class LibroControllerTest {
    private Libro libro1;
    private Libro libro2;
    private Libro libro3;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibroService libroService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        libro1 = new Libro(1L, "123-456-789", "Percy Jackson y El ladrón del rayo", "Rick Riordan", EstadoLibro.DISPONIBLE);
        libro2 = new Libro(2L, "987-654-321", "Harry Potter y la piedra filosofal", "J.K. Rowling", EstadoLibro.DISPONIBLE);
        libro3 = new Libro(3L, "111-222-333", "El señor de los anillos", "J.R.R. Tolkien", EstadoLibro.DISPONIBLE);
    }

    @Test
    void obtenerTodosTest() throws Exception {
        when(libroService.obtenerTodos()).thenReturn(List.of(libro1, libro2, libro3));

        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(libro1.getId()))
                .andExpect(jsonPath("$[0].titulo").value(libro1.getTitulo()))
                .andExpect(jsonPath("$[0].autor").value(libro1.getAutor()))
                .andExpect(jsonPath("$[0].isbn").value(libro1.getIsbn()))
                .andExpect(jsonPath("$[0].estado").value(libro1.getEstado().toString()))

                .andExpect(jsonPath("$[1].id").value(libro2.getId()))
                .andExpect(jsonPath("$[1].titulo").value(libro2.getTitulo()))
                .andExpect(jsonPath("$[1].autor").value(libro2.getAutor()))
                .andExpect(jsonPath("$[1].isbn").value(libro2.getIsbn()))
                .andExpect(jsonPath("$[1].estado").value(libro2.getEstado().toString()))

                .andExpect(jsonPath("$[2].id").value(libro3.getId()))
                .andExpect(jsonPath("$[2].titulo").value(libro3.getTitulo()))
                .andExpect(jsonPath("$[2].autor").value(libro3.getAutor()))
                .andExpect(jsonPath("$[2].isbn").value(libro3.getIsbn()))
                .andExpect(jsonPath("$[2].estado").value(libro3.getEstado().toString()));
    }

    @Test
    void obtenerPorIdTest() throws Exception {
        // Simulamos que el servicio devolverá un libro específico
        when(libroService.buscarPorId(libro1.getId())).thenReturn(libro1);

        // Realizamos la petición GET al endpoint correspondiente
        mockMvc.perform(get("/api/libros/{id}", libro1.getId()))
                .andExpect(status().isOk())  // Esperamos un 200 OK
                .andExpect(jsonPath("$.id").value(libro1.getId()))  // Comprobamos que el ID del libro es el correcto
                .andExpect(jsonPath("$.titulo").value(libro1.getTitulo()))  // Comprobamos que el título es el correcto
                .andExpect(jsonPath("$.autor").value(libro1.getAutor()))  // Comprobamos que el autor es el correcto
                .andExpect(jsonPath("$.isbn").value(libro1.getIsbn()))  // Comprobamos que el ISBN es el correcto
                .andExpect(jsonPath("$.estado").value(libro1.getEstado().toString()));  // Comprobamos que el estado es correcto
    }

    @Test
    void obtenerPorIdNoEncontradoTest() throws Exception {
        // Simulamos que el servicio lanza una excepción cuando no encuentra el libro
        Long idInexistente = 999L;
        String mensajeError = "Libro no encontrado con ID " + idInexistente;

        when(libroService.buscarPorId(idInexistente)).thenThrow(new LibroNoEncontradoException(mensajeError));

        // Ejecutamos la petición y validamos la respuesta
        mockMvc.perform(get("/api/libros/{id}", idInexistente))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(mensajeError));
    }

    @Test
    void obtenerPorIsbnTest() throws Exception {
        // Simulamos que el servicio devolverá un libro específico
        when(libroService.buscarPorIsbn(libro2.getIsbn())).thenReturn(libro2);

        // Realizamos la petición GET al endpoint correspondiente
        mockMvc.perform(get("/api/libros/isbn/{isbn}", libro2.getIsbn()))
                .andExpect(status().isOk())  // Esperamos un 200 OK
                .andExpect(jsonPath("$.id").value(libro2.getId()))  // Comprobamos que el ID del libro es el correcto
                .andExpect(jsonPath("$.titulo").value(libro2.getTitulo()))  // Comprobamos que el título es el correcto
                .andExpect(jsonPath("$.autor").value(libro2.getAutor()))  // Comprobamos que el autor es el correcto
                .andExpect(jsonPath("$.isbn").value(libro2.getIsbn()))  // Comprobamos que el ISBN es el correcto
                .andExpect(jsonPath("$.estado").value(libro2.getEstado().toString()));  // Comprobamos que el estado es correcto
    }

    @Test
    void obtenerPorIsbnNoEncontradoTest() throws Exception {
        // Simulamos que el servicio lanza una excepción cuando no encuentra el libro
        String isbnInexistente = "000-000-000";
        String mensajeError = "Libro no encontrado con ISBN " + isbnInexistente;

        when(libroService.buscarPorIsbn(isbnInexistente)).thenThrow(new LibroNoEncontradoException(mensajeError));

        // Ejecutamos la petición y validamos la respuesta
        mockMvc.perform(get("/api/libros/isbn/{isbn}", isbnInexistente))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(mensajeError));
    }

    @Test
    void crearLibroTest() throws Exception {
        when(libroService.guardar(libro2)).thenReturn(libro2);

        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libro2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(libro2.getId()))
                .andExpect(jsonPath("$.titulo").value(libro2.getTitulo()))
                .andExpect(jsonPath("$.autor").value(libro2.getAutor()))
                .andExpect(jsonPath("$.isbn").value(libro2.getIsbn()))
                .andExpect(jsonPath("$.estado").value(libro2.getEstado().toString()));
    }

    @Test
    void actualizarLibroTest() throws Exception {
        when(libroService.actualizar(eq(libro2.getId()), any(Libro.class))).thenReturn(libro2);

        mockMvc.perform(put("/api/libros/{id}", libro2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libro2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(libro2.getId()))
                .andExpect(jsonPath("$.titulo").value(libro2.getTitulo()))
                .andExpect(jsonPath("$.autor").value(libro2.getAutor()))
                .andExpect(jsonPath("$.isbn").value(libro2.getIsbn()))
                .andExpect(jsonPath("$.estado").value(libro2.getEstado().toString()));
    }

    @Test
    void eliminarLibroTest() throws Exception {
        // Simulamos que el libro existe y se puede eliminar correctamente
        when(libroService.buscarPorId(libro3.getId())).thenReturn(libro3);

        // Esperamos un 204 No Content si la eliminación es exitosa
        mockMvc.perform(delete("/api/libros/{id}", libro3.getId()))
                .andExpect(status().isNoContent());  // Esperamos un 204 No Content
    }
}
