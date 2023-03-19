package az.ingress.book_user_store.rest;

import az.ingress.book_user_store.dto.AbstractDTO;
import az.ingress.book_user_store.errors.ForeignKeyException;
import az.ingress.book_user_store.service.GenericService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public abstract class GenericController<DTO extends AbstractDTO> {

    private final GenericService<DTO> genericService;

    @GetMapping
    public ResponseEntity<?> findAll()
    {
        return new ResponseEntity<>(genericService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id)
    {
        return new ResponseEntity<>(genericService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody DTO dto)
    {
        genericService.save(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id)
    {
        try {
            genericService.delete(id);
        } catch (DataIntegrityViolationException exp) {
            throw new ForeignKeyException(id);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
