package az.ingress.book_user_store.dto;

import az.ingress.book_user_store.domain.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends AbstractDTO {

    private Integer id;

    @NotEmpty
    @Size(min = 1, max = 50)
    private String firstName;

    @NotEmpty
    @Size(min = 1, max = 50)
    private String lastName;

    @NotEmpty
    @Size(min = 1, max = 50)
    private String username;

    @NotEmpty
    @Size(min = 1, max = 50)
    @Email
    private String email;

    @NotEmpty
    @Size(min = 6, max = 100)
    private String password;

    private Set<RoleDTO> roles;

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream().map(RoleDTO::new).collect(Collectors.toSet());
    }

}
