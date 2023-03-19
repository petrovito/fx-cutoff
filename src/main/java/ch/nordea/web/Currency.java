package ch.nordea.web;


import lombok.Data;

/**
 * Currency web entity.
 * <p>
 * This entity is used to store basic currency information.
 * </p>
 */
@Data
public class Currency {

    /**
     * The ISO code of the currency.
     */
    private String isoCode;

    /**
     * The country where the currency is being used.
     */
    private String country;

}
