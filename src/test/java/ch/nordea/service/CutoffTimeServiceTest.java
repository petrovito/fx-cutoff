package ch.nordea.service;

import ch.nordea.data.CurrencyD;
import ch.nordea.data.CutoffRecordD;
import ch.nordea.repository.CurrencyRepository;
import ch.nordea.repository.CutoffRepository;
import ch.nordea.web.CutoffTime;
import ch.nordea.web.CutoffType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = {
        "ch.populate-db=false"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CutoffTimeServiceTest {

    @MockBean
    CurrencyRepository currencyRepository;

    @MockBean
    CutoffRepository cutoffRepository;

    @Autowired
    CutoffTimeService cutoffTimeService;

    @BeforeAll
    public void setup() {
        when(currencyRepository.findByIsoCode(anyString())).thenAnswer(invocation -> {
            String isoCode = invocation.getArgument(0);
            if (isoCode == null || isoCode.equals("NOT-REAL")) {
                return Optional.empty();
            }
            CurrencyD currencyD = new CurrencyD();
            currencyD.setIsoCode(isoCode);
            return Optional.of(currencyD);
        });

        when(cutoffRepository.findByCurrency_IsoCodeAndDate(anyString(), any())).thenAnswer(
                invocation -> {
                    String isoCode = invocation.getArgument(0);
                    LocalDate date = invocation.getArgument(1);
                    if (date.equals(LocalDate.of(2020, 1, 1))) {
                        switch (isoCode) {
                            case "EUR" -> {
                                CutoffRecordD cutoffRecordD = new CutoffRecordD();
                                cutoffRecordD.setCutoffTime(LocalTime.of(12, 0));
                                cutoffRecordD.setCutoffType(CutoffType.UNTIL);
                                cutoffRecordD.setCurrency(currencyRepository.findByIsoCode(isoCode).get());
                                return Optional.of(cutoffRecordD);
                            }
                            case "USD" -> {
                                CutoffRecordD cutoffRecordD = new CutoffRecordD();
                                cutoffRecordD.setCutoffTime(LocalTime.of(15, 0));
                                cutoffRecordD.setCutoffType(CutoffType.UNTIL);
                                cutoffRecordD.setCurrency(currencyRepository.findByIsoCode(isoCode).get());
                                return Optional.of(cutoffRecordD);
                            }
                        }
                    } else if (date.equals(LocalDate.of(2020, 1, 2))) {
                        switch (isoCode) {
                            case "EUR" -> {
                                CutoffRecordD cutoffRecordD = new CutoffRecordD();
                                cutoffRecordD.setCutoffType(CutoffType.ALWAYS);
                                cutoffRecordD.setCurrency(currencyRepository.findByIsoCode(isoCode).get());
                                return Optional.of(cutoffRecordD);
                            }
                            case "USD" -> {
                                CutoffRecordD cutoffRecordD = new CutoffRecordD();
                                cutoffRecordD.setCutoffTime(LocalTime.of(15, 0));
                                cutoffRecordD.setCutoffType(CutoffType.UNTIL);
                                cutoffRecordD.setCurrency(currencyRepository.findByIsoCode(isoCode).get());
                                return Optional.of(cutoffRecordD);
                            }
                        }
                    } else if (date.equals(LocalDate.of(2020, 1, 3))) {
                        switch (isoCode) {
                            case "EUR" -> {
                                CutoffRecordD cutoffRecordD = new CutoffRecordD();
                                cutoffRecordD.setCutoffType(CutoffType.ALWAYS);
                                cutoffRecordD.setCurrency(currencyRepository.findByIsoCode(isoCode).get());
                                return Optional.of(cutoffRecordD);
                            }
                            case "USD" -> {
                                CutoffRecordD cutoffRecordD = new CutoffRecordD();
                                cutoffRecordD.setCutoffType(CutoffType.NEVER);
                                cutoffRecordD.setCurrency(currencyRepository.findByIsoCode(isoCode).get());
                                return Optional.of(cutoffRecordD);
                            }
                        }
                    } else if (date.equals(LocalDate.of(2020, 1, 4))) {
                        switch (isoCode) {
                            case "EUR", "USD" -> {
                                CutoffRecordD cutoffRecordD = new CutoffRecordD();
                                cutoffRecordD.setCutoffType(CutoffType.ALWAYS);
                                cutoffRecordD.setCurrency(currencyRepository.findByIsoCode(isoCode).get());
                                return Optional.of(cutoffRecordD);
                            }
                        }
                    }
                    return Optional.empty();
                });
    }

    @Test
    void getCutoffTime() {
        {
            LocalDate date = LocalDate.of(2020, 1, 1);
            CutoffTime time = cutoffTimeService.getCutoffTime("EUR", "USD", date);
            assertEquals(new CutoffTime(CutoffType.UNTIL, LocalTime.of(12, 0)), time);
        }
        {
            LocalDate date = LocalDate.of(2020, 1, 2);
            CutoffTime time = cutoffTimeService.getCutoffTime("EUR", "USD", date);
            assertEquals(new CutoffTime(CutoffType.UNTIL, LocalTime.of(15, 0)), time);
        }
        {
            LocalDate date = LocalDate.of(2020, 1, 3);
            CutoffTime time = cutoffTimeService.getCutoffTime("EUR", "USD", date);
            assertEquals(new CutoffTime(CutoffType.NEVER, null), time);
        }
        {
            LocalDate date = LocalDate.of(2020, 1, 4);
            CutoffTime time = cutoffTimeService.getCutoffTime("EUR", "USD", date);
            assertEquals(new CutoffTime(CutoffType.ALWAYS, null), time);
        }

        //throw exception, if currency not found
        LocalDate date = LocalDate.of(2020, 1, 1);
        assertThrows(RuntimeException.class,
                () -> cutoffTimeService.getCutoffTime("NOT-REAL", "USD", date));

        //throw exception, if cutoff time not found for a currency for the given date
        LocalDate oldDate = LocalDate.of(2019, 1, 1);
        assertThrows(RuntimeException.class,
                () -> cutoffTimeService.getCutoffTime("EUR", "USD", oldDate));
    }
}