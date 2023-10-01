package ru.siberia.LibraryAPI.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.siberia.LibraryAPI.Entities.Author;
import ru.siberia.LibraryAPI.Repositories.IAuthorRepository;

import java.util.List;

@Service
public class AuthorService {

    private final IAuthorRepository repository;

    @Autowired
    public AuthorService(IAuthorRepository repository) {
        this.repository = repository;
    }

    public List<Author> getAll() {
        return repository.findAll();
    }

    public Author getByName(String name) { return  repository.findByName(name); }

    public Author save(Author author) { return repository.save(author); }
}
