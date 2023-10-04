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
import ru.siberia.LibraryAPI.DTOs.ChangeEmployeeDTO;
import ru.siberia.LibraryAPI.DTOs.RegisterEmployeeDTO;
import ru.siberia.LibraryAPI.DTOs.RegisterReaderDTO;
import ru.siberia.LibraryAPI.DTOs.WorkedStatusDTO;
import ru.siberia.LibraryAPI.Entities.Employee;
import ru.siberia.LibraryAPI.Entities.Enums.Roles;
import ru.siberia.LibraryAPI.JwtFilter.JwtFilter;
import ru.siberia.LibraryAPI.Services.EmployeeService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getAll(@CookieValue String jwt,
                               Model model) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            if (!Objects.equals(role, Roles.Director.toString())) {
                throw new InvalidTokenOrWrongRole();
            }

            model.addAttribute("employs", employeeService.getAll());

            return "showEmployee";
        } catch (InvalidTokenOrWrongRole e) {
            return "error";
        }
    }

    @PostMapping("/update")
    public RedirectView update(@ModelAttribute("user") @RequestBody ChangeEmployeeDTO newEmployee,
                               @RequestParam long id,
                               @CookieValue String jwt) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            if (!Objects.equals(role, Roles.Director.toString())) {
                throw new InvalidTokenOrWrongRole();
            }

            Employee employee = employeeService.getById(id);
            employee.setFirstName(newEmployee.getFirstName());
            employee.setLastName(newEmployee.getFirstName());
            employee.setSalary(newEmployee.getSalary());
            employee.setBirthday(newEmployee.getBirthday());

            employeeService.save(employee);


            return new RedirectView("/api/employee");
        } catch (InvalidTokenOrWrongRole e) {
            return new RedirectView("/error");
        }

    }

    @PostMapping("/dismiss")
    public ResponseEntity<?> dismissEmployee(@RequestParam long id,
                                    @RequestBody WorkedStatusDTO workedStatusDTO,
                                    @CookieValue String jwt) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            if (!Objects.equals(role, Roles.Director.toString())) {
                throw new InvalidTokenOrWrongRole();
            }

            Employee employee = employeeService.getById(id);
            System.out.println(workedStatusDTO.isWorked());
            employee.setWorked(workedStatusDTO.isWorked());
            employee.setEndDate(new Date());

            return ResponseEntity.ok(employeeService.save(employee));
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        }
    }

    @GetMapping("/getCreateHtml")
    public String getRegisterHtml(@CookieValue String jwt,
                              Model model) {
        try {

            String role = getRole(JwtFilter.getBody(jwt));

            if (!Objects.equals(role, Roles.Director.toString())) {
                throw new InvalidTokenOrWrongRole();
            }

            model.addAttribute("user", new RegisterEmployeeDTO());

            return "registerEmployee";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/getChangeHtml")
    public String getChangeHtml(@RequestParam long id,
                                @CookieValue String jwt,
                                Model model) {
        try {

            String role = getRole(JwtFilter.getBody(jwt));

            if (!Objects.equals(role, Roles.Director.toString())) {
                throw new InvalidTokenOrWrongRole();
            }

            model.addAttribute("user", employeeService.getById(id));

            return "changeEmployee";
        } catch (Exception e) {
            return "error";
        }
    }

    private String getRole(Claims claims) {
        return claims.get("role").toString();
    }

}
