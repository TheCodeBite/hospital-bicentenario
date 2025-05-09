package kevin.bicentenario.controller;

import kevin.bicentenario.entity.CitaEntity;
import kevin.bicentenario.service.HospitalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {
    private final HospitalService hospitalService;

    public CitaController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping
    public ResponseEntity<List<CitaEntity>> listarCitas() {
        return ResponseEntity.ok(hospitalService.obtenerCitas());
    }

    @PostMapping
    public ResponseEntity<?> crearCita(@RequestBody CitaEntity cita) {
        try {
            CitaEntity nuevaCita = hospitalService.crearCita(cita);
            return ResponseEntity.ok(nuevaCita);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
