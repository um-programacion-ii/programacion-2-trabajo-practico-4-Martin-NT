package com.example.TP4.repository;
import com.example.TP4.enums.EstadoUsuario;
import com.example.TP4.model.Usuario;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioRepositoryImplTest {
    private UsuarioRepository usuarioRepository;
    private Usuario usuario1;
    private Usuario usuario2;
    private Usuario usuario3;

    @BeforeEach
    void setUp() {
        usuarioRepository = new UsuarioRepositoryImpl();
        usuario1 = new Usuario(1L, "Martina", "Rizzotti", "belen@example.com", EstadoUsuario.ACTIVO);
        usuario2 = new Usuario(2L, "Valentina", "Rosales", "paz@example.com", EstadoUsuario.INACTIVO);
        usuario3 = new Usuario(3L, "Facundo", "San Roman", "luciano@example.com", EstadoUsuario.SUSPENDIDO);
    }

    @Test
    void saveUsuarioTest() {
        Usuario usuarioGuardado = usuarioRepository.save(usuario1);

        assertNotNull(usuarioGuardado);
        assertEquals(usuario1.getId(), usuarioGuardado.getId());
        assertEquals(usuario1 , usuarioGuardado);
    }

    @Test
    void findByIdTest() {
        Usuario usuarioGuardado = usuarioRepository.save(usuario1);
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findById(usuarioGuardado.getId());

        assertFalse(usuarioEncontrado.isEmpty());
        assertEquals(usuarioGuardado.getId(), usuarioEncontrado.get().getId());
        assertEquals(usuarioGuardado, usuarioEncontrado.get());
    }

    @Test
    void findByIdNotFoundTest() {
        assertTrue(usuarioRepository.findById(10L).isEmpty());
    }

    @Test
    void findByNombreCompletoTest() {
        Usuario usuarioGuardado = usuarioRepository.save(usuario1);
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByNombreCompleto(usuarioGuardado.getNombre() , usuarioGuardado.getApellido());

        assertFalse(usuarioEncontrado.isEmpty());
        assertEquals(usuarioGuardado.getNombre(), usuarioEncontrado.get().getNombre());
        assertEquals(usuarioGuardado.getApellido(), usuarioEncontrado.get().getApellido());
        assertEquals(usuarioGuardado, usuarioEncontrado.get());
    }

    @Test
    void findByNombreCompletoNotFoundTest() {
        assertTrue(usuarioRepository.findByNombreCompleto("Martin", "Navarro").isEmpty());
    }

    @Test
    void testFindAll() {
        usuarioRepository.save(usuario1);
        usuarioRepository.save(usuario2);
        usuarioRepository.save(usuario3);
        List<Usuario> usuarios = usuarioRepository.findAll();

        assertEquals(3, usuarios.size());
        assertTrue(usuarios.contains(usuario1));
        assertTrue(usuarios.contains(usuario2));
        assertTrue(usuarios.contains(usuario3));
    }

    @Test
    void testDeleteById() {
        Usuario usuarioGuardado = usuarioRepository.save(usuario2);
        usuarioRepository.deleteById(usuarioGuardado.getId());
        assertTrue(usuarioRepository.findById(usuarioGuardado.getId()).isEmpty());
    }

    @Test
    void testDeleteByIdNotFound() {
        usuarioRepository.deleteById(999L);  // ID que no ha sido guardado previamente
        assertTrue(usuarioRepository.findById(999L).isEmpty());
    }

    @Test
    void testExistsById() {
        Usuario usuarioGuardado = usuarioRepository.save(usuario3);
        assertTrue(usuarioRepository.existsById(usuarioGuardado.getId()));
    }

    @Test
    void testExistsByIdFalse() {
        assertFalse(usuarioRepository.existsById(999L));
    }


}
