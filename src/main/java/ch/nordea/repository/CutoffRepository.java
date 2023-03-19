package ch.nordea.repository;

import ch.nordea.data.CutoffRecordD;
import org.springframework.data.repository.CrudRepository;

public interface CutoffRepository extends CrudRepository<CutoffRecordD, Long> {
}
