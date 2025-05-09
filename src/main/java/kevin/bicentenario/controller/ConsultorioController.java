package kevin.bicentenario.controller;

import kevin.bicentenario.entity.ConsultorioEntity;
import kevin.bicentenario.service.HospitalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultorios")
public class ConsultorioController {
    private final HospitalService hospitalService;

    public ConsultorioController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping
    public ResponseEntity<List<ConsultorioEntity>> listarConsultorios() {
        return ResponseEntity.ok(hospitalService.obtenerConsultorios());
    }

    @PostMapping
    public ResponseEntity<ConsultorioEntity> crearConsultorio(@RequestBody ConsultorioEntity consultorio) {
        return ResponseEntity.ok(hospitalService.crearConsultorio(consultorio));
    }
}
