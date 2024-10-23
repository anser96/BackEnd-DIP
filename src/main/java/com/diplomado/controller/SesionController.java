package com.diplomado.controller;

import com.diplomado.model.Invitado;
import com.diplomado.model.Miembro;
import com.diplomado.model.Sesion;
import com.diplomado.model.dto.SesionDTO;
import com.diplomado.service.SesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sesiones")
public class SesionController {

    @Autowired
    private SesionService sesionService;

    @PostMapping
    public Sesion crearSesion(@RequestBody Sesion sesion) {
        return sesionService.save(sesion);
    }

    @PostMapping("/{sesionId}/invitados")
    public void programarInvitados(@PathVariable int sesionId, @RequestBody List<Invitado> invitados) {
        sesionService.agregarInvitados(sesionId, invitados);
    }

    @PostMapping("/{sesionId}/miembros")
    public void citarMiembros(@PathVariable int sesionId, @RequestBody List<Miembro> miembros) {
        sesionService.citarMiembros(sesionId, miembros);
    }

    @PostMapping("/{sesionId}/definir-contenido")
    public SesionDTO definirContenido(@PathVariable int sesionId, @RequestBody Map<String, String> requestBody) {
        String contenido = requestBody.get("contenido");

        // Actualizar el contenido de la sesión
        Sesion sesion = sesionService.definirContenido(sesionId, contenido);

        // Convertir la entidad Sesion en DTO con todos los datos asociados
        return sesionService.convertToDTO(sesion);
    }

    @GetMapping
    public List<SesionDTO> getSesiones() {
        List<Sesion> sesiones = sesionService.findAll();

        // Convertir cada Sesion en un SesionDTO con toda la información relacionada
        return sesiones.stream()
                .map(sesion -> sesionService.convertToDTO(sesion))
                .collect(Collectors.toList());
    }

    @PostMapping("/{sesionId}/definir-fechas")
    public SesionDTO definirFechasSesion(@PathVariable int sesionId, @RequestBody Map<String, String> requestBody) {
        // Verificamos que los campos existan
        String fechaStr = requestBody.get("fecha");
        String horaInicioStr = requestBody.get("horaInicio");
        String horaFinalStr = requestBody.get("horaFinal");

        // Asegurarnos que los valores no sean null antes de intentar parsearlos
        if (fechaStr == null || horaInicioStr == null || horaFinalStr == null) {
            throw new IllegalArgumentException("Todos los campos (fecha, horaInicio, horaFinal) son obligatorios");
        }

        LocalDate fecha = LocalDate.parse(fechaStr);  // Parsear fecha
        LocalTime horaInicio = LocalTime.parse(horaInicioStr);  // Parsear hora de inicio
        LocalTime horaFinal = LocalTime.parse(horaFinalStr);  // Parsear hora de finalización

        Sesion sesionActualizada = sesionService.definirFechas(sesionId, fecha, horaInicio, horaFinal);

        // Devolvemos la sesión actualizada
        return SesionDTO.builder()
                .idSesion(sesionActualizada.getIdSesion())
                .lugar(sesionActualizada.getLugar())
                .fecha(sesionActualizada.getFecha())
                .horaInicio(sesionActualizada.getHoraInicio())
                .horaFinal(sesionActualizada.getHoraFinal())
                .contenido(sesionActualizada.getContenido())
                .build();
    }
}

