package az.ingress.book_user_store.dto;

import az.ingress.book_user_store.domain.Role;
import az.ingress.book_user_store.domain.enumeration.RoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {

    private RoleName name;

    public RoleDTO(Role role) {
        this.name = role.getName();
    }

}
