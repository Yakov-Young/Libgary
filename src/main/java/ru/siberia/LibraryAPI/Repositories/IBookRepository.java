package ru.siberia.LibraryAPI.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.siberia.LibraryAPI.Entities.Book;

import java.util.List;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long> {

    Book findById(long id);

    List<Book> findByAvailable(boolean available);

    List<Book> findByTitle(String title);

    List<Book> findByAuthorId(long author);
}
