package ch.nordea.config;

import ch.nordea.data.CurrencyD;
import ch.nordea.data.CutoffRecordD;
import ch.nordea.repository.CurrencyRepository;
import ch.nordea.repository.CutoffRepository;
import ch.nordea.web.CutoffTime;
import ch.nordea.web.CutoffType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Dumb Spring configuration class.
 * Populates the database with static currency and cutoff data.
 */
@Configuration
@ConditionalOnExpression("${ch.populate-db:false}")
public class PopulateDb implements InitializingBean {

    //TODO read from file
    String currencies = """
            AED|United Arab Emirates
            AUD|Australia
            BGN|Bulgaria
            CAD|Canada
            CHF|Switzerland
            CNH|China (Hong Kong)
            CZK|Czech Republic
            DKK|Denmark
            EUR|Euro Area
            GBP|United Kingdom
            HKD|Hong Kong
            HRK|Croatia
            HUF|Hungary
            ILS|Israel
            JPY|Japan
            MXN|Mexico
            NOK|Norway
            NZD|New Zealand
            PLN|Poland
            RON|Romania
            RUB|Russia
            RSD|Serbia Never pos
            SAR|Saudi Arabia
            SEK|Sweden
            SGD|Singapore
            THB|Thailand
            TRY|Turkey
            USD|United States
            ZAR|South Africa
            """;
    String cutoffData = """
            AED|Never|14:00|Always
            AUD|Never|14:00|Always
            BGN|Never|14:00|Always
            CAD|15:30|Always|Always
            CHF|10:00|Always|Always
            CNH|Never|14:00|Always
            CZK|11:00|Always|Always
            DKK|15:30|Always|Always
            EUR|16:00|Always|Always
            GBP|15:30|Always|Always
            HKD|Never|14:00|Always
            HRK|Never|14:00|Always
            HUF|11:00|Always|Always
            ILS|Never|14:00|Always
            JPY|Never|15:30|Always
            MXN|11:00|Always|Always
            NOK|15:00|Always|Always
            NZD|Never|14:00|Always
            PLN|10:00|Always|Always
            RON|Never|14:00|Always
            RUB|Never|13:00|Always
            RSD|Never|Never|Always
            SAR|Never|14:00|Always
            SEK|15:30|Always|Always
            SGD|Never|14:00|Always
            THB|Never|09:00|Always
            TRY|Never|14:00|Always
            USD|16:00|Always|Always
            ZAR|Never|14:00|Always
            """;

    @Value("${ch.populate-db:true}")
    boolean shouldPopulateDb;

    @Autowired
    CutoffRepository cutoffRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    
    @Override
    public void afterPropertiesSet() throws Exception {
        if (shouldPopulateDb) {
            if (cutoffRepository.count() == 0) {
                //populate currencies
                {
                    String[] lines = currencies.split("\n");
                    for (String line : lines) {
                        String[] parts = line.split("\\|");
                        String isoCode = parts[0];
                        String name = parts[1];
                        CurrencyD currencyD = new CurrencyD();
                        currencyD.setIsoCode(isoCode);
                        currencyD.setCountry(name);
                        currencyRepository.save(currencyD);
                    }
                }
                //populate cutoffs
                LocalDate today = LocalDate.now();
                String[] lines = cutoffData.split("\n");
                for (String line : lines) {
                    String[] parts = line.split("\\|");
                    String isoCode = parts[0];
                    CurrencyD currencyD = currencyRepository.findByIsoCode(isoCode).orElseThrow();
                    for (int i = 1; i < parts.length; i++) {
                        CutoffTime cutoffTime = parseCutoffTime(parts[i]);
                        LocalDate date = today.plusDays(i-1);
                        CutoffRecordD cutoffRecordD = new CutoffRecordD();
                        cutoffRecordD.setCurrency(currencyD);
                        cutoffRecordD.setDate(date);
                        cutoffRecordD.setCutoffType(cutoffTime.getCutoffType());
                        cutoffRecordD.setCutoffTime(cutoffTime.getCutoffTime());
                        cutoffRepository.save(cutoffRecordD);
                    }
                }
            }
        }
    }

    private CutoffTime parseCutoffTime(String time) {
        if (time.equals("Always")) {
            return CutoffTime.ALWAYS;
        } else if (time.equals("Never")) {
            return CutoffTime.NEVER;
        } else {
            return new CutoffTime(CutoffType.UNTIL, LocalTime.parse(time));
        }
    }

}
