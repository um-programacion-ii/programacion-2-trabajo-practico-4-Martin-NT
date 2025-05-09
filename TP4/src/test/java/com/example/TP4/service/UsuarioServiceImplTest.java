package com.example.TP4.service;
import com.example.TP4.enums.EstadoUsuario;
import com.example.TP4.exception.UsuarioNoEncontradoException;
import com.example.TP4.model.Usuario;
import com.example.TP4.repository.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // Extiende el contexto de prueba para usar Mockito.
public class UsuarioServiceImplTest {
    private Usuario usuario1;
    private Usuario usuario2;
    private Usuario usuario3;

    @Mock  // Crea un objeto simulado del repositorio para que no se use uno real.
    private UsuarioRepository usuarioRepository;

    @InjectMocks  // Inyecta el mock del repositorio en el servicio que estamos probando.
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setUp() {
        usuario1 = new Usuario(1L, "Martina", "Rizzotti", "belen@example.com", EstadoUsuario.ACTIVO);
        usuario2 = new Usuario(2L, "Valentina", "Rosales", "paz@example.com", EstadoUsuario.INACTIVO);
        usuario3 = new Usuario(3L, "Facundo", "San Roman", "luciano@example.com", EstadoUsuario.SUSPENDIDO);
    }

    @Test
    void cuandoBuscarPorIdExiste_entoncesRetornaUsuario() {
        Long id = usuario1.getId();
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario1));

        Usuario resultado = usuarioService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(usuarioRepository).findById(id);
    }

    @Test
    void cuandoBuscarPorIdNoExiste_entoncesLanzaExcepcion() {
        Long id = usuario2.getId();
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () ->
                usuarioService.buscarPorId(id)
        );
    }

    @Test
    void cuandoBuscarPorNombreCompletoExiste_entoncesRetornaUsuario() {
        String nombre = usuario1.getNombre();
        String apellido = usuario1.getApellido();
        when(usuarioRepository.findByNombreCompleto(nombre, apellido)).thenReturn(Optional.of(usuario1));

        Usuario resultado = usuarioService.buscarPorNombreCompleto(nombre, apellido);

        assertNotNull(resultado);
        assertEquals(nombre, resultado.getNombre());
        assertEquals(apellido, resultado.getApellido());
        verify(usuarioRepository).findByNombreCompleto(nombre, apellido);
    }

    @Test
    void cuandoBuscarPorNombreCompletoNoExiste_entoncesLanzaExcepcion() {
        String nombre = usuario2.getNombre();
        String apellido = usuario2.getApellido();
        when(usuarioRepository.findByNombreCompleto(nombre, apellido)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () ->
                usuarioService.buscarPorNombreCompleto(nombre, apellido)
        );
    }

    @Test
    void testObtenerTodosLosUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario1, usuario2, usuario3));

        List<Usuario> todos = usuarioService.obtenerTodos();

        assertNotNull(todos);
        assertEquals(3, todos.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    void testGuardarUsuario() {
        when(usuarioRepository.save(usuario3)).thenReturn(usuario3);

        Usuario resultado = usuarioService.guardar(usuario3);

        assertNotNull(resultado);
        assertEquals(usuario3.getId(), resultado.getId());
        assertEquals(usuario3, resultado);
        verify(usuarioRepository).save(usuario3);
    }

    @Test
    void cuandoEliminarExiste_entoncesUsuarioEsEliminado() {
        Long id = usuario2.getId();
        when(usuarioRepository.existsById(id)).thenReturn(true);

        usuarioService.eliminar(id);

        verify(usuarioRepository).deleteById(id);
    }

    @Test
    void cuandoEliminarNoExiste_entoncesLanzaExcepcion() {
        Long id = usuario3.getId();
        when(usuarioRepository.existsById(id)).thenReturn(false);

        assertThrows(UsuarioNoEncontradoException.class, () -> usuarioService.eliminar(id));
    }

    @Test
    void cuandoActualizarExiste_entoncesUsuarioEsActualizado() {
        Long id = usuario3.getId();
        when(usuarioRepository.existsById(id)).thenReturn(true);
        when(usuarioRepository.save(usuario3)).thenReturn(usuario3);

        Usuario resultado = usuarioService.actualizar(id, usuario3);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(usuario3, resultado);
        verify(usuarioRepository).save(usuario3);
    }

    @Test
    void cuandoActualizarNoExiste_entoncesLanzaExcepcion() {
        Long id = usuario3.getId();
        when(usuarioRepository.existsById(id)).thenReturn(false);

        assertThrows(UsuarioNoEncontradoException.class, () -> usuarioService.actualizar(id, usuario3));
    }


}
