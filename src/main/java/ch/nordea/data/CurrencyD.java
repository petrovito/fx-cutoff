package ch.nordea.data;

import ch.nordea.web.Currency;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Currency data entity.
 * <p>
 * This entity is used to store basic currency information.
 * </p>
 */
@Data
@Entity
@Table(name = "currency", indexes = {
        @Index(name = "idx_currency_iso_code", columnList = "iso_code", unique = true)
})
public class CurrencyD {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    /**
     * The ISO code of the currency.
     */
    @Column(name = "iso_code")
    private String isoCode;

    /**
     * The country where the currency is being used.
     */
    @Column
    private String country;

    public Currency toWeb() {
        Currency currency = new Currency();
        currency.setIsoCode(isoCode);
        currency.setCountry(country);
        return currency;
    }
}
