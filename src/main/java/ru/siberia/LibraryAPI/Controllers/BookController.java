package ru.siberia.LibraryAPI.Controllers;


import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

@RestController
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
    public ResponseEntity<?> getAll(@CookieValue String jwt) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            return ResponseEntity.ok(bookService.getAll());
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        }
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable int id,
                                     @CookieValue String jwt,
                                     Model model) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            model.addAttribute("user", ResponseEntity.ok(bookService.getById(id)));
            //return ResponseEntity.ok(bookService.getById(id));
            return "GetAllBook";
        } catch (InvalidTokenOrWrongRole e) {
            /*return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );*/
            return ")";
        }
    }

    @PostMapping("/deleteBook/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable long id,
                                        @CookieValue String jwt,
                                        @RequestBody DeleteBookDTO deleteBookDTO) throws Exception {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            Book book = bookService.getById(id);
            book.setAvailable(deleteBookDTO.isAvailable());

            return ResponseEntity.ok(bookService.save(book));
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@ModelAttribute @RequestBody CreateBookDTO bookDTO,
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
            newBook.setAvailable(bookDTO.isAvailable());

            Author author = authorService.getByName(bookDTO.getAuthor());

            newBook.setAuthor(author);

            return ResponseEntity.ok(bookService.save(newBook));
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid data")
            );
        }

    }

    @GetMapping("/available/{available}")
    public ResponseEntity<?> availableBook(@PathVariable boolean available,
                                                    @CookieValue String jwt) {
        try {
            getRole(JwtFilter.getBody(jwt));

            return ResponseEntity.ok(bookService.getByAvailable(available));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid data")
            );
        }
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<?> upload(@PathVariable int id,
                                    @CookieValue String jwt,
                                    @RequestBody CreateBookDTO body) {
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
            uploadBook.setAvailable(body.isAvailable());

            Author author = authorService.getByName(body.getAuthor());

            uploadBook.setAuthor(author);

            return ResponseEntity.ok(bookService.save(uploadBook));
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid data")
            );
        }
    }

    @GetMapping("/searchByName/{authorName}")
    public ResponseEntity<?> getByAuthor(@PathVariable String authorName,
                                         @CookieValue String jwt) {
        try {
            getRole(JwtFilter.getBody(jwt));

            long authorId = authorService.getByName(authorName).getId();

            return ResponseEntity.ok(bookService.getByAuthor(authorId));
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        }
    }

    @GetMapping("/searchByTitle/{title}")
    public ResponseEntity<?> getByTitle(@PathVariable String title,
                                         @CookieValue String jwt) {
        try {
            getRole(JwtFilter.getBody(jwt));

            System.out.println(title);

            return ResponseEntity.ok(bookService.getByTitle(title));
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
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
