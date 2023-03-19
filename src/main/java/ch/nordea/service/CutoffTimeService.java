package ch.nordea.service;

import ch.nordea.repository.CurrencyRepository;
import ch.nordea.repository.CutoffRepository;
import ch.nordea.web.Currency;
import ch.nordea.web.CutoffTime;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service for calculating cutoff times, based on cutoff records in database.
 * Also, caching the records queried from the database.
 */
@Service
public class CutoffTimeService implements CutoffProvider {

    Logger logger = org.slf4j.LoggerFactory.getLogger(CutoffTimeService.class);

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    CutoffRepository cutoffRepository;


    @Override
    public CutoffTime getCutoffTime(String iso1, String iso2, LocalDate date) {
        validateCurrencies(iso1, iso2);
        CutoffTime cutoff1 = getCutoffTimeForCurrency(iso1, date);
        CutoffTime cutoff2 = getCutoffTimeForCurrency(iso2, date);
        return ObjectUtils.min(cutoff1, cutoff2);
    }

    /**
     * Validates the given currency ISO codes.
     * @throws IllegalArgumentException if any of the following is true:
     *    * input is null/empty
     *    * currency not found in database
     */
    private void validateCurrencies(String... isos) {
        for (String iso : isos) {
            if (iso == null || iso.isEmpty()) {
                throw new IllegalArgumentException("currency is null or empty");
            }
            getCurrency(iso); //throws exception if currency not found
        }
    }

    /**
     * Returns the cutoff time for the given currency on the given date.
     * Also caches the result.
     * @throws IllegalArgumentException if no cutoff time found for the given currency on the given date
     * @param currency currency ISO code
     * @param date date
     * @return cutoff time web object
     */
    @Cacheable("cutoffRecords")
    private CutoffTime getCutoffTimeForCurrency(String currency, LocalDate date) {
        return cutoffRepository.findByCurrency_IsoCodeAndDate(currency, date)
                .orElseThrow(() -> {
                    logger.error("no cutoff time found for {} on {}", currency, date);
                    return new IllegalArgumentException("no cutoff time found for " + currency + " on " + date);
                }).toWeb().getCutoffTime();
    }

    /**
     * Returns the currency object for the given currency ISO code.
     * Also caches the result.
     * @throws RuntimeException if no currency found for the given ISO code
     * @param currency currency ISO code
     * @return currency web object
     */
    @Cacheable("currencies")
    private Currency getCurrency(String currency) {
        return currencyRepository.findByIsoCode(currency)
                .orElseThrow(() -> {
                    logger.error("no cutoff time found for {}", currency);
                    return new IllegalArgumentException("no cutoff time found for " + currency);
                }).toWeb();
    }


}
