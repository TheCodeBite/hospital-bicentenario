package kevin.bicentenario.service.impl;

import kevin.bicentenario.constants.MensajesError;
import kevin.bicentenario.entity.CitaEntity;
import kevin.bicentenario.entity.ConsultorioEntity;
import kevin.bicentenario.entity.DoctorEntity;
import kevin.bicentenario.repository.CitaRepository;
import kevin.bicentenario.repository.ConsultorioRepository;
import kevin.bicentenario.repository.DoctorRepository;
import kevin.bicentenario.service.HospitalService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HospitalServiceImpl implements HospitalService {

    private DoctorRepository doctorRepository;

    private ConsultorioRepository consultorioRepository;

    private CitaRepository citaRepository;


    public HospitalServiceImpl(DoctorRepository doctorRepository, ConsultorioRepository consultorioRepository, CitaRepository citaRepository) {
        this.doctorRepository = doctorRepository;
        this.consultorioRepository = consultorioRepository;
        this.citaRepository = citaRepository;
    }

    @Override
    public List<DoctorEntity> obtenerDoctores() {
        return doctorRepository.findAll();
    }

    @Override
    public DoctorEntity crearDoctor(DoctorEntity doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException(MensajesError.VALOR_NULO);
        }
        return doctorRepository.save(doctor);
    }

    // MÉTODOS PARA CONSULTORIOS
    @Override
    public List<ConsultorioEntity> obtenerConsultorios() {
        return consultorioRepository.findAll();
    }

    @Override
    public ConsultorioEntity crearConsultorio(ConsultorioEntity consultorio) {
        if (consultorio == null) {
            throw new IllegalArgumentException(MensajesError.VALOR_NULO);
        }
        return consultorioRepository.save(consultorio);
    }

    @Override
    public CitaEntity crearCita(CitaEntity cita) {
        if (cita == null) {
            throw new IllegalArgumentException(MensajesError.VALOR_NULO);
        }

        if (cita.getDoctor() == null || cita.getConsultorio() == null) {
            throw new IllegalArgumentException("Doctor y Consultorio son obligatorios.");
        }

        Optional<DoctorEntity> doctorOpt = doctorRepository.findById(cita.getDoctor().getId());
        Optional<ConsultorioEntity> consultorioOpt = consultorioRepository.findById(cita.getConsultorio().getId());

        if (doctorOpt.isEmpty() || consultorioOpt.isEmpty()) {
            throw new IllegalArgumentException(MensajesError.RECURSO_NO_ENCONTRADO);
        }

        if (cita.getHorarioConsulta().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha y hora de la cita deben ser en el futuro.");
        }

        LocalDateTime inicioDia = cita.getHorarioConsulta().toLocalDate().atStartOfDay();
        LocalDateTime finDia = cita.getHorarioConsulta().toLocalDate().atTime(23, 59, 59);

        boolean pacienteTieneCita = citaRepository.existsByNombrePacienteAndHorarioConsultaBetween(
                cita.getNombrePaciente(),
                inicioDia,
                finDia
        );

        if (pacienteTieneCita) {
            List<CitaEntity> citasPaciente = citaRepository.findCitasByNombrePacienteAndHorarioConsultaBetween(
                    cita.getNombrePaciente(),
                    inicioDia,
                    finDia
            );

            for (CitaEntity citaPaciente : citasPaciente) {
                long diferenciaMinutos = Math.abs(Duration.between(citaPaciente.getHorarioConsulta(), cita.getHorarioConsulta()).toMinutes());
                if (diferenciaMinutos < 120) {
                    throw new IllegalArgumentException("El paciente ya tiene una cita en menos de 2 horas de diferencia.");
                }
            }
        }

        boolean doctorOcupado = citaRepository.existsByDoctorAndHorarioConsulta(
                doctorOpt.get(), cita.getHorarioConsulta()
        );
        if (doctorOcupado) {
            throw new IllegalArgumentException("El doctor ya tiene una cita programada en este horario.");
        }

        boolean consultorioOcupado = citaRepository.existsByConsultorioAndHorarioConsulta(
                consultorioOpt.get(), cita.getHorarioConsulta()
        );
        if (consultorioOcupado) {
            throw new IllegalArgumentException("El consultorio ya tiene una cita programada en este horario.");
        }

        long citasDoctor = citaRepository.countByDoctorAndHorarioConsultaBetween(
                doctorOpt.get(),
                inicioDia,
                finDia
        );
        if (citasDoctor >= 8) {
            throw new IllegalArgumentException("El doctor ya tiene el máximo de 8 citas para este día.");
        }

        // Guardar la cita si pasa todas las validaciones
        return citaRepository.save(cita);
    }


    @Override
    public List<CitaEntity> obtenerCitas() {
        return citaRepository.findAll();
    }
}
