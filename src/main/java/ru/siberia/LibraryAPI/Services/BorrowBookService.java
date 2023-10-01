package ru.siberia.LibraryAPI.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.siberia.LibraryAPI.Entities.BorrowBook;
import ru.siberia.LibraryAPI.Entities.Enums.Status;
import ru.siberia.LibraryAPI.Repositories.IBorrowBookRepository;

import java.util.List;

@Service
public class BorrowBookService {
    private final IBorrowBookRepository repository;

    @Autowired
    public BorrowBookService(IBorrowBookRepository repository) {
        this.repository = repository;
    }

    public List<BorrowBook> getAll() {
        return repository.findAll();
    }

    public BorrowBook getById(long id) { return repository.findById(id); }

    public BorrowBook save(BorrowBook borrowBook) { return repository.save(borrowBook); }
    public List<BorrowBook> getByStatus(Status status) { return repository.findByStatus((status)); }

    public List<BorrowBook> getByBookId(long id) { return repository.findByBookId(id); }

    public List<BorrowBook> getByReaderId(long id) { return repository.findByReaderId(id); }
}
