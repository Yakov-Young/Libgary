package ru.siberia.LibraryAPI.Controllers.WebPages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.siberia.LibraryAPI.DTOs.CreateBookDTO;

@Controller
@RequestMapping("/book")
public class BookPages {

    @GetMapping("new")
    public String newBook(Model model) {
        model.addAttribute("book", new CreateBookDTO());

        return "NewBook";
    }

}
