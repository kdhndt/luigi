package be.vdab.luigi.controllers;

import be.vdab.luigi.sessions.Identificatie;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
class MyControllerAdvice {
    private final Identificatie identificatie;

    public MyControllerAdvice(Identificatie identificatie) {
        this.identificatie = identificatie;
    }
    //Je Model uit je ModelAndView stelt een stuk data voor
    //Deze annotation zorgt ervoor dat data wordt doorgegeven aan de Thymeleaf (HTML) pagina
    //Vanaf nu wordt bij elke request deze method opgeroepen
    @ModelAttribute
    void extraDataToevoegenAanModel(Model model) {
        model.addAttribute(identificatie);
    }
}