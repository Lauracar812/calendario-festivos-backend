package com.calendario.repository;

import com.calendario.model.DiaCalendario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DiaCalendarioRepository extends JpaRepository<DiaCalendario, Long> {
    // Busca todos los días de un país en un año específico
    List<DiaCalendario> findByPaisIdAndFechaBetween(Long paisId, LocalDate fechaInicio, LocalDate fechaFin);
}