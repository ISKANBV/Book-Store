package az.ingress.book_user_store.service;

import az.ingress.book_user_store.dto.AbstractDTO;
import java.util.List;

public interface GenericService<DTO extends AbstractDTO> {
    void save(DTO dto);

    List<DTO> findAll();

    DTO findById(Integer id);

    void delete(Integer id);
}
