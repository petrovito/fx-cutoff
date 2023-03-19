package ch.nordea.web;

import lombok.Data;

import java.time.LocalDate;

/**
 * Cutoff record web entity.
 * <p>
 * This entity is used to store the cutoff time for a currency on a given date.
 * </p>
 */
@Data
public class CutoffRecord {

    /**
     * The currency for which the cutoff time is defined.
     */
    private Currency currency;

    /**
     * The date for which the cutoff time is defined.
     */
    private LocalDate date;

    /**
     * The cutoff type for the currency on the given date, signalling whether the trade is
     * ALWAYS, UNTIL, NEVER possible. If the value is UNTIL, the `cutoffTime` is also set.
     */
    private CutoffTime cutoffTime;

}
