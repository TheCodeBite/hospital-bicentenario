package kevin.bicentenario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {
    @GetMapping ("/")
    public String mostrarVistaPrincipal(Model model) {
        model.addAttribute("title", "Hospital Bicentenario");
        return "index";
    }
}
