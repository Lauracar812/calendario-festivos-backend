package com.calendario.repository;

import com.calendario.model.Festivo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FestivoRepository extends JpaRepository<Festivo, Long> {
    // Agrega este método para buscar festivos por país
    List<Festivo> findByPaisId(Long paisId);
}
