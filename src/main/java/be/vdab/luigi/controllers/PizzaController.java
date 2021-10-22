package be.vdab.luigi.controllers;

import be.vdab.luigi.domain.Pizza;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("pizzas")
public class PizzaController {
    //    private final String[] pizzas = {"Prosciutto", "Margherita", "Calzone"};
    private final Pizza[] pizzas = {
            new Pizza(1, "Prosciutto", BigDecimal.valueOf(4), true),
            new Pizza(2, "Margherita", BigDecimal.valueOf(5), false),
            new Pizza(3, "Calzone", BigDecimal.valueOf(4), false)
    };

    @GetMapping
    public ModelAndView pizzas() {
        return new ModelAndView("pizzas", "pizzas", pizzas)
                .addObject("getallen", List.of(3, 7))
                .addObject("landen", Map.of("B", "Belgie",
                        "NL", "Nederland"));
    }

    //pizzas/{id} verzoeken
    @GetMapping("{id}")
    //dit is de id uit de pagina die gerequest wordt (e.g. localhost:8080/pizzas/3), daarvoor dient @PathVariable
    public ModelAndView pizza(@PathVariable long id) {
        //we gebruiken de pagina pizza.html
        var modelAndView = new ModelAndView("pizza");
        //als we de pizza met de id uit de path variable vinden geven we die door aan de Thymeleaf pagina onder de naam pizza
        Arrays.stream(pizzas).filter(pizza -> pizza.getId() == id).findFirst().ifPresent(
                pizza -> modelAndView.addObject(pizza));
        return modelAndView;
    }

    private List<BigDecimal> uniekePrijzen() {
        return Arrays.stream(pizzas).map(Pizza::getPrijs).distinct().sorted().toList();
    }

    //geen "/" voor het pad!
    @GetMapping("prijzen")
    public ModelAndView prijzen() {
        return new ModelAndView("prijzen", "prijzen", uniekePrijzen());
    }

    private List<Pizza> pizzasMetPrijs(BigDecimal prijs) {
        return Arrays.stream(pizzas)
                .filter(pizza -> pizza.getPrijs().compareTo(prijs) == 0)
                .toList();
    }
    @GetMapping("/prijzen/{prijs}")
    public ModelAndView pizzasMetEenPrijs(@PathVariable BigDecimal prijs) {
        return new ModelAndView("prijzen", "pizzas", pizzasMetPrijs(prijs))
                .addObject("prijzen", uniekePrijzen());
    }
    //pushtest
}
