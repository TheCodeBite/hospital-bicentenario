package kevin.bicentenario.controller;

import kevin.bicentenario.entity.DoctorEntity;
import kevin.bicentenario.service.HospitalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctores")
public class DoctorController {
    private final HospitalService hospitalService;

    public DoctorController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping
    public ResponseEntity<List<DoctorEntity>> listarDoctores() {
        return ResponseEntity.ok(hospitalService.obtenerDoctores());
    }

    @PostMapping
    public ResponseEntity<DoctorEntity> crearDoctor(@RequestBody DoctorEntity doctor) {
        return ResponseEntity.ok(hospitalService.crearDoctor(doctor));
    }
}
