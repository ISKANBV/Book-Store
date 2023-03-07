package az.ingress.book_user_store.service.impl;

import az.ingress.book_user_store.domain.Author;
import az.ingress.book_user_store.dto.AuthorDTO;
import az.ingress.book_user_store.repository.AuthorRepository;
import az.ingress.book_user_store.service.AuthorService;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final ModelMapper mapper;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, ModelMapper mapper) {
        this.authorRepository = authorRepository;
        this.mapper = mapper;
    }

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
    public AuthorDTO findById(Integer id) {
        return mapper.map(authorRepository.findById(id).orElseThrow(), AuthorDTO.class);
    }

    @Override
    public void delete(Integer id) {
        authorRepository.deleteById(id);
    }
}
