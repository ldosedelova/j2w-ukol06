package cz.czechitas.java2webapps.ukol6.controller;
import cz.czechitas.java2webapps.ukol6.entity.Vizitka;
import cz.czechitas.java2webapps.ukol6.repository.VizitkaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.Optional;

@Controller
public class VizitkaController {

    private final VizitkaRepository vizitkaRepository;

    public VizitkaController(VizitkaRepository vizitkaRepository) {
        this.vizitkaRepository = vizitkaRepository;
    }

    @InitBinder
    public void nullStringBinding(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/")
    public ModelAndView seznam() {
        ModelAndView result = new ModelAndView("seznam");
        result.addObject("seznam", vizitkaRepository.findAll());
        return result;
    }

    @GetMapping("/{id}")
    public Object detail(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("vizitka");
        Optional<Vizitka> vizitka = vizitkaRepository.findById(id);
        if (vizitka.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return modelAndView.addObject("vizitka", vizitka.get());
        }
    }

    @GetMapping("/nova")
    public ModelAndView nova() {
        ModelAndView modelAndView = new ModelAndView("formular");
        modelAndView.addObject("vizitka", new Vizitka());
        return modelAndView;
    }

    @PostMapping("/nova")
    public String pridat(@Valid Vizitka vizitka, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "formular";
        }
        vizitkaRepository.save(vizitka);
        return "redirect:/";
    }
}
