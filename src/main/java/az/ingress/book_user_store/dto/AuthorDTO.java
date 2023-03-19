package az.ingress.book_user_store.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorDTO extends AbstractDTO {
    private Long id;

    @NotEmpty
    @Size(min = 1, max = 100)
    private String name;
}
