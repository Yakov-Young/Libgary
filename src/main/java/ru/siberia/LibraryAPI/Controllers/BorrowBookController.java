package ru.siberia.LibraryAPI.Controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.siberia.LibraryAPI.Controllers.Error.AccessException;
import ru.siberia.LibraryAPI.Controllers.Error.InvalidTokenOrWrongRole;
import ru.siberia.LibraryAPI.DTOs.BookingDTO;
import ru.siberia.LibraryAPI.DTOs.ChangeStatusDTO;
import ru.siberia.LibraryAPI.Entities.BorrowBook;
import ru.siberia.LibraryAPI.Entities.Enums.Roles;
import ru.siberia.LibraryAPI.Entities.Enums.Status;
import ru.siberia.LibraryAPI.JwtFilter.JwtFilter;
import ru.siberia.LibraryAPI.Services.BookService;
import ru.siberia.LibraryAPI.Services.BorrowBookService;
import ru.siberia.LibraryAPI.Services.ReaderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
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
    public ResponseEntity<?> getAllByStatus(@PathVariable long id,
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
    public ResponseEntity<?> getHistoryReader(@PathVariable long id,
                                              @CookieValue String jwt) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            return ResponseEntity.ok(borrowBookService.getByReaderId(id));
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        }
    }

    @GetMapping("/getYourselfHistory")
    public ResponseEntity<?> getYourselfHistory(@CookieValue String jwt) {
        try {
            int id = Integer.parseInt(JwtFilter.getBody(jwt).getId());

            return ResponseEntity.ok(borrowBookService.getByReaderId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token")
            );
        }
    }

    @PostMapping("/changeStatus/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable long id,
                                          @RequestBody ChangeStatusDTO body,
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
    public ResponseEntity<?> Booking(@PathVariable long id,
                                     @RequestBody BookingDTO body,
                                     @CookieValue String jwt) {
        try {
            int userId = Integer.parseInt(JwtFilter.getBody(jwt).getId());

            if (body.getStatus() != Status.Available
                    & body.getStatus() != Status.Booked) {
                throw new AccessException();
            }

            BorrowBook newBorrowBook = new BorrowBook();
            newBorrowBook.setBook(bookService.getById(id));
            newBorrowBook.setReader(readerService.getById(userId));
            newBorrowBook.setStartDate(body.getStartDate());
            newBorrowBook.setEndDate(body.getEndDate());
            newBorrowBook.setStatus(body.getStatus());

            return ResponseEntity.ok(borrowBookService.save(newBorrowBook));
        } catch (AccessException e) {
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
