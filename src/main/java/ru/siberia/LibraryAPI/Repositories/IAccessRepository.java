package ru.siberia.LibraryAPI.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.siberia.LibraryAPI.Entities.Access;

@Repository
public interface IAccessRepository extends JpaRepository<Access, Long> {

    //Access save();
    Access findByLoginAndPassword(String login, String password);
}
