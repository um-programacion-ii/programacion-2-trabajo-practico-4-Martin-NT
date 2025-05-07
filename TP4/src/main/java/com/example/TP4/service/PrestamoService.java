package com.example.TP4.service;

import com.example.TP4.model.Libro;
import com.example.TP4.model.Prestamo;
import com.example.TP4.model.Usuario;

import java.util.List;

public interface PrestamoService {
    Prestamo buscarPorId(Long id);
    Prestamo buscarPorLibro(Libro libro);
    Prestamo buscarPorUsuario(Usuario usuario);
    List<Prestamo> buscarTodos();
    Prestamo guardar(Prestamo prestamo);
    void eliminar(Long id);
    Prestamo actualizar(Long id, Prestamo prestamo);

}
