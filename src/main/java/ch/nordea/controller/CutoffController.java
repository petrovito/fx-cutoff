package ch.nordea.controller;

import ch.nordea.service.CutoffProvider;
import ch.nordea.service.CutoffTimeService;
import ch.nordea.web.CutoffTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/cutoff")
public class CutoffController {

    Logger logger = org.slf4j.LoggerFactory.getLogger(CutoffController.class);

    @Autowired
    private CutoffProvider cutoffProvider;

    /**
     * Returns the cutoff time for the given currencies on the given date.
     * @param iso1 currency iso code
     * @param iso2 currency iso code
     * @param date date
     * @return cutoff time
     */
    @RequestMapping(value = "/time", method = GET)
    public CutoffTime getCutoffTime(
            @RequestParam(value = "iso1") String iso1,
            @RequestParam(value = "iso2") String iso2,
            @RequestParam(value = "date") LocalDate date
    ) {
        logger.debug("Requesting cutoff time for currencies {} and {} on date {}", iso1, iso2, date);
        return cutoffProvider.getCutoffTime(iso1, iso2, date);
    }

}
