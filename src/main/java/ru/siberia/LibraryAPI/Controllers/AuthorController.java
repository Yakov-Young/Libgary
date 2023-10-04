package ru.siberia.LibraryAPI.Controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.ReaderEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.siberia.LibraryAPI.Controllers.Error.InvalidTokenOrWrongRole;
import ru.siberia.LibraryAPI.Entities.Author;
import ru.siberia.LibraryAPI.Entities.Enums.Roles;
import ru.siberia.LibraryAPI.JwtFilter.JwtFilter;
import ru.siberia.LibraryAPI.Services.AuthorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/api/author")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAll() {
        return ResponseEntity.ok(authorService.getAll());
    }

    @PostMapping("/create")
    public RedirectView create(@ModelAttribute("author") @RequestBody Author body,
                               @CookieValue String jwt) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<Roles>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (!checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            authorService.save(body);

            return new RedirectView("/api/book/available/true");
        } catch (Exception e) {
            return new RedirectView("/error");
        }

    }

    @GetMapping("/getHtml")
    public String getHtml(Model model,
                          @CookieValue String jwt) {

        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<Roles>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (!checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            model.addAttribute("author", new Author());

            return "addAuthor";
        } catch (Exception e) {
            return "error";
        }

    }

    private boolean checkRights(List<Roles> requiredRoles, String role) {
        for (Roles requiredRole : requiredRoles) {
            boolean tmp = Objects.equals(requiredRole.toString(), role);

            if (tmp) {
                return true;
            }
        }
        return false;
    }

    private String getRole(Claims claims) {
        return claims.get("role").toString();
    }
}
