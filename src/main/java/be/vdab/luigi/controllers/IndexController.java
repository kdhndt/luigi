package be.vdab.luigi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
@RequestMapping("/")
//geef classes niet meer visibility dan nodig, public hoeft hier dus niet
class IndexController {
    @GetMapping
    public String index() {
        var morgenOfMiddag = LocalTime.now().getHour() < 12 ? "morgen" : "middag";
        return "<!doctype html><html><title>Hallo</title><body>Goede " + morgenOfMiddag +
                "</body></html>";
    }
}
