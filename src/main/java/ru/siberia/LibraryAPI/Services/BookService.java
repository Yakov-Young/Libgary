package ru.siberia.LibraryAPI.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.siberia.LibraryAPI.Entities.Book;
import ru.siberia.LibraryAPI.Repositories.IBookRepository;

import java.util.List;

@Service
public class BookService {
    private final IBookRepository repository;

    @Autowired
    public BookService(IBookRepository repository) {
        this.repository = repository;
    }

    public List<Book> getAll() {
        return repository.findAll();
    }

    public Book getById(long id) {
        return repository.findById(id);
    }

    public List<Book> getByAvailable(boolean available) {
        return repository.findByAvailable(available);
    }

    public Book save(Book book) {
        return repository.save(book);
    }
    public List<Book> getByTitle(String title) { return  repository.findByTitle(title); }
    public List<Book> getByAuthor(long id) { return  repository.findByAuthorId(id); }

}
