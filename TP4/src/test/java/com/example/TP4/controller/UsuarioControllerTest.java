package com.example.TP4.controller;
import com.example.TP4.enums.EstadoUsuario;
import com.example.TP4.exception.UsuarioNoEncontradoException;
import com.example.TP4.model.Usuario;
import com.example.TP4.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {
    private Usuario usuario1;
    private Usuario usuario2;
    private Usuario usuario3;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        usuario1 = new Usuario(1L, "Martina", "Rizzotti", "belen@example.com", EstadoUsuario.ACTIVO);
        usuario2 = new Usuario(2L, "Valentina", "Rosales", "paz@example.com", EstadoUsuario.INACTIVO);
        usuario3 = new Usuario(3L, "Facundo", "San Roman", "luciano@example.com", EstadoUsuario.SUSPENDIDO);
    }

    @Test
    void obtenerTodosTest() throws Exception {
        when(usuarioService.obtenerTodos()).thenReturn(List.of(usuario1, usuario2, usuario3));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(usuario1.getId()))
                .andExpect(jsonPath("$[0].nombre").value(usuario1.getNombre()))
                .andExpect(jsonPath("$[0].apellido").value(usuario1.getApellido()))
                .andExpect(jsonPath("$[0].email").value(usuario1.getEmail()))
                .andExpect(jsonPath("$[0].estado").value(usuario1.getEstado().toString()))

                .andExpect(jsonPath("$[1].id").value(usuario2.getId()))
                .andExpect(jsonPath("$[1].nombre").value(usuario2.getNombre()))
                .andExpect(jsonPath("$[1].apellido").value(usuario2.getApellido()))
                .andExpect(jsonPath("$[1].email").value(usuario2.getEmail()))
                .andExpect(jsonPath("$[1].estado").value(usuario2.getEstado().toString()))

                .andExpect(jsonPath("$[2].id").value(usuario3.getId()))
                .andExpect(jsonPath("$[2].nombre").value(usuario3.getNombre()))
                .andExpect(jsonPath("$[2].apellido").value(usuario3.getApellido()))
                .andExpect(jsonPath("$[2].email").value(usuario3.getEmail()))
                .andExpect(jsonPath("$[2].estado").value(usuario3.getEstado().toString()));
    }

    @Test
    void obtenerPorIdTest() throws Exception {
        when(usuarioService.buscarPorId(usuario1.getId())).thenReturn(usuario1);

        // Realizamos la petición GET al endpoint correspondiente
        mockMvc.perform(get("/api/usuarios/{id}", usuario1.getId()))
                .andExpect(status().isOk())  // Esperamos un 200 OK
                .andExpect(jsonPath("$.id").value(usuario1.getId()))
                .andExpect(jsonPath("$.nombre").value(usuario1.getNombre()))
                .andExpect(jsonPath("$.apellido").value(usuario1.getApellido()))
                .andExpect(jsonPath("$.email").value(usuario1.getEmail()))
                .andExpect(jsonPath("$.estado").value(usuario1.getEstado().toString()));
    }

    @Test
    void obtenerPorIdNoEncontradoTest() throws Exception {
        Long idInexistente = 999L;
        String mensajeError = "Usuario no encontrado con ID " + idInexistente;

        when(usuarioService.buscarPorId(idInexistente)).thenThrow(new UsuarioNoEncontradoException(mensajeError));

        // Ejecutamos la petición y validamos la respuesta
        mockMvc.perform(get("/api/usuarios/{id}", idInexistente))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(mensajeError));
    }

    @Test
    void obtenerPorNombreCompletoTest() throws Exception {
        when(usuarioService.buscarPorNombreCompleto(usuario1.getNombre(), usuario1.getApellido())).thenReturn(usuario1);

        // Realizamos la petición GET al endpoint correspondiente
        mockMvc.perform(get("/api/usuarios/nombre/{nombre}/apellido/{apellido}", usuario1.getNombre(), usuario1.getApellido()))
                .andExpect(status().isOk())  // Esperamos un 200 OK
                .andExpect(jsonPath("$.id").value(usuario1.getId()))
                .andExpect(jsonPath("$.nombre").value(usuario1.getNombre()))
                .andExpect(jsonPath("$.apellido").value(usuario1.getApellido()))
                .andExpect(jsonPath("$.email").value(usuario1.getEmail()))
                .andExpect(jsonPath("$.estado").value(usuario1.getEstado().toString()));
    }

    @Test
    void obtenerPorNombreCompletoNoEncontradoTest() throws Exception {
        String nombreInexistente = "Martin";
        String apellidoInexistente = "Navarro";
        String mensajeError = "Usuario no encontrado con nombre " + nombreInexistente + " y apellido " + apellidoInexistente;

        when(usuarioService.buscarPorNombreCompleto(nombreInexistente, apellidoInexistente)).thenThrow(new UsuarioNoEncontradoException(mensajeError));

        // Ejecutamos la petición y validamos la respuesta
        mockMvc.perform(get("/api/usuarios/nombre/{nombre}/apellido/{apellido}", nombreInexistente, apellidoInexistente))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(mensajeError));
    }

    @Test
    void crearUsuarioTest() throws Exception {
        when(usuarioService.guardar(usuario2)).thenReturn(usuario2);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuario2.getId()))
                .andExpect(jsonPath("$.nombre").value(usuario2.getNombre()))
                .andExpect(jsonPath("$.apellido").value(usuario2.getApellido()))
                .andExpect(jsonPath("$.email").value(usuario2.getEmail()))
                .andExpect(jsonPath("$.estado").value(usuario2.getEstado().toString()));
    }

    @Test
    void actualizarUsuarioTest() throws Exception {
        when(usuarioService.actualizar(eq(usuario2.getId()), any(Usuario.class))).thenReturn(usuario2);

        mockMvc.perform(put("/api/usuarios/{id}", usuario2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuario2.getId()))
                .andExpect(jsonPath("$.nombre").value(usuario2.getNombre()))
                .andExpect(jsonPath("$.apellido").value(usuario2.getApellido()))
                .andExpect(jsonPath("$.email").value(usuario2.getEmail()))
                .andExpect(jsonPath("$.estado").value(usuario2.getEstado().toString()));
    }

    @Test
    void eliminarUsuarioTest() throws Exception {
        when(usuarioService.buscarPorId(usuario3.getId())).thenReturn(usuario3);

        // Esperamos un 204 No Content si la eliminación es exitosa
        mockMvc.perform(delete("/api/usuarios/{id}", usuario3.getId()))
                .andExpect(status().isNoContent());  // Esperamos un 204 No Content
    }
}
