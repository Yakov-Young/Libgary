package ru.siberia.LibraryAPI.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorPage {

    @GetMapping
    public String error() {
        return "error";
    }
}
