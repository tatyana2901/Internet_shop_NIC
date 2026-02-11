package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.RegistrationRequest;
import Internet_shop_NIC.Entity.Users;
import Internet_shop_NIC.Exception.UserAlreadyExistException;
import Internet_shop_NIC.Mapper.RegistrationRequestMapper;
import Internet_shop_NIC.Mapper.RegistrationRequestMapperImpl;
import Internet_shop_NIC.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private RegistrationRequestMapper registrationRequestMapper;

    private PasswordEncoder passwordEncoder ;
    @InjectMocks
    private UserService userService;

    private Users user;
    private RegistrationRequest request;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();

        registrationRequestMapper = new RegistrationRequestMapperImpl();
        ReflectionTestUtils.setField(registrationRequestMapper, "passwordEncoder", passwordEncoder);

        user = new Users();
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

/*    @Test
    @DisplayName("Получение ID пользователя из UsDetails")
    void getUserId_ReturnsUserId() {
        // Arrange
        UsDetails usDetails = mock(UsDetails.class);
        when(usDetails.getUser()).thenReturn(testUser);

        // Act
        Long userId = userService.getUserId(usDetails);

        // Assert
        assertEquals(1L, userId);
        verify(usDetails).getUser();
    }

    @Test
    @DisplayName("Получение пользователя из UsDetails")
    void getUser_ReturnsUser() {
        // Arrange
        UsDetails usDetails = mock(UsDetails.class);
        when(usDetails.getUser()).thenReturn(testUser);

        // Act
        Users result = userService.getUser(usDetails);

        // Assert
        assertEquals(testUser, result);
        verify(usDetails).getUser();
    }

    @Test
    @DisplayName("Проверка существования пользователя по ID - существует")
    void ifUserExists_UserExists_ReturnsTrue() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        boolean result = userService.ifUserExists(userId);

        // Assert
        assertTrue(result);
        verify(userRepository).existsById(userId);
    }

    @Test
    @DisplayName("Проверка существования пользователя по ID - не существует")
    void ifUserExists_UserNotExists_ReturnsFalse() {
        // Arrange
        Long userId = 999L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act
        boolean result = userService.ifUserExists(userId);

        // Assert
        assertFalse(result);
        verify(userRepository).existsById(userId);
    }

    @Test
    @DisplayName("Получение пользователя по ID")
    void getUserById_ReturnsUser() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(testUser));

        // Act
        Users result = userService.getUserById(userId);

        // Assert
        assertEquals(testUser, result);
        verify(userRepository).findById(userId);
    }

*/


}
