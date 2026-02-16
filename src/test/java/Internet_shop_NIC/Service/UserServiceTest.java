package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.RegistrationRequest;
import Internet_shop_NIC.Entity.Users;
import Internet_shop_NIC.Exception.UserAlreadyExistException;
import Internet_shop_NIC.Mapper.RegistrationRequestMapper;
import Internet_shop_NIC.Mapper.RegistrationRequestMapperImpl;
import Internet_shop_NIC.Repository.UserRepository;
import Internet_shop_NIC.Security.UsDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private RegistrationRequestMapper registrationRequestMapper;

    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private Users user;
    private RegistrationRequest request;
    private UsDetails usDetails;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();

        registrationRequestMapper = new RegistrationRequestMapperImpl();
        ReflectionTestUtils.setField(registrationRequestMapper, "passwordEncoder", passwordEncoder);

        user = new Users();
        user.setId(1L);
        user.setEmail("test@example.com");

        userService = new UserService(userRepository, registrationRequestMapper);

        request = new RegistrationRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Doe");
    }

    @Test
    void create_ShouldCreateUserSuccessfully() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        userService.create(user);

        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void create_ShouldThrowUserAlreadyExistException() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        UserAlreadyExistException exception = assertThrows(UserAlreadyExistException.class, () -> {
            userService.create(user);
        });

        assertTrue(exception.getMessage().contains("Пользователь с таким email уже существует"));

        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_ShouldCreateUserWithCorrectData() {
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);

        userService.register(request);

        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
        verify(userRepository).save(userCaptor.capture());

        Users savedUser = userCaptor.getValue();

        assertEquals(request.getEmail(), savedUser.getEmail());
        assertEquals(request.getFirstName(), savedUser.getFirstName());
        assertEquals(request.getLastName(), savedUser.getLastName());

        assertNotEquals(request.getPassword(), savedUser.getPassword());
        assertTrue(passwordEncoder.matches(request.getPassword(), savedUser.getPassword()));

        verify(userRepository).existsByEmail(request.getEmail());
    }

    @Test
    void register_ShouldThrowUserAlreadyExistExceptionIfEmailAlreadyExists() {
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        UserAlreadyExistException exception = assertThrows(UserAlreadyExistException.class, () -> {
            userService.register(request); // <-- Здесь маппер вызовет passwordEncoder.encode() без NPE
        });

        assertTrue(exception.getMessage().contains("Пользователь с таким email уже существует"));

        verify(userRepository).existsByEmail(request.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserId_ShouldReturnUserId() {
        UsDetails usDetails = mock(UsDetails.class);
        when(usDetails.getUser()).thenReturn(user);

        Long userId = userService.getUserId(usDetails);

        assertEquals(1L, userId);
        verify(usDetails).getUser();
    }

    @Test
    void getUser_ShouldReturnUser() {
        UsDetails usDetails = mock(UsDetails.class);
        when(usDetails.getUser()).thenReturn(user);

        Users result = userService.getUser(usDetails);

        assertEquals(user, result);
        verify(usDetails).getUser();
    }

    @Test
    void ifUserExists_ShouldReturnTrueIfUserExist() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        boolean result = userService.ifUserExists(userId);

        assertTrue(result);
        verify(userRepository).existsById(userId);
    }

    @Test
    void ifUserExists_ShouldReturnFalseIfUserNotExist() {
        Long userId = 999L;
        when(userRepository.existsById(userId)).thenReturn(false);

        boolean result = userService.ifUserExists(userId);

        assertFalse(result);
        verify(userRepository).existsById(userId);
    }

    @Test
    void getUserById_ShouldReturnUser() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        Users result = userService.getUserById(userId);

        assertEquals(user, result);
        verify(userRepository).findById(userId);
    }


}
