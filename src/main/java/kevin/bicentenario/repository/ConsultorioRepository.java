package kevin.bicentenario.repository;

import kevin.bicentenario.entity.ConsultorioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultorioRepository extends JpaRepository<ConsultorioEntity, Long> {
}
