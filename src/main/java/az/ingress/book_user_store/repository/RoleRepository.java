package az.ingress.book_user_store.repository;

import az.ingress.book_user_store.domain.Role;
import az.ingress.book_user_store.domain.enumeration.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(RoleName name);
}
