package kevin.bicentenario.service;

import kevin.bicentenario.entity.CitaEntity;
import kevin.bicentenario.entity.ConsultorioEntity;
import kevin.bicentenario.entity.DoctorEntity;
import kevin.bicentenario.repository.CitaRepository;

import java.util.List;

public interface HospitalService {
    List<DoctorEntity> obtenerDoctores();
    DoctorEntity crearDoctor(DoctorEntity doctor);

    List<ConsultorioEntity> obtenerConsultorios();
    ConsultorioEntity crearConsultorio(ConsultorioEntity consultorio);

    CitaEntity crearCita(CitaEntity cita);
    List<CitaEntity> obtenerCitas();
}
