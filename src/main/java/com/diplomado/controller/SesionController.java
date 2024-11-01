package com.diplomado.controller;

import com.diplomado.model.ApiResponse;
import com.diplomado.model.Invitado;
import com.diplomado.model.Miembro;
import com.diplomado.model.Sesion;
import com.diplomado.model.dto.SesionDTO;
import com.diplomado.service.SesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sesiones")
public class SesionController {

    @Autowired
    private SesionService sesionService;

    @PostMapping
    public ResponseEntity<ApiResponse<SesionDTO>> crearSesion(@RequestBody SesionDTO sesionDTO) {
        Sesion nuevaSesion = Sesion.builder()
                .lugar(sesionDTO.getLugar())
                .fecha(sesionDTO.getFecha())
                .horaInicio(sesionDTO.getHoraInicio())
                .horaFinal(sesionDTO.getHoraFinal())
                .build();

        Sesion sesionCreada = sesionService.save(nuevaSesion);
        SesionDTO respuesta = SesionDTO.builder()
                .idSesion(sesionCreada.getIdSesion())
                .lugar(sesionCreada.getLugar())
                .fecha(sesionCreada.getFecha())
                .horaInicio(sesionCreada.getHoraInicio())
                .horaFinal(sesionCreada.getHoraFinal())
                .build();

        return ResponseEntity.ok(new ApiResponse<>("success", "Sesión creada con éxito", respuesta));
    }

    @PostMapping("/{sesionId}/invitados")
    public ResponseEntity<ApiResponse<Void>> programarInvitados(@PathVariable int sesionId, @RequestBody List<Invitado> invitados) {
        sesionService.agregarInvitados(sesionId, invitados);
        return ResponseEntity.ok(new ApiResponse<>("success", "Invitados agregados con éxito", null));
    }

    @PostMapping("/{sesionId}/miembros")
    public ResponseEntity<ApiResponse<Void>> citarMiembros(@PathVariable int sesionId, @RequestBody List<Miembro> miembros) {
        sesionService.citarMiembros(sesionId, miembros);
        return ResponseEntity.ok(new ApiResponse<>("success", "Miembros citados con éxito", null));
    }

    @PostMapping("/{sesionId}/definir-contenido")
    public ResponseEntity<ApiResponse<SesionDTO>> definirContenido(@PathVariable int sesionId, @RequestBody Map<String, String> requestBody) {
        String contenido = requestBody.get("contenido");
        Sesion sesion = sesionService.definirContenido(sesionId, contenido);
        SesionDTO sesionDTO = sesionService.convertToDTO(sesion);

        return ResponseEntity.ok(new ApiResponse<>("success", "Contenido definido con éxito", sesionDTO));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SesionDTO>>> getSesiones() {
        List<Sesion> sesiones = sesionService.findAll();
        List<SesionDTO> sesionesDTO = sesiones.stream()
                .map(sesionService::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>("success", "Sesiones obtenidas con éxito", sesionesDTO));
    }

    @PostMapping("/{sesionId}/definir-fechas")
    public ResponseEntity<ApiResponse<SesionDTO>> definirFechasSesion(@PathVariable int sesionId, @RequestBody Map<String, String> requestBody) {
        String fechaStr = requestBody.get("fecha");
        String horaInicioStr = requestBody.get("horaInicio");
        String horaFinalStr = requestBody.get("horaFinal");

        if (fechaStr == null || horaInicioStr == null || horaFinalStr == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", "Todos los campos (fecha, horaInicio, horaFinal) son obligatorios", null));
        }

        LocalDate fecha = LocalDate.parse(fechaStr);
        LocalTime horaInicio = LocalTime.parse(horaInicioStr);
        LocalTime horaFinal = LocalTime.parse(horaFinalStr);

        Sesion sesionActualizada = sesionService.definirFechas(sesionId, fecha, horaInicio, horaFinal);
        SesionDTO sesionDTO = SesionDTO.builder()
                .idSesion(sesionActualizada.getIdSesion())
                .lugar(sesionActualizada.getLugar())
                .fecha(sesionActualizada.getFecha())
                .horaInicio(sesionActualizada.getHoraInicio())
                .horaFinal(sesionActualizada.getHoraFinal())
                .contenido(sesionActualizada.getContenido())
                .build();

        return ResponseEntity.ok(new ApiResponse<>("success", "Fechas definidas con éxito", sesionDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SesionDTO>> getSesionById(@PathVariable int id) {
        SesionDTO sesion = sesionService.getSesionById(id);
        if (sesion != null) {
            return ResponseEntity.ok(new ApiResponse<>("success", "Sesión obtenida con éxito", sesion));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>("error", "Sesión no encontrada", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SesionDTO>> updateSesion(@PathVariable int id, @RequestBody SesionDTO sesionDTO) {
        SesionDTO updatedSesion = sesionService.updateSesion(id, sesionDTO);
        if (updatedSesion != null) {
            return ResponseEntity.ok(new ApiResponse<>("success", "Sesión actualizada con éxito", updatedSesion));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>("error", "Sesión no encontrada", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSesion(@PathVariable int id) {
        sesionService.deleteSesion(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Sesión eliminada con éxito", null));
    }
}
