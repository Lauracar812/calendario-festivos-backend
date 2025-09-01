package com.calendario.service;

import com.calendario.model.Festivo;
import com.calendario.model.Pais;
import com.calendario.model.TipoFestivo;
import com.calendario.model.DiaCalendario;
import com.calendario.repository.FestivoRepository;
import com.calendario.repository.PaisRepository;
import com.calendario.repository.DiaCalendarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class FestivoService {

    @Autowired
    private FestivoRepository festivoRepository;

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private DiaCalendarioRepository diaCalendarioRepository;

    /**
     * Verifica si una fecha es festiva para un país.
     * @param paisId id del país
     * @param year año
     * @param month mes (1-12)
     * @param day día (1-31)
     * @return String: "Es festivo", "No es festivo" o "Fecha no válida"
     */
    public String verificarFestivo(Long paisId, int year, int month, int day) {
        LocalDate fecha;
        try {
            fecha = LocalDate.of(year, month, day);
        } catch (Exception e) {
            return "Fecha no válida";
        }

        Optional<Pais> paisOpt = paisRepository.findById(paisId);
        if (paisOpt.isEmpty()) {
            return "País no válido";
        }
        Pais pais = paisOpt.get();

        List<Festivo> festivos = festivoRepository.findByPaisId(pais.getId());

        for (Festivo festivo : festivos) {
            TipoFestivo tipo = festivo.getTipoFestivo();
            String tipoNombre = tipo.getTipo().trim().toLowerCase();

            if (tipoNombre.contains("fijo") && !tipoNombre.contains("puente") && !tipoNombre.contains("pascua")) {
                if (festivo.getDia() != null && festivo.getMes() != null) {
                    if (festivo.getDia() == day && festivo.getMes() == month) {
                        return "Es festivo (fijo): " + festivo.getNombre();
                    }
                }
            } else if (tipoNombre.contains("puente") && !tipoNombre.contains("pascua")) {
                if (festivo.getDia() != null && festivo.getMes() != null) {
                    LocalDate original = LocalDate.of(year, festivo.getMes(), festivo.getDia());
                    LocalDate trasladado = moverAlLunes(original);
                    if (trasladado.equals(fecha)) {
                        return "Es festivo (puente): " + festivo.getNombre();
                    }
                }
            } else if (tipoNombre.contains("pascua") && !tipoNombre.contains("puente")) {
                if (festivo.getDiasPascua() != null) {
                    LocalDate pascua = calcularFechaPascua(year);
                    LocalDate festivoPascua = pascua.plusDays(festivo.getDiasPascua());
                    if (festivoPascua.equals(fecha)) {
                        return "Es festivo (pascua): " + festivo.getNombre();
                    }
                }
            } else if (tipoNombre.contains("pascua") && tipoNombre.contains("puente")) {
                if (festivo.getDiasPascua() != null) {
                    LocalDate pascua = calcularFechaPascua(year);
                    LocalDate festivoPascua = pascua.plusDays(festivo.getDiasPascua());
                    LocalDate trasladado = moverAlLunes(festivoPascua);
                    if (trasladado.equals(fecha)) {
                        return "Es festivo (pascua + puente): " + festivo.getNombre();
                    }
                }
            }
        }
        return "No es festivo";
    }

    /**
     * Método que lista todos los festivos de un país en un año.
     * @param paisId id del país
     * @param year año
     * @return Lista de mapas {"festivo": nombre, "fecha": yyyy-MM-dd}
     */
    public List<Map<String, String>> listarFestivosPorAnio(Long paisId, int year) {
        List<Map<String, String>> resultado = new ArrayList<>();

        Optional<Pais> paisOpt = paisRepository.findById(paisId);
        if (paisOpt.isEmpty()) {
            return resultado;
        }
        Pais pais = paisOpt.get();

        List<Festivo> festivos = festivoRepository.findByPaisId(pais.getId());

        for (Festivo festivo : festivos) {
            TipoFestivo tipo = festivo.getTipoFestivo();
            String tipoNombre = tipo.getTipo().trim().toLowerCase();
            LocalDate fechaFestivo = null;

            if (tipoNombre.contains("fijo") && !tipoNombre.contains("puente") && !tipoNombre.contains("pascua")) {
                if (festivo.getDia() != null && festivo.getMes() != null) {
                    fechaFestivo = LocalDate.of(year, festivo.getMes(), festivo.getDia());
                }
            } else if (tipoNombre.contains("puente") && !tipoNombre.contains("pascua")) {
                if (festivo.getDia() != null && festivo.getMes() != null) {
                    LocalDate original = LocalDate.of(year, festivo.getMes(), festivo.getDia());
                    fechaFestivo = moverAlLunes(original);
                }
            } else if (tipoNombre.contains("pascua") && !tipoNombre.contains("puente")) {
                if (festivo.getDiasPascua() != null) {
                    LocalDate pascua = calcularFechaPascua(year);
                    fechaFestivo = pascua.plusDays(festivo.getDiasPascua());
                }
            } else if (tipoNombre.contains("pascua") && tipoNombre.contains("puente")) {
                if (festivo.getDiasPascua() != null) {
                    LocalDate pascua = calcularFechaPascua(year);
                    LocalDate festivoPascua = pascua.plusDays(festivo.getDiasPascua());
                    fechaFestivo = moverAlLunes(festivoPascua);
                }
            }

            if (fechaFestivo != null) {
                Map<String, String> map = new HashMap<>();
                map.put("festivo", festivo.getNombre());
                map.put("fecha", fechaFestivo.toString());
                resultado.add(map);
            }
        }

        resultado.sort(Comparator.comparing(m -> m.get("fecha")));

        return resultado;
    }

    /**
     * Mueve una fecha al siguiente lunes si no cae en lunes.
     * @param fecha original
     * @return fecha en lunes
     */
    private LocalDate moverAlLunes(LocalDate fecha) {
        int dayOfWeek = fecha.getDayOfWeek().getValue();
        if (dayOfWeek == 1) {
            return fecha;
        } else {
            return fecha.plusDays(8 - dayOfWeek);
        }
    }

    /**
     * Algoritmo para calcular la fecha de Pascua (Meeus/Jones/Butcher).
     * @param year Año
     * @return Fecha de Pascua (domingo)
     */
    private LocalDate calcularFechaPascua(int year) {
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int mes = (h + l - 7 * m + 114) / 31;
        int dia = ((h + l - 7 * m + 114) % 31) + 1;
        return LocalDate.of(year, mes, dia);
    }

    /**
     * Genera y almacena todos los días del año para el país, clasificándolos como:
     * LABORAL, FIN_SEMANA o FESTIVO. Retorna true si el proceso fue exitoso.
     * @param paisId id del país
     * @param year año
     * @return booleano de éxito
     */
    public boolean generarCalendarioLaboral(Long paisId, int year) {
        Optional<Pais> paisOpt = paisRepository.findById(paisId);
        if (paisOpt.isEmpty()) return false;
        Pais pais = paisOpt.get();

        List<Map<String, String>> festivos = listarFestivosPorAnio(paisId, year);
        Set<LocalDate> fechasFestivas = new HashSet<>();
        for (Map<String, String> festivo : festivos) {
            fechasFestivas.add(LocalDate.parse(festivo.get("fecha")));
        }

        List<DiaCalendario> dias = new ArrayList<>();
        LocalDate inicio = LocalDate.of(year, 1, 1);
        LocalDate fin = LocalDate.of(year, 12, 31);
        for (LocalDate fecha = inicio; !fecha.isAfter(fin); fecha = fecha.plusDays(1)) {
            DiaCalendario dia = new DiaCalendario();
            dia.setFecha(fecha);
            dia.setPais(pais);

            if (fechasFestivas.contains(fecha)) {
                dia.setTipo("FESTIVO");
            } else if (fecha.getDayOfWeek().getValue() >= 6) {
                dia.setTipo("FIN_SEMANA");
            } else {
                dia.setTipo("LABORAL");
            }
            dias.add(dia);
        }

        diaCalendarioRepository.saveAll(dias);
        return true;
    }

    /**
     * Retorna el calendario completo con los días clasificados para un país y año.
     */
    public List<Map<String, Object>> obtenerCalendarioCompleto(Long paisId, int year) {
        LocalDate inicio = LocalDate.of(year, 1, 1);
        LocalDate fin = LocalDate.of(year, 12, 31);

        List<DiaCalendario> dias = diaCalendarioRepository
            .findByPaisIdAndFechaBetween(paisId, inicio, fin);

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (DiaCalendario d : dias) {
            Map<String, Object> dia = new LinkedHashMap<>();
            dia.put("id", d.getId());
            dia.put("fecha", d.getFecha().toString());
            dia.put("tipo", d.getTipo());
            dia.put("descripcion", obtenerDescripcionDia(d.getFecha()));
            resultado.add(dia);
        }
        return resultado;
    }

    // Auxiliar para obtener el nombre del día en español
    private String obtenerDescripcionDia(LocalDate fecha) {
        return fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
    }
}