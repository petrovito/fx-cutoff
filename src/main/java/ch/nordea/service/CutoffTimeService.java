package ch.nordea.service;

import ch.nordea.repository.CurrencyRepository;
import ch.nordea.repository.CutoffRepository;
import ch.nordea.web.CutoffTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CutoffTimeService implements CutoffProvider {

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    CutoffRepository cutoffRepository;


    @Override
    public CutoffTime getCutoffTime(String currency1, String currency2, LocalDate date) {
        throw new RuntimeException("not implemented");
    }

}
