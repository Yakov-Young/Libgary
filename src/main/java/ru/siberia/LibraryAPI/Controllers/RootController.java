package ru.siberia.LibraryAPI.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.siberia.LibraryAPI.DTOs.LoginDTO;

@Controller
@RequestMapping("/root")
public class RootController {

    @GetMapping
    public String root(Model model) {
        model.addAttribute("accessData", new LoginDTO());
        return "root";
    }

}
