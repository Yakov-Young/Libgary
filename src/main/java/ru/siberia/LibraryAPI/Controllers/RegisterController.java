package ru.siberia.LibraryAPI.Controllers;

import io.jsonwebtoken.Claims;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.siberia.LibraryAPI.Controllers.Error.InvalidTokenOrWrongRole;
import ru.siberia.LibraryAPI.DTOs.RegisterEmployeeDTO;
import ru.siberia.LibraryAPI.DTOs.RegisterReaderDTO;
import ru.siberia.LibraryAPI.Entities.Access;
import ru.siberia.LibraryAPI.Entities.Employee;
import ru.siberia.LibraryAPI.Entities.Enums.Roles;
import ru.siberia.LibraryAPI.Entities.Reader;
import ru.siberia.LibraryAPI.JwtFilter.JwtFilter;
import ru.siberia.LibraryAPI.SecurePassword.HashPassword;
import ru.siberia.LibraryAPI.Services.AccessService;
import ru.siberia.LibraryAPI.Services.EmployeeService;
import ru.siberia.LibraryAPI.Services.ReaderService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/api/register")
public class RegisterController {

    private final ReaderService readerService;
    private final AccessService accessService;
    private final EmployeeService employeeService;

    @Autowired
    public RegisterController(ReaderService readerService, AccessService accessService, EmployeeService employeeService) {
        this.readerService = readerService;
        this.accessService = accessService;
        this.employeeService = employeeService;
    }

    @PostMapping("/reader")
    public ResponseEntity<?> registerReader(@NotNull @RequestBody RegisterReaderDTO dto) {

        Access newAccess = createAccess(dto.getLogin(), dto.getPassword());

        Reader newReader = new Reader();

        try {
            newReader.setFirstName(dto.getFirstName());
            newReader.setLastName(dto.getLastName());
            newReader.setAddress(dto.getAddress());
            newReader.setBanned(false);
            newReader.setBirthday(dto.getBirthday());
            newReader.setAccess(newAccess);

            accessService.save(newAccess);
            readerService.save(newReader);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid data");
        }

        return ResponseEntity.status(201).body("Ok");
    }

    @PostMapping("/employee")
    public ResponseEntity<?> registerEmployee(@NotNull @RequestBody RegisterEmployeeDTO dto,
                                              @CookieValue String jwt) {
        try {
            Claims jwtBody = JwtFilter.getBody(jwt);

            String role = (String) jwtBody.get("role");

            System.out.println(Roles.Director.toString());

            if (!Objects.equals(role, Roles.Director.toString())) {
                throw new InvalidTokenOrWrongRole();
            }

            Access newAccess = createAccess(dto.getLogin(), dto.getPassword());

            Employee newEmployee = new Employee();


            newEmployee.setFirstName(dto.getFirstName());
            newEmployee.setLastName(dto.getLastName());
            newEmployee.setStartDate(LocalDateTime.now());
            newEmployee.setSalary(dto.getSalary());
            newEmployee.setRole(dto.getRole());
            newEmployee.setWorked(true);
            newEmployee.setAccess(newAccess);
            if (dto.getBirthday() != null) {
                newEmployee.setBirthday(dto.getBirthday());
            }

            accessService.save(newAccess);
            employeeService.save(newEmployee);
        } catch (InvalidTokenOrWrongRole e) {
            return ResponseEntity.badRequest().body(Map.of("description", "Invalid token or wrong role"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid data");
        }
        return ResponseEntity.status(201).body("Ok");
    }

    @GetMapping("/test")
    public String test() {
        return "index";
    }


    private Access createAccess(String login, String password) {
        HashPassword hashPassword = new HashPassword();

        return new Access(
                login,
                hashPassword.getHashPassword(password)
        );
    }
}
