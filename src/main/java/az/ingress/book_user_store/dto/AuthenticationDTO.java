package az.ingress.book_user_store.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticationDTO {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
