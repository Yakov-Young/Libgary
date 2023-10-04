package ru.siberia.LibraryAPI.Controllers.WebPages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import ru.siberia.LibraryAPI.Services.ReaderService;

@Controller
@RequestMapping("/reader")
public class ReaderPages {

    private final ReaderService readerService;
    @Autowired

    public ReaderPages(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    public ModelAndView changeProfileHtml(){
        return new ModelAndView("changeReaderProfile");
    }

}
