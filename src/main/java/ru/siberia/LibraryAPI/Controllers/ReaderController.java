package ru.siberia.LibraryAPI.Controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.siberia.LibraryAPI.Controllers.Error.InvalidTokenOrWrongRole;
import ru.siberia.LibraryAPI.DTOs.BanDTO;
import ru.siberia.LibraryAPI.DTOs.ChangeReaderDTO;
import ru.siberia.LibraryAPI.Entities.Enums.Roles;
import ru.siberia.LibraryAPI.Entities.Reader;
import ru.siberia.LibraryAPI.JwtFilter.JwtFilter;
import ru.siberia.LibraryAPI.Services.ReaderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("api/reader")
public class ReaderController {

    private final ReaderService readerService;

    @Autowired
    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
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

            return ResponseEntity.ok(readerService.getAll());
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> getById(@CookieValue String jwt,
                                     @PathVariable int id) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            return ResponseEntity.ok(readerService.getById(id));
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        }
    }

    @PostMapping("/ban/{id}")
    public ResponseEntity<?> ban(@CookieValue String jwt,
                                 @PathVariable int id,
                                 @RequestBody BanDTO body) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            Reader uploadReader = readerService.getById(id);
            uploadReader.setBanned(body.isBanned());

            return ResponseEntity.ok(readerService.save(uploadReader));
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        }
    }

    @GetMapping("/{lastName}")
    public ResponseEntity<?> getByLastName(@PathVariable String lastName,
                                     @CookieValue String jwt
                                     ) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            return ResponseEntity.ok(readerService.getByLastName(lastName));
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        }
    }

    @PostMapping("/changeYourselfProfile")
    public ResponseEntity<?> changeYourselfProfile(@RequestBody ChangeReaderDTO body,
                                                   @CookieValue String jwt) {
        try {
            int id = Integer.parseInt(JwtFilter.getBody(jwt).getId());

            Reader reader = readerService.getById(id);

            reader.setFirstName(body.getFirstName());
            reader.setLastName(body.getLastName());
            reader.setBirthday(body.getBirthday());
            reader.setAddress(body.getAddress());

            return ResponseEntity.ok(readerService.save(reader));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid data or token")
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
