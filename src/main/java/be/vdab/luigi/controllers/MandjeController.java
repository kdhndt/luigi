package be.vdab.luigi.controllers;

import be.vdab.luigi.services.PizzaService;
import be.vdab.luigi.sessions.Mandje;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("mandje")
public class MandjeController {
    private final PizzaService pizzaService;
    private final Mandje mandje;

    //mandje wordt aangemaakt d.m.v. een default constructor met een uniek SerialVersionUID voor de sessie van een gebruiker? (@SessionScope)
    public MandjeController(PizzaService pizzaService, Mandje mandje) {
        this.pizzaService = pizzaService;
        this.mandje = mandje;
    }

    @PostMapping("{id}")
    public String voegToe(@PathVariable long id) {
        mandje.voegToe(id);
        return "redirect:/mandje";
    }

    @GetMapping()
    public ModelAndView toonMandje() {
        return new ModelAndView("mandje", "pizzas", pizzaService.findByIds(mandje.getIds()));
    }

}
