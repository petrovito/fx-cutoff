package ch.nordea.service;

import ch.nordea.web.CutoffTime;

import java.time.LocalDate;

/**
 * Provides the cutoff time for a given currency pair on a given date.
 */
public interface CutoffProvider {

    /**
     * Returns the cutoff time for the given currencies on the given date.
     * @param iso1 currency iso code
     * @param iso2 currency iso code
     * @param date date
     * @return cutoff time
     */
    CutoffTime getCutoffTime(String iso1, String iso2, LocalDate date);

}
