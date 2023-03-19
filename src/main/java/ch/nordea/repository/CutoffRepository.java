package ch.nordea.repository;

import ch.nordea.data.CutoffRecordD;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CutoffRepository extends CrudRepository<CutoffRecordD, Long> {

    Optional<CutoffRecordD> findByCurrency_IsoCodeAndDate(String iso, LocalDate date);

}
