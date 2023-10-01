package ru.siberia.LibraryAPI.Controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.siberia.LibraryAPI.Controllers.Error.InvalidTokenOrWrongRole;
import ru.siberia.LibraryAPI.DTOs.WorkedStatusDTO;
import ru.siberia.LibraryAPI.Entities.Employee;
import ru.siberia.LibraryAPI.Entities.Enums.Roles;
import ru.siberia.LibraryAPI.JwtFilter.JwtFilter;
import ru.siberia.LibraryAPI.Services.EmployeeService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@CookieValue String jwt) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            if (!Objects.equals(role, Roles.Director.toString())) {
                throw new InvalidTokenOrWrongRole();
            }

            return ResponseEntity.ok(employeeService.getAll());
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        }

    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Employee newEmployee,
                                            @CookieValue String jwt) {
        try {
            String role = getRole(JwtFilter.getBody(jwt));

            if (!Objects.equals(role, Roles.Director.toString())) {
                throw new InvalidTokenOrWrongRole();
            }

            return ResponseEntity.ok(employeeService.save(newEmployee));
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        }

    }

    @PostMapping("/dismiss/{id}")
    public ResponseEntity<?> dismissEmployee(@PathVariable long id,
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
            employee.setEndDate(LocalDateTime.now());

            return ResponseEntity.ok(employeeService.save(employee));
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(
                    Map.of("description", "Invalid token or wrong role")
            );
        }
    }

    private String getRole(Claims claims) {
        return claims.get("role").toString();
    }

}
