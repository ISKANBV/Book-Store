package az.ingress.book_user_store.service;

import az.ingress.book_user_store.dto.AuthorDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface AuthorService {

    void save(AuthorDTO dto);

    List<AuthorDTO> findAll();

    AuthorDTO findById(Integer id);

    void delete(Integer id);
}
