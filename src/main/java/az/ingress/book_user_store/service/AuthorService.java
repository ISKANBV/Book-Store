package az.ingress.book_user_store.service;

import az.ingress.book_user_store.domain.Author;
import az.ingress.book_user_store.dto.AuthorDTO;
import az.ingress.book_user_store.repository.AuthorRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthorService implements GenericService<AuthorDTO> {
    private final AuthorRepository authorRepository;
    private final ModelMapper mapper;

    @Override
    public void save(AuthorDTO dto) {
        Author author = new Author();
        author.setName(dto.getName());
        authorRepository.save(author);
    }

    @Override
    public List<AuthorDTO> findAll() {
        return mapper.map(authorRepository.findAll(), new TypeToken<List<AuthorDTO>>() {
        }.getType());
    }

    @Override
    public AuthorDTO findById(Long id) {
        return mapper.map(authorRepository.findById(id).orElseThrow(), AuthorDTO.class);
    }

    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
