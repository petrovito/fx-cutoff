package ch.nordea.web;

/**
 * Cutoff type.
 * <p>
 *     This enum is used to define the cutoff type for a currency on a given date.
 *     The cutoff type can be NEVER, UNTIL or ALWAYS.
 *     NEVER means that the trade is not possible on the given date.
 *     UNTIL means that the trade is possible until a specified cutoff time.
 *     ALWAYS means that the trade is possible on the given date.
 * </p>
 */
public enum CutoffType {
    NEVER,
    UNTIL,
    ALWAYS,
}
