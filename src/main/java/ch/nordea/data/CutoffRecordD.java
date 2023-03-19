package ch.nordea.data;

import ch.nordea.web.CutoffRecord;
import ch.nordea.web.CutoffTime;
import ch.nordea.web.CutoffType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * CutoffRecord data entity.
 * <p>
 *     This entity is used to store the cutoff time for a currency for a given date.
 * </p>
 */
@Data
@Entity
public class CutoffRecordD {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    /**
     * The currency for which the cutoff time is defined.
     */
    @Column
    @ManyToOne(fetch = FetchType.EAGER)
    private CurrencyD currency;

    /**
     * The date for which the cutoff time is defined.
     */
    @Column
    private LocalDate date;

    /**
     * The cutoff type for the currency on the given date, signalling whether the trade is
     * ALWAYS, UNTIL, NEVER possible. If the value is UNTIL, the `cutoffTime` is also set.
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CutoffType cutoffType;

    /**
     * The cutoff time for the currency on the given date.
     */
    @Column
    private LocalTime cutoffTime;


    public CutoffRecord toWeb() {
        CutoffRecord cutoffRecord = new CutoffRecord();
        cutoffRecord.setCurrency(currency.toWeb());
        cutoffRecord.setDate(date);
        CutoffTime cutoff = new CutoffTime();
        cutoff.setCutoffType(cutoffType);
        cutoff.setCutoffTime(cutoffTime);
        cutoffRecord.setCutoffTime(cutoff);
        return cutoffRecord;
    }

}
