package be.vdab.luigi.controllers;

import be.vdab.luigi.domain.Pizza;
import be.vdab.luigi.exceptions.KoersClientException;
import be.vdab.luigi.forms.VanTotPrijsForm;
import be.vdab.luigi.services.EuroService;
import be.vdab.luigi.services.PizzaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;

@Controller
@RequestMapping("pizzas")
public class PizzaController {

/*    private final Pizza[] pizzas = {
            new Pizza(1, "Prosciutto", BigDecimal.valueOf(4), true),
            new Pizza(2, "Margherita", BigDecimal.valueOf(5), false),
            new Pizza(3, "Calzone", BigDecimal.valueOf(4), false)
    };*/

    private final EuroService euroService;
    private final PizzaService pizzaService;

    //maak een constructor zodat Spring bij het opstarten v/d website de membervariabele kan invullen
    //in dit geval wordt een object v/d DefaultEuroService class die de EuroService interface implementeert als bean gebruikt,
    //aangezien we daar de @Service annotation gebruiken
    //klik op icoon links om de Dependency link te zien
    public PizzaController(EuroService euroService, PizzaService pizzaService) {
        this.euroService = euroService;
        this.pizzaService = pizzaService;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    public ModelAndView pizzas() {
        return new ModelAndView("pizzas", "pizzas", pizzaService.findAll())
                /*.addObject("getallen", List.of(3, 7))
                .addObject("landen", Map.of("B", "Belgie",
                        "NL", "Nederland"))*/;
    }

    //pizzas/{id} verzoeken
    @GetMapping("{id}")
    //dit is de id uit de pagina die gerequest wordt (e.g. localhost:8080/pizzas/3), daarvoor dient @PathVariable
    public ModelAndView pizza(@PathVariable long id) {
        //we gebruiken de pagina pizza.html
        var modelAndView = new ModelAndView("pizza");
        //als we de pizza met de id uit de path variable vinden geven we die door aan de Thymeleaf pagina onder de naam pizza
/*
        Arrays.stream(pizzas).filter(pizza -> pizza.getId() == id).findFirst().ifPresent(
*/
        pizzaService.findById(id).ifPresent(
        pizza -> {
                    //gewijzigde code
                    modelAndView.addObject(pizza);
                    try {
                        modelAndView.addObject("inDollar", euroService.naarDollar(pizza.getPrijs()));
                    } catch (KoersClientException ex) {
                        ///TBD
                        logger.error("Kan dollar koers niet lezen", ex);
                    }
                }
        );
        return modelAndView;
    }

/*    private List<BigDecimal> uniekePrijzen() {
        return Arrays.stream(pizzas).map(Pizza::getPrijs).distinct().sorted().toList();
    }*/

    //geen "/" voor het pad!
    @GetMapping("prijzen")
    public ModelAndView prijzen() {
        //service(s) oproepen ipv method
        return new ModelAndView("prijzen", "prijzen", pizzaService.findUniekePrijzen());
    }

/*    private List<Pizza> pizzasMetPrijs(BigDecimal prijs) {
        return Arrays.stream(pizzas)
                .filter(pizza -> pizza.getPrijs().compareTo(prijs) == 0)
                .toList();
    }*/

    //hier ook niet
    @GetMapping("prijzen/{prijs}")
    //@PathVariable geeft de {prijs} gratis mee als toegankelijke informatie naar je Thymeleaf
    //dit dus naast hetgeen die je telkens in je ModelAndView meegeeft
    public ModelAndView pizzasMetEenPrijs(@PathVariable BigDecimal prijs) {
        //service(s) oproepen ipv method
        return new ModelAndView("prijzen", "pizzas", pizzaService.findByPrijs(prijs))
                .addObject("prijzen", pizzaService.findUniekePrijzen());
    }

    @GetMapping("aantalpizzasperprijs")
    public ModelAndView aantalPizzasPerPrijs() {
        return new ModelAndView("aantalpizzasperprijs", "aantalPizzasPerPrijs", pizzaService.findAantalPizzasPerPrijs());
    }

    @GetMapping("vantotprijs/form")
    public ModelAndView vanTotPrijsForm() {
        return new ModelAndView("vantotprijs")
                //modelName wordt automatisch "vanTotPrijsForm", gebaseerd op de naam van je datatype
                .addObject(new VanTotPrijsForm(null, null));
    }

    @GetMapping("vantotprijs")
    //Spring gebruikt de constructor van VanTotPrijsForm en vult de van en tot membervariabelen daarvan in met
    //de van en tot parameter uit je query string (e.g. .../pizzas/vantotpijrs?van=4&tot=5
    public ModelAndView vanTotPrijs(@Valid VanTotPrijsForm form, Errors errors) {
        var modelAndView = new ModelAndView("vantotprijs");
        //Spring geeft hasErrors als true terug als het form object niet kon gemaakt worden
        if (errors.hasErrors()) {
            //zoek geen pizzas maar toon de pagina opnieuw
            return modelAndView;
        }
        return modelAndView.addObject("pizzas",
                //we halen de waarden uit ons VanTotPrijsForm object en gebruiken die in de method
                pizzaService.findByPrijsBetween(form.van(), form.tot()));
    }

    @GetMapping("toevoegen/form")
    public ModelAndView toevoegenForm() {
        return new ModelAndView("toevoegen")
                .addObject(new Pizza(0, "", null, false));
    }

    //Er komt niets na @PostMapping, d.w.z. dat we hier POST requests verwerken naar de URL bij RequestMappings (pizzas)
    @PostMapping
    public String toevoegen(@Valid Pizza pizza, Errors errors, RedirectAttributes redirect) {
        if (errors.hasErrors()) {
//            return new ModelAndView("toevoegen");
            return "toevoegen";
        }
//        pizzaService.create(pizza);
        redirect.addAttribute("idNieuwePizza", pizzaService.create(pizza));
//        return new ModelAndView("pizzas", "pizzas", pizzaService.findAll());
        return "redirect:/pizzas";
    }

}
