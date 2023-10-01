package ru.siberia.LibraryAPI.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.siberia.LibraryAPI.Entities.Reader;
import ru.siberia.LibraryAPI.Repositories.IReaderRepository;

import java.util.List;

@Service
public class ReaderService {
    private final IReaderRepository repository;

    @Autowired
    public ReaderService(IReaderRepository repository) {
        this.repository = repository;
    }

    public List<Reader> getAll() {
        return repository.findAll();
    }

    public Reader getByAccessId(long accessId) {
        return repository.findByAccessId(accessId);
    }

    public Reader save(Reader reader) { return repository.save(reader); }

    public Reader getById(long id) { return  repository.findById(id);}

    public List<Reader> getByLastName(String lastName) { return repository.findByLastName(lastName); }
}
