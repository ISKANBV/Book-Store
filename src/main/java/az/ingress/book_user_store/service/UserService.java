package az.ingress.book_user_store.service;

import az.ingress.book_user_store.domain.Role;
import az.ingress.book_user_store.domain.User;
import az.ingress.book_user_store.domain.enumeration.RoleName;
import az.ingress.book_user_store.dto.UserDTO;
import az.ingress.book_user_store.repository.RoleRepository;
import az.ingress.book_user_store.repository.UserRepository;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements GenericService<UserDTO> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Override
    public void save(UserDTO userDTO) {
        User user = mapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRoles(new HashSet<>(Collections.singleton(roleRepository.findByName(RoleName.USER))));
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(Long id) {
        return userRepository.findById(id).map(UserDTO::new).orElseThrow();
    }

    public void addPublisherRole(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.getRoles().add(roleRepository.findByName(RoleName.PUBLISHER));
        userRepository.save(user);
    }

    public UserDTO findByUsername(String username) {
        return mapper.map(userRepository.findByUsername(username).orElseThrow(), UserDTO.class);
    }

    @Override
    public void delete(Long id) {
        userRepository.findById(id).ifPresentOrElse(user -> {
            if (user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()).contains(RoleName.ADMIN))
                throw new AccessDeniedException("Admin can't be deleted");

            userRepository.deleteById(id);
        }, () -> {
            throw new NoSuchElementException();
        });
    }
}

