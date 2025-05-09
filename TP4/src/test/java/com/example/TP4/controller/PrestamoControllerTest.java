package com.example.TP4.controller;
import com.example.TP4.enums.*;
import com.example.TP4.exception.PrestamoNoEncontradoException;
import com.example.TP4.model.*;
import com.example.TP4.service.PrestamoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrestamoController.class)
public class PrestamoControllerTest {
    private Usuario usuario1;
    private Usuario usuario2;
    private Libro libro1;
    private Libro libro2;
    private Prestamo prestamo1;
    private Prestamo prestamo2;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrestamoService prestamoService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void obtenerTodosTest() throws Exception {
        when(prestamoService.buscarTodos()).thenReturn(List.of(prestamo1, prestamo2));

        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(prestamo1.getId()))
                .andExpect(jsonPath("$[0].libro").value(prestamo1.getLibro()))
                .andExpect(jsonPath("$[0].usuario").value(prestamo1.getUsuario()))

                .andExpect(jsonPath("$[1].id").value(prestamo2.getId()))
                .andExpect(jsonPath("$[1].libro").value(prestamo2.getLibro()))
                .andExpect(jsonPath("$[1].usuario").value(prestamo2.getUsuario()));

    }

    @Test
    void obtenerPorIdTest() throws Exception {
        when(prestamoService.buscarPorId(prestamo1.getId())).thenReturn(prestamo1);

        // Realizamos la petición GET al endpoint correspondiente
        mockMvc.perform(get("/api/prestamos/{id}", prestamo1.getId()))
                .andExpect(status().isOk())  // Esperamos un 200 OK
                .andExpect(jsonPath("$.id").value(prestamo1.getId()))
                .andExpect(jsonPath("$.libro").value(prestamo1.getLibro()))
                .andExpect(jsonPath("$.usuario").value(prestamo1.getUsuario()));
    }

    @Test
    void obtenerPorIdNoEncontradoTest() throws Exception {
        Long idInexistente = 999L;
        String mensajeError = "Préstamo no encontrado con ID " + idInexistente;

        when(prestamoService.buscarPorId(idInexistente)).thenThrow(new PrestamoNoEncontradoException(mensajeError));

        // Ejecutamos la petición y validamos la respuesta
        mockMvc.perform(get("/api/prestamos/{id}", idInexistente))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(mensajeError));
    }

    @Test
    void obtenerPorLibroTest() throws Exception {
        when(prestamoService.buscarPorLibro(libro1)).thenReturn(prestamo1);

        mockMvc.perform(get("/api/prestamos/libro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libro1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(prestamo1.getId()))
                .andExpect(jsonPath("$.libro.id").value(libro1.getId()))
                .andExpect(jsonPath("$.usuario.id").value(usuario1.getId()));
    }

    @Test
    void obtenerPorLibroNoEncontradoTest() throws Exception {
        when(prestamoService.buscarPorLibro(libro1)).thenReturn(null);

        mockMvc.perform(get("/api/prestamos/libro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libro1)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No se encontró un préstamo para el libro con ID " + libro1.getId()));
    }

    @Test
    void obtenerPorUsuarioTest() throws Exception {
        when(prestamoService.buscarPorUsuario(usuario2)).thenReturn(prestamo2);

        mockMvc.perform(get("/api/prestamos/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(prestamo2.getId()))
                .andExpect(jsonPath("$.usuario.id").value(usuario2.getId()))
                .andExpect(jsonPath("$.libro.id").value(libro2.getId()));
    }

    @Test
    void obtenerPorUsuarioNoEncontradoTest() throws Exception {
        when(prestamoService.buscarPorUsuario(usuario2)).thenReturn(null);

        mockMvc.perform(get("/api/prestamos/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario2)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No se encontró un préstamo para el usuario con ID " + usuario2.getId()));
    }

    @Test
    void crearPrestamoTest() throws Exception {
        when(prestamoService.guardar(prestamo1)).thenReturn(prestamo1);

        mockMvc.perform(post("/api/prestamos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prestamo1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(prestamo1.getId()))
                .andExpect(jsonPath("$.usuario.id").value(usuario1.getId()))
                .andExpect(jsonPath("$.libro.id").value(libro1.getId()));
    }

    @Test
    void actualizarPrestamoTest() throws Exception {
        when(prestamoService.actualizar(eq(prestamo2.getId()), any(Prestamo.class))).thenReturn(prestamo2);

        mockMvc.perform(put("/api/prestamos/{id}", prestamo2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prestamo2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(prestamo2.getId()))
                .andExpect(jsonPath("$.usuario.id").value(usuario2.getId()))
                .andExpect(jsonPath("$.libro.id").value(libro2.getId()));
    }

    @Test
    void eliminarPrestamoTest() throws Exception {
        when(prestamoService.buscarPorId(prestamo2.getId())).thenReturn(prestamo2);

        // Esperamos un 204 No Content si la eliminación es exitosa
        mockMvc.perform(delete("/api/prestamos/{id}", prestamo2.getId()))
                .andExpect(status().isNoContent());  // Esperamos un 204 No Content
    }
}
