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

    private void validateCurrencies(String... isos) {
        for (String iso : isos) {
            if (iso == null || iso.isEmpty()) {
                throw new RuntimeException("currency is null or empty");
            }
            getCurrency(iso); //throws exception if currency not found
        }
    }

    @Cacheable("cutoffRecords")
    private CutoffTime getCutoffTimeForCurrency(String currency, LocalDate date) {
        return cutoffRepository.findByCurrency_IsoCodeAndDate(currency, date)
                .orElseThrow(() -> {
                    logger.error("no cutoff time found for {} on {}", currency, date);
                    return new RuntimeException("no cutoff time found for " + currency + " on " + date);
                }).toWeb().getCutoffTime();
    }

    @Cacheable("currencies")
    private Currency getCurrency(String currency) {
        return currencyRepository.findByIsoCode(currency)
                .orElseThrow(() -> {
                    logger.error("no cutoff time found for {}", currency);
                    return new RuntimeException("no cutoff time found for " + currency);
                }).toWeb();
    }


}
