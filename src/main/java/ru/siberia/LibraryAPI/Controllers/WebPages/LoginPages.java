package ru.siberia.LibraryAPI.Controllers.WebPages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.siberia.LibraryAPI.DTOs.CreateBookDTO;
import ru.siberia.LibraryAPI.DTOs.LoginDTO;

@Controller
@RequestMapping("/login")
public class LoginPages {

    @GetMapping
    public String newBook(Model model) {
        model.addAttribute("accessData", new LoginDTO());

        return "Login";
    }
}
