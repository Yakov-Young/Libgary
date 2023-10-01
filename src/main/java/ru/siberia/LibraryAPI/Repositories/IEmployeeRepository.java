package ru.siberia.LibraryAPI.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.siberia.LibraryAPI.Entities.Employee;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
    Employee getByAccessId(long accessId);

    Employee getById(long id);
}
