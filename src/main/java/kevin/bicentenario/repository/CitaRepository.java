package kevin.bicentenario.repository;

import kevin.bicentenario.entity.CitaEntity;
import kevin.bicentenario.entity.ConsultorioEntity;
import kevin.bicentenario.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<CitaEntity, Long> {
    @Query("SELECT COUNT(c) > 0 FROM CitaEntity c WHERE c.nombrePaciente = :nombrePaciente AND c.horarioConsulta BETWEEN :inicio AND :fin")
    boolean existsByNombrePacienteAndHorarioConsultaBetween(
            @Param("nombrePaciente") String nombrePaciente,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    @Query("SELECT c FROM CitaEntity c WHERE c.nombrePaciente = :nombrePaciente AND c.horarioConsulta BETWEEN :inicio AND :fin")
    List<CitaEntity> findCitasByNombrePacienteAndHorarioConsultaBetween(
            @Param("nombrePaciente") String nombrePaciente,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    @Query("SELECT COUNT(c) FROM CitaEntity c WHERE c.doctor = :doctor AND c.horarioConsulta BETWEEN :inicio AND :fin")
    long countByDoctorAndHorarioConsultaBetween(
            @Param("doctor") DoctorEntity doctor,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    @Query("SELECT COUNT(c) > 0 FROM CitaEntity c WHERE c.doctor = :doctor AND c.horarioConsulta = :horario")
    boolean existsByDoctorAndHorarioConsulta(
            @Param("doctor") DoctorEntity doctor,
            @Param("horario") LocalDateTime horario
    );

    @Query("SELECT COUNT(c) > 0 FROM CitaEntity c WHERE c.consultorio = :consultorio AND c.horarioConsulta = :horario")
    boolean existsByConsultorioAndHorarioConsulta(
            @Param("consultorio") ConsultorioEntity consultorio,
            @Param("horario") LocalDateTime horario
    );
}