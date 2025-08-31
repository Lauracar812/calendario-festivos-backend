package com.calendario.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class TipoFestivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
}
