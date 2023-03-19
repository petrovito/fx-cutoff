package ch.nordea.web;

import lombok.Data;

import java.time.LocalTime;

@Data
public class CutoffTime implements Comparable<CutoffTime> {

    /**
     * Type of cutoff: NEVER, UNTIL, ALWAYS. If UNTIL then the cutoffTime is also set.
     */
    private CutoffType cutoffType;

    /**
     * The cutoff time for the currency on the given date.
     */
    private LocalTime cutoffTime;


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
