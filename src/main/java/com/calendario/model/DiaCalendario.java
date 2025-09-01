package com.calendario.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class DiaCalendario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "id_pais")
    private Pais pais;

    // Puede ser "LABORAL", "FIN_SEMANA", "FESTIVO"
    private String tipo;
}
