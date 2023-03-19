package ch.nordea.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalTime;

/**
 * Cutoff time web entity.
 * <p>
 * This entity is used to store the cutoff time for a currency on a given date.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CutoffTime implements Comparable<CutoffTime> {

    public static final CutoffTime ALWAYS = new CutoffTime(CutoffType.ALWAYS, null);
    public static final CutoffTime NEVER = new CutoffTime(CutoffType.NEVER, null);

    /**
     * Type of cutoff: NEVER, UNTIL, ALWAYS. If UNTIL then the cutoffTime is also set.
     */
    @NonNull
    private CutoffType cutoffType;

    /**
     * The cutoff time for the currency on the given date.
     */
    private LocalTime cutoffTime;


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CutoffTime that = (CutoffTime) o;
        if (cutoffType != that.cutoffType) return false;
        //if type is UNTIL compare the cutoff times, otherwise return equal
        if (cutoffType == CutoffType.UNTIL)
            return cutoffTime.equals(that.cutoffTime);
        return true;
    }


    @Override
    public int compareTo(CutoffTime o) {
        if (this.cutoffType == o.cutoffType) {
            //if both are of type UNTIL, compare the cutoff times, otherwise return equal
            if (this.cutoffType != CutoffType.UNTIL)
                return 0;
            return this.cutoffTime.compareTo(o.cutoffTime);
        }
        //cutoffTypes are different, return by the order: NEVER < UNTIL < ALWAYS
        switch (this.cutoffType) {
            case NEVER -> {
                return -1;
            }
            case UNTIL -> {
                if (o.cutoffType == CutoffType.NEVER)
                    return 1;
                else
                    return -1;
            }
            case ALWAYS -> {
                return 1;
            }
            default -> throw new IllegalStateException("Unexpected value: " + this.cutoffType);
        }
    }
}
