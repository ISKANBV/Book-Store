package az.ingress.book_user_store.rest;

import az.ingress.book_user_store.dto.AuthorDTO;
import az.ingress.book_user_store.service.AuthorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authors")
public class AuthorController extends GenericController<AuthorDTO> {
    public AuthorController(AuthorService authorService) {
        super(authorService);
    }
}
