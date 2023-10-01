package ru.siberia.LibraryAPI.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.siberia.LibraryAPI.DTOs.LoginDTO;
import ru.siberia.LibraryAPI.Entities.Access;
import ru.siberia.LibraryAPI.Repositories.IAccessRepository;

import java.net.http.HttpRequest;

@Service
public class AccessService {
    private final IAccessRepository repository;

    @Autowired
    public AccessService(IAccessRepository repository) {
        this.repository = repository;
    }

    public Access save(Access access) {
        return repository.save(access);
    }

    public long getIdByAccessDate(String login, String password) {
        Access response = repository.findByLoginAndPassword(login, password);

        if (checkLogin(response)) {
            return response.getId();
        }

        return -1;
    }

    public boolean checkLogin(Access response) {
        if (response != null) {
            return true;
        }

        return false;
    }
}
