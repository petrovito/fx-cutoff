package ch.nordea.controller;

import ch.nordea.web.CutoffTime;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/cutoff")
public class CutoffController {



    @RequestMapping(value = "/time", method = GET)
    public CutoffTime getCutoffTime(
            @RequestParam(value = "currency1") String currency1,
            @RequestParam(value = "currency2") String currency2,
            @RequestParam(value = "date") LocalDate date
    ) {
        throw new RuntimeException("not implemented");
    }

}
