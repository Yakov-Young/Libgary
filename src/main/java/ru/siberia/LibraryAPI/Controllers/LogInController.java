package ru.siberia.LibraryAPI.Controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.siberia.LibraryAPI.Controllers.Error.AccessException;
import ru.siberia.LibraryAPI.DTOs.LoginDTO;
import ru.siberia.LibraryAPI.Entities.Employee;
import ru.siberia.LibraryAPI.Entities.Enums.Roles;
import ru.siberia.LibraryAPI.Entities.IUser;
import ru.siberia.LibraryAPI.Entities.Reader;
import ru.siberia.LibraryAPI.SecurePassword.HashPassword;
import ru.siberia.LibraryAPI.Services.AccessService;
import ru.siberia.LibraryAPI.Services.EmployeeService;
import ru.siberia.LibraryAPI.Services.ReaderService;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class LogInController {
    private final AccessService accessService;
    private final ReaderService readerService;
    private final EmployeeService employeeService;

    private static final String JWT_KEY = "CGuZtKUBu3JIvXOCwWIyJYSS4cP+TNiDIDdvhr6aqnpQ45y3nw0qC9WY4cJPiHXcKKKlILZhpJI8hX5MTRn9QQ==";

    @Autowired
    public LogInController(AccessService accessService,
                           ReaderService readerService,
                           EmployeeService employeeService) {
        this.accessService = accessService;
        this.readerService = readerService;
        this.employeeService = employeeService;
    }

    @PostMapping("/login")
    public RedirectView login(@ModelAttribute("accessData") @RequestBody LoginDTO body,
                              HttpServletResponse response) {

        try {
            HashPassword hashPassword = new HashPassword();
            String password = hashPassword.getHashPassword(body.getPassword());

            long id = accessService.getIdByAccessDate(body.getLogin(), password);

            IUser user = searchUser(id);

            if (user == null)
                throw new AccessException();

            Roles role;

            if (user.getClass() == Reader.class) {
                if (((Reader) user).isBanned()) {
                    throw new AccessException();
                }
                role = Roles.Reader;
            } else {
                if (!((Employee) user).isWorked()) {
                    throw new AccessException();
                }
                role = ((Employee) user).getRole();
            }

            String issuer = String.valueOf(user.getId());
            SecretKey key = Keys.hmacShaKeyFor(
                    Decoders.BASE64.decode(JWT_KEY)
            );

            String jwt = Jwts.builder()
                    .setIssuer(issuer)
                    .setExpiration(new Date(System.currentTimeMillis() * 60 * 20 * 1000))
                    .claim("role", role)
                    .claim("id", user.getId())
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();

            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setHttpOnly(true);

            response.addCookie(cookie);
            if (role == Roles.Reader)
                return new RedirectView("/api/book/available/true");
            else
                return new RedirectView("/api/book");
        } catch (AccessException e) {
            return new RedirectView("/error");
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.status(202).body("");
    }

    public IUser searchUser(long id) {
        if (id >= 0) {
            Employee employee = employeeService.getByAccessId(id);

            if (employee == null) {

                return readerService.getByAccessId(id);
            }

            return employee;
        }

        return null;
    }

}
