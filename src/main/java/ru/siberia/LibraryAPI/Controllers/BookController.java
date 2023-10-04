package ru.siberia.LibraryAPI.Controllers;


import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.siberia.LibraryAPI.Controllers.Error.InvalidTokenOrWrongRole;
import ru.siberia.LibraryAPI.DTOs.CreateBookDTO;
import ru.siberia.LibraryAPI.DTOs.DeleteBookDTO;
import ru.siberia.LibraryAPI.Entities.Author;
import ru.siberia.LibraryAPI.Entities.Book;
import ru.siberia.LibraryAPI.Entities.Enums.Roles;
import ru.siberia.LibraryAPI.JwtFilter.JwtFilter;
import ru.siberia.LibraryAPI.Services.AuthorService;
import ru.siberia.LibraryAPI.Services.BookService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("api/book")
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    @Autowired
    public BookController(BookService bookService,
                          AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping
    public ModelAndView getAll(@CookieValue String jwt,
                               ModelMap model) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }
            model.addAttribute("books", bookService.getAll());

            if (Objects.equals(role, Roles.Employee.toString()))
                return new ModelAndView("mainEmployee", model);
            else
                return new ModelAndView("mainDirector", model);
        } catch (InvalidTokenOrWrongRole e) {
            return new ModelAndView("error");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id,
                                     @CookieValue String jwt,
                                     Model model) {
        try {
            JwtFilter.getBody(jwt);

            return ResponseEntity.ok(bookService.getById(id));
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Wrong role")
            );
        }
    }

    @GetMapping("/deleteBook")
    public RedirectView deleteBook(@RequestParam long id,
                                        @CookieValue String jwt) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            Book book = bookService.getById(id);
            book.setAvailable(!book.isAvailable());
            bookService.save(book);

            return new RedirectView("/api/book");
        } catch (InvalidTokenOrWrongRole e) {
            return new RedirectView("/error");
        }
    }

    @PostMapping("/create")
    public RedirectView create(@ModelAttribute("book") @RequestBody CreateBookDTO bookDTO,
                               @CookieValue String jwt) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            Book newBook = new Book();
            newBook.setTitle(bookDTO.getTitle());
            newBook.setCategory(bookDTO.getCategory());
            newBook.setDescription(bookDTO.getDescription());
            newBook.setIsbn(bookDTO.getIsbn());
            newBook.setPageCount(bookDTO.getPageCount());
            newBook.setAvailable(true);

            Author author = authorService.getByName(bookDTO.getAuthor());
            newBook.setAuthor(author);
            bookService.save(newBook);

            return new RedirectView("/api/book");
        } catch (Exception e) {
            return new RedirectView("/error");
        }

    }

    @GetMapping("/available/{available}")
    public ModelAndView availableBook(@PathVariable boolean available,
                                                    ModelMap model,
                                                    @CookieValue String jwt) {
        try {
            var tmp = JwtFilter.getBody(jwt);
            String role = tmp.get("role").toString();

            model.addAttribute("books", bookService.getByAvailable(available));

            if (Objects.equals(role, Roles.Reader.toString())) {

                return new ModelAndView("mainReader", model);
            } else {
                return new ModelAndView("mainEmployee", model);
            }

        } catch (Exception e) {
            return new ModelAndView("error");
        }
    }

    @PostMapping("/upload")
    public RedirectView upload(@RequestParam long id,
                                    @CookieValue String jwt,
                                    @ModelAttribute("book") @RequestBody CreateBookDTO body) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<Roles>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            Book uploadBook = bookService.getById(id);
            uploadBook.setTitle(body.getTitle());
            uploadBook.setCategory(body.getCategory());
            uploadBook.setDescription(body.getDescription());
            uploadBook.setIsbn(body.getIsbn());
            uploadBook.setPageCount(body.getPageCount());
            uploadBook.setAvailable(true);

            Author author = authorService.getByName(body.getAuthor());

            uploadBook.setAuthor(author);

            bookService.save(uploadBook);

            return new RedirectView("/api/book");

        } catch (Exception e) {
            return new RedirectView("/error");
        }
    }

    @GetMapping("/searchByTitle")
    public ModelAndView getByTitle(@RequestParam String title,
                                         ModelMap model,
                                         @CookieValue String jwt) {
        try {
            getRole(JwtFilter.getBody(jwt));

            model.addAttribute("books", bookService.getByTitle(title));
            return new ModelAndView("searchBook", model);
        } catch (InvalidTokenOrWrongRole e) {
            return new ModelAndView("error");
        }
    }

    @GetMapping("/getCreateHtml")
    public String getCreateHtml(Model model,
                          @CookieValue String jwt) {

        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<Roles>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            model.addAttribute("book", new Book());

            return "addBook";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/getChangeHtml")
    public String getChangeHtml(Model model,
                                @RequestParam long id,
                                @CookieValue String jwt) {

        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<Roles>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            model.addAttribute("book", bookService.getById(id));

            return "changeBook";
        } catch (Exception e) {
            return "error";
        }
    }


    private boolean checkRights(List<Roles> requiredRoles, String role) {
        for (Roles requiredRole : requiredRoles) {
            boolean tmp = Objects.equals(requiredRole.toString(), role);

            if (tmp) {
                return false;
            }
        }
        return true;
    }

    private String getRole(Claims claims) {
        return claims.get("role").toString();
    }
}
