package ru.siberia.LibraryAPI.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.siberia.LibraryAPI.Entities.Reader;

import java.util.List;

@Repository
public interface IReaderRepository extends JpaRepository<Reader, Long> {
    Reader findByAccessId(long accessId);

    Reader findById(long id);

    List<Reader> findByLastName(String lastName);

    Reader findByFirstNameAndLastName(String firstName, String lastName);
}
