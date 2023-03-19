package az.ingress.book_user_store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import az.ingress.book_user_store.domain.Role;
import az.ingress.book_user_store.domain.User;
import az.ingress.book_user_store.domain.enumeration.RoleName;
import az.ingress.book_user_store.dto.UserDTO;
import az.ingress.book_user_store.repository.RoleRepository;
import az.ingress.book_user_store.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper mapper;

    private UserDTO userDTO;

    private User user;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        mapper = mock(ModelMapper.class);
        this.userService = new UserService(userRepository, roleRepository, passwordEncoder, mapper);

        userDTO = new UserDTO();
        userDTO.setEmail("test@bookstore.com");
        userDTO.setFirstName("First name");
        userDTO.setLastName("Last name");
        userDTO.setUsername("username");
        userDTO.setPassword("1test1");

        user = new User();
        user.setId(1L);
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword("errGtRhe432");
    }

    @Test
    void shouldSaveUser() {
        when(mapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        when(roleRepository.findByName(RoleName.USER)).thenReturn(any());
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn(UUID.randomUUID().toString());

        userService.save(userDTO);

        verify(mapper).map(userDTO, User.class);
        verify(roleRepository).findByName(RoleName.USER);
        verify(passwordEncoder).encode(userDTO.getPassword());

        verify(userRepository).save(any());
        verifyNoMoreInteractions(userRepository, roleRepository, mapper, passwordEncoder);
    }

    @Test
    void shouldGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> all = userService.findAll();

        assertEquals(1, all.size());

        verify(userRepository).findAll();
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(roleRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void shouldGetOneUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDTO fromDB = userService.findById(anyLong());

        assertEquals(user.getUsername(), fromDB.getUsername());
        assertEquals(user.getEmail(), fromDB.getEmail());
        assertEquals(user.getFirstName(), fromDB.getFirstName());
        assertEquals(user.getLastName(), fromDB.getLastName());

        verify(userRepository).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(roleRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void shouldThrowError_whenTryToGetOneUser_NotExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> userService.findById(anyLong())
        );

        verify(userRepository).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(roleRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void shouldAddPublisherRoleToUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(roleRepository.findByName(RoleName.PUBLISHER)).thenReturn(any(Role.class));

        userService.addPublisherRole(user.getId());

        verify(roleRepository).findByName(RoleName.PUBLISHER);
        verify(userRepository).save(any(User.class));
        verify(userRepository).findById(user.getId());
        verifyNoMoreInteractions(userRepository, roleRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenTryToAddPublisherRoleToUser_NotExistInDB() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> userService.addPublisherRole(anyLong())
        );

        verify(userRepository).findById(anyLong());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(roleRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void shouldDeleteUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(user.getId());

        userService.delete(user.getId());

        verify(userRepository).deleteById(user.getId());
        verify(userRepository).findById(user.getId());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowAccessDeniedException_whenTryToDeleteAdmin() {
        user.setRoles(new HashSet<>(Collections.singleton(new Role(null, RoleName.ADMIN))));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> userService.delete(user.getId())
        );

        verify(userRepository).findById(user.getId());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowAccessDeniedException_whenTryToDeleteUser_notExistInDB() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> userService.delete(user.getId())
        );

        verify(userRepository).findById(user.getId());
        verifyNoMoreInteractions(userRepository);
    }
}
