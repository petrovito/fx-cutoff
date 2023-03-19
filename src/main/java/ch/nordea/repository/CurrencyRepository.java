package ch.nordea.repository;

import ch.nordea.data.CurrencyD;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyRepository extends CrudRepository<CurrencyD, Long> {
}
