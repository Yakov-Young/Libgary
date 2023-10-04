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
import ru.siberia.LibraryAPI.DTOs.BanDTO;
import ru.siberia.LibraryAPI.DTOs.ChangeReaderDTO;
import ru.siberia.LibraryAPI.DTOs.LoginDTO;
import ru.siberia.LibraryAPI.Entities.Enums.Roles;
import ru.siberia.LibraryAPI.Entities.Reader;
import ru.siberia.LibraryAPI.JwtFilter.JwtFilter;
import ru.siberia.LibraryAPI.Services.ReaderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("api/reader")
public class ReaderController {

    private final ReaderService readerService;

    @Autowired
    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    public ModelAndView getAll(ModelMap model,
                               @CookieValue String jwt) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            model.addAttribute("readers", readerService.getAll());

            return new ModelAndView("showReader", model);
        } catch (InvalidTokenOrWrongRole e) {
            return new ModelAndView("error");
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

    @GetMapping("/ban/{id}")
    public RedirectView ban(@CookieValue String jwt,
                                 @PathVariable int id) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            List<Roles> requiredRoles = new ArrayList<>();
            requiredRoles.add(Roles.Director);
            requiredRoles.add(Roles.Employee);

            if (checkRights(requiredRoles, role)) {
                throw new InvalidTokenOrWrongRole();
            }

            Reader uploadReader = readerService.getById(id);
            uploadReader.setBanned(!uploadReader.isBanned());

            readerService.save(uploadReader);

            return new RedirectView("/api/reader");
        } catch (InvalidTokenOrWrongRole e) {
            return new RedirectView("/error");
        }
    }

    @GetMapping("findByLastName")
    public ModelAndView getByLastName(@RequestParam String lastName,
                                     ModelMap model,
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

            model.addAttribute("readers", readerService.getByLastName(lastName));

            return new ModelAndView("searchReader", model);
        } catch (InvalidTokenOrWrongRole e) {
            return new ModelAndView("/error");
        }
    }

    @GetMapping("/profile")
    public ModelAndView getProfile(@CookieValue String jwt,
                                   ModelMap model) {
        try {
            int id = Integer.parseInt(JwtFilter.getBody(jwt).get("id").toString());

            model.addAttribute("user", readerService.getById(id));

            return new ModelAndView("profile", model);
        } catch (Exception e) {
            return new ModelAndView("/error");
        }
    }

    @PostMapping("/changeYourselfProfile")
    public RedirectView changeYourselfProfile(@ModelAttribute("dataProfile") @RequestBody ChangeReaderDTO body,
                                              @CookieValue String jwt) {
        try {
            int id = Integer.parseInt(JwtFilter.getBody(jwt).get("id").toString());

            Reader reader = readerService.getById(id);

            reader.setFirstName(body.getFirstName());
            reader.setLastName(body.getLastName());
            reader.setAddress(body.getAddress());

            readerService.save(reader);

            return new RedirectView("/api/reader/profile");
        } catch (Exception e) {
            return new RedirectView("/error");
        }
    }

    @GetMapping("/changeProfileHtml")
    public String changeProfileHtml(Model model,
                                    @CookieValue String jwt){
        int id = Integer.parseInt(JwtFilter.getBody(jwt).get("id").toString());

        model.addAttribute("dataProfile", readerService.getById(id));

        return "changeReaderProfile";
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
