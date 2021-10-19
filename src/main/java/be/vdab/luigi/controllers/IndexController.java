package be.vdab.luigi.controllers;

import be.vdab.luigi.domain.Adres;
import be.vdab.luigi.domain.Persoon;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalTime;

//@RestController
@Controller
@RequestMapping("/")
//geef classes niet meer visibility dan nodig, public hoeft hier dus niet
class IndexController {
    @GetMapping
    public ModelAndView index() {
        var morgenOfMiddag = LocalTime.now().getHour() < 12 ? "morgen" : "middag";
        var modelAndView = new ModelAndView("index", "moment", morgenOfMiddag);
        //addObject geeft extra data mee onder de naam zaakvoerder
        modelAndView.addObject("zaakvoerder",
                new Persoon("Luigi", "Peperone", 7, true,
                        LocalDate.of(1966,1,31),
                        new Adres("Grote Markt", "3", 9700, "Oudenaarde")));
        return modelAndView;
    }

/*    @GetMapping
    //Model staat voor data, View staat voor de Thymeleaf pagina
    //gebruik dit returntype voor methods die data doorgeven aan de Thymeleaf pagina
    public ModelAndView index() {
        var morgenOfMiddag = LocalTime.now().getHour() < 12 ? "morgen" : "middag";
        //naam van de pagina, naam van het stukje data, de data zelf die je doorgeeft
        return new ModelAndView("index", "moment", morgenOfMiddag);
    }*/

/*    @GetMapping
    public String index() {
        //verwijst naar index.html, klik op de method om te bevestigen
        return "index";
    }*/

/*
    @GetMapping
    public String index() {
                    var morgenOfMiddag = LocalTime.now().getHour() < 12 ? "morgen" : "middag";
        return "<!doctype html><html><title>Hallo</title><body>Goede " + morgenOfMiddag +
                "</body></html>";
    }
*/

}
