package ru.siberia.LibraryAPI.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.siberia.LibraryAPI.Entities.BorrowBook;
import ru.siberia.LibraryAPI.Entities.Enums.Status;

import java.util.List;

@Repository
public interface IBorrowBookRepository extends JpaRepository<BorrowBook, Long> {
    List<BorrowBook> findByStatus(Status status);

    List<BorrowBook> findByBookId(long id);

    List<BorrowBook> findByReaderId(long id);

    BorrowBook findById(long id);
}
