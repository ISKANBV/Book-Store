package az.ingress.book_user_store.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;

@Getter
@ResponseStatus(code = HttpStatus.CONFLICT, value = HttpStatus.CONFLICT)
public class ForeignKeyException extends AbstractThrowableProblem {
    private final Long id;

    public ForeignKeyException(Long id) {
        this.id = id;
    }
}
