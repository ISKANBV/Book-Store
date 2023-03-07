package az.ingress.book_user_store.service;

import az.ingress.book_user_store.dto.BookDTO;
import az.ingress.book_user_store.dto.BookSpecDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface BookService {

    void save(BookDTO dto);

    List<BookDTO> findAll();

    BookDTO findById(Integer id);

    void delete(Integer id);

    Page<BookDTO> findByFilter(BookSpecDTO bookSpecDTO, Pageable pageable);

    List<BookDTO> findAllByPublisher(Integer publisherId);

    void update(Integer id, BookDTO dto);
}
