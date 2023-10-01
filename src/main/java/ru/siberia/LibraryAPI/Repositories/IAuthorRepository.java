package ru.siberia.LibraryAPI.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.siberia.LibraryAPI.Entities.Author;

@Repository
public interface IAuthorRepository extends JpaRepository<Author, Long> {
    Author findByName(String name);
}
