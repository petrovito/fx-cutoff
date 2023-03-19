package ch.nordea.repository;

import ch.nordea.data.CurrencyD;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CurrencyRepository extends CrudRepository<CurrencyD, Long> {

    Optional<CurrencyD> findByIsoCode(String isoCode);

}
