package com.example.TP4.service;
import com.example.TP4.exception.LibroNoEncontradoException;
import com.example.TP4.exception.PrestamoNoEncontradoException;
import com.example.TP4.exception.UsuarioNoEncontradoException;
import com.example.TP4.model.Libro;
import com.example.TP4.model.Prestamo;
import com.example.TP4.model.Usuario;
import com.example.TP4.repository.PrestamoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PrestamoServiceImpl implements PrestamoService {
    private final PrestamoRepository prestamoRepository;

    // Inyección de dependencias por constructor
    public PrestamoServiceImpl(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    public Prestamo buscarPorId(Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new PrestamoNoEncontradoException("Préstamo con " + id + " no encontrado."));
    }

    @Override
    public Prestamo buscarPorLibro(Libro libro) {
        return prestamoRepository.findByLibro(libro)
                .orElseThrow(() -> new LibroNoEncontradoException("No hay préstamo registrado para el libro con ISBN " + libro.getIsbn()));
    }

    @Override
    public Prestamo buscarPorUsuario(Usuario usuario) {
        return prestamoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No hay préstamo registrado para el usuario " + usuario.getNombre()));
    }

    @Override
    public List<Prestamo> buscarTodos() {
        return prestamoRepository.findAll();
    }

    @Override
    public Prestamo guardar(Prestamo prestamo) {
        return prestamoRepository.save(prestamo);
    }

    @Override
    public void eliminar(Long id) {
        if (!prestamoRepository.existsById(id)) {
            throw new PrestamoNoEncontradoException("No se encontró el préstamo con ID " + id);
        }
        prestamoRepository.deleteById(id);
    }

    @Override
    public Prestamo actualizar(Long id, Prestamo prestamo) {
        if (!prestamoRepository.existsById(id)) {
            throw new PrestamoNoEncontradoException("No se encontró el préstamo con ID " + id);
        }
        prestamo.setId(id);
        return prestamoRepository.save(prestamo);
    }
}
