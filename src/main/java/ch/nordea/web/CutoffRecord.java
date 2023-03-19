package ch.nordea.web;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CutoffRecord {

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
