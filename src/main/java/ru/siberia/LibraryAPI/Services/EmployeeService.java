package ru.siberia.LibraryAPI.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.siberia.LibraryAPI.Entities.Employee;
import ru.siberia.LibraryAPI.Repositories.IEmployeeRepository;

import java.util.List;

@Service
public class EmployeeService {
    private final IEmployeeRepository repository;

    @Autowired
    public EmployeeService(IEmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public Employee getByAccessId(long accessId) {
        return repository.getByAccessId(accessId);
    }

    public Employee save(Employee employee) { return repository.save(employee); }

    public Employee getById(long id) { return repository.getById(id); }
}
