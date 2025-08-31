package com.calendario.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Festivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pais")
    private Pais pais;

    private String nombre;
    private Integer dia;
    private Integer mes;
    private Integer diasPascua;

    @ManyToOne
    @JoinColumn(name = "id_tipo")
    private TipoFestivo tipoFestivo;
}
