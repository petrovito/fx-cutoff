package ch.nordea.service;

import ch.nordea.web.CutoffTime;

import java.time.LocalDate;

public interface CutoffProvider {

    CutoffTime getCutoffTime(String currency1, String currency2, LocalDate date);

}
