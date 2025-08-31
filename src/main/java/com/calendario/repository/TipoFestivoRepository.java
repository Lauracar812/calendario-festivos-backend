package com.calendario.repository;

import com.calendario.model.TipoFestivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoFestivoRepository extends JpaRepository<TipoFestivo, Long> {
}
