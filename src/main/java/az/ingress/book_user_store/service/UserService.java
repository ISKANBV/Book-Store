package az.ingress.book_user_store.service;

import az.ingress.book_user_store.dto.UserDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void save(UserDTO dto);

    List<UserDTO> findAll();

    UserDTO findById(Integer id);

    void delete(Integer id);

    void addPublisherRole(Integer id);

    UserDTO findByUsername(String username);
}
