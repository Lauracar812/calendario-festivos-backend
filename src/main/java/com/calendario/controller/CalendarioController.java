package com.calendario.controller;

import com.calendario.service.FestivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calendario")
public class CalendarioController {

    @Autowired
    private FestivoService festivoService;

    // Endpoint para validar si una fecha es festiva
    @GetMapping("/verificar/{paisId}/{year}/{month}/{day}")
    public String verificarFestivo(
            @PathVariable Long paisId,
            @PathVariable int year,
            @PathVariable int month,
            @PathVariable int day
    ) {
        return festivoService.verificarFestivo(paisId, year, month, day);
    }

    // Endpoint para obtener todos los festivos de un país durante un año
    @GetMapping("/festivos/{paisId}/{year}")
    public List<Map<String, String>> listarFestivosPorAnio(
            @PathVariable Long paisId,
            @PathVariable int year
    ) {
        return festivoService.listarFestivosPorAnio(paisId, year);
    }

    // NUEVO ENDPOINT: Generar calendario laboral
    // Ejemplo de uso: POST /api/calendario/generar/1/2023
    @PostMapping("/generar/{paisId}/{year}")
    public boolean generarCalendarioLaboral(
            @PathVariable Long paisId,
            @PathVariable int year
    ) {
        return festivoService.generarCalendarioLaboral(paisId, year);
    }

    // NUEVO ENDPOINT: Retornar el calendario completo con los días clasificados
    // Ejemplo de uso: GET /api/calendario/listar/1/2023
    @GetMapping("/listar/{paisId}/{year}")
    public List<Map<String, Object>> obtenerCalendarioCompleto(
            @PathVariable Long paisId,
            @PathVariable int year
    ) {
        return festivoService.obtenerCalendarioCompleto(paisId, year);
    }
}