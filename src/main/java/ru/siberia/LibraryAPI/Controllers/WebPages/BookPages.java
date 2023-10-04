package ru.siberia.LibraryAPI.Controllers.WebPages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.siberia.LibraryAPI.DTOs.CreateBookDTO;
import ru.siberia.LibraryAPI.Entities.Book;
import ru.siberia.LibraryAPI.Services.BookService;

@Controller
@RequestMapping("/book")
public class BookPages {

    private final BookService bookService;

    @Autowired
    public BookPages(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("new")
    public ModelAndView newBook(ModelMap model) {
        model.addAttribute("book", new CreateBookDTO());

        return new ModelAndView("NewBook", model);
    }

    @GetMapping("view")
    public ModelAndView viewBook(@RequestParam int id,
                                 ModelMap model) {


        model.addAttribute("book", bookService.getById(id));
        System.out.println(bookService.getById(id).getTitle());

        return new ModelAndView("viewBook", model);
    }

    @GetMapping("search")
    public ModelAndView searchBook(@RequestParam String title,
                                   ModelMap model) {


        model.addAttribute("books", bookService.getByTitle(title));
        model.addAttribute("title", title);
        System.out.println(bookService.getByTitle(title));

        return new ModelAndView("searchBook", model);
    }

}
