package com.example.TP4.model;
import com.example.TP4.enums.EstadoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private EstadoUsuario estado;
}
