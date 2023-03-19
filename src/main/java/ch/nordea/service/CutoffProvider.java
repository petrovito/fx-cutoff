package ch.nordea.service;

import ch.nordea.web.CutoffTime;

import java.time.LocalDate;

public interface CutoffProvider {

    CutoffTime getCutoffTime(String iso1, String iso2, LocalDate date);

}
