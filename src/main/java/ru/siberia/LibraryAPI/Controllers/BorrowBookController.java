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
import ru.siberia.LibraryAPI.Controllers.Error.AccessException;
import ru.siberia.LibraryAPI.Controllers.Error.InvalidTokenOrWrongRole;
import ru.siberia.LibraryAPI.DTOs.BookingDTO;
import ru.siberia.LibraryAPI.DTOs.ChangeStatusDTO;
import ru.siberia.LibraryAPI.DTOs.GiveDTO;
import ru.siberia.LibraryAPI.Entities.BorrowBook;
import ru.siberia.LibraryAPI.Entities.Enums.Roles;
import ru.siberia.LibraryAPI.Entities.Enums.Status;
import ru.siberia.LibraryAPI.JwtFilter.JwtFilter;
import ru.siberia.LibraryAPI.Services.BookService;
import ru.siberia.LibraryAPI.Services.BorrowBookService;
import ru.siberia.LibraryAPI.Services.ReaderService;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("api/borrowBook")
public class BorrowBookController {

    private final BorrowBookService borrowBookService;

    private final ReaderService readerService;

    private final BookService bookService;
    @Autowired
    public BorrowBookController(BorrowBookService borrowBookService,
                                BookService bookService,
                                ReaderService readerService) {
        this.borrowBookService = borrowBookService;
        this.readerService = readerService;
        this.bookService = bookService;
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

            return ResponseEntity.ok(borrowBookService.getAll());
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getAllByStatus(@PathVariable Status status,
                                         @CookieValue String jwt) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            return ResponseEntity.ok(borrowBookService.getByStatus(
                    status));
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        }
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<?> getById(@PathVariable long id,
                                            @CookieValue String jwt) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            return ResponseEntity.ok(borrowBookService.getByBookId(id));
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        }
    }

    @GetMapping("/reader/{id}")
    public ModelAndView getHistoryReader(@PathVariable long id,
                                         ModelMap model,
                                         @CookieValue String jwt) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            model.addAttribute("newStatus", new ChangeStatusDTO());
            model.addAttribute("history", borrowBookService.getByReaderId(id));

            return new ModelAndView("userHistoryForEmployee", model);
        } catch (InvalidTokenOrWrongRole e) {
            return new ModelAndView("error");
        }
    }

    @GetMapping("/getYourselfHistory")
    public ModelAndView getYourselfHistory(@CookieValue String jwt,
                                           ModelMap model) {
        try {
            int id = Integer.parseInt(JwtFilter.getBody(jwt).get("id").toString());

            model.addAttribute("history", borrowBookService.getByReaderId(id));
            return new ModelAndView("userHistory", model);
        } catch (Exception e) {
            return new ModelAndView("error");
        }
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(@RequestParam long id,
                                          @ModelAttribute("newStatus") @RequestBody ChangeStatusDTO body,
                                          @CookieValue String jwt) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            BorrowBook borrowBook = borrowBookService.getById(id);
            borrowBook.setStatus(body.getStatus());


            return ResponseEntity.ok(borrowBookService.save(borrowBook));
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        }
    }

    @PostMapping("/Booking/{id}")
    public ResponseEntity<?> booking(@PathVariable long id,
                                     @RequestBody BookingDTO body,
                                     @CookieValue String jwt) {
        try {
            int userId = Integer.parseInt(JwtFilter.getBody(jwt).getId());

            if (body.getStatus() != Status.Booked) {
                throw new AccessException();
            }

            BorrowBook newBorrowBook = new BorrowBook();
            newBorrowBook.setBook(bookService.getById(id));
            newBorrowBook.setReader(readerService.getById(userId));
            //newBorrowBook.setStartDate(body.getStartDate());
            //newBorrowBook.setEndDate(body.getEndDate());
            newBorrowBook.setStatus(body.getStatus());

            return ResponseEntity.ok(borrowBookService.save(newBorrowBook));
        } catch (AccessException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        }
    }

    @PostMapping("/give")
    public RedirectView giveBook(@RequestParam long id,
                                     @ModelAttribute("borrowBook") @RequestBody GiveDTO body,
                                     @CookieValue String jwt) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            BorrowBook newBorrowBook = new BorrowBook();
            newBorrowBook.setBook(bookService.getById(id));
            newBorrowBook.setReader(readerService.getByFullName(body.getFirstName(), body.getLastName()));
            newBorrowBook.setStartDate(new Date());
            newBorrowBook.setEndDate(new Date(0));
            newBorrowBook.setStatus(Status.Given);

            borrowBookService.save(newBorrowBook);

            return new RedirectView("/api/book");
        } catch (AccessException e) {
            return new RedirectView("/error");
        }
    }

    @GetMapping("/return")
    public RedirectView returnBook(@RequestParam long id,
                             @CookieValue String jwt) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            BorrowBook uploadBorrowBook = borrowBookService.getById(id);
            uploadBorrowBook.setStatus(Status.Returned);
            uploadBorrowBook.setEndDate(new Date());

            borrowBookService.save(uploadBorrowBook);

            return new RedirectView("/api/book");
        } catch (AccessException e) {
            return new RedirectView("/error");
        }
    }

    @GetMapping("/getGiveHtml")
    public String getGiveHtml(Model model,
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
            model.addAttribute("borrowBook", new GiveDTO());

            return "giveBook";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/getReturnHtml")
    public String getReturnHtml(Model model,
                                @CookieValue String jwt) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<Roles>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            model.addAttribute("history", borrowBookService.getByStatus(Status.Given));

            return "returnHtml";
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
