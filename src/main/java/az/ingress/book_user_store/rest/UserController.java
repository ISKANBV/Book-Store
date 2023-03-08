package az.ingress.book_user_store.rest;

import az.ingress.book_user_store.dto.UserDTO;
import az.ingress.book_user_store.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController extends GenericController<UserDTO> {

    private final UserService userService;

    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @PutMapping("/{id}/add_publisher_role")
    public ResponseEntity<Void> addPublisherRole(@PathVariable Integer id) {
        userService.addPublisherRole(id);
        return ResponseEntity.ok().build();
    }
}
