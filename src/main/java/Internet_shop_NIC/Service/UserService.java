package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.RegistrationRequestDTO;
import Internet_shop_NIC.Entity.Users;
import Internet_shop_NIC.Exception.NewUserDataRegistrationNotProvided;
import Internet_shop_NIC.Exception.UserAlreadyExistException;
import Internet_shop_NIC.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users create(Users user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException("Пользователь с таким email уже существует");
        }
        return save(user);
    }

    public Users save(Users user) {
        return userRepository.save(user);
    }

    public void register(RegistrationRequestDTO regRequest) {
        Users user = toUser(regRequest);
        create(user);
    }

    private Users toUser(RegistrationRequestDTO regRequest) {
        if (regRequest != null) {
            Users user = new Users();
            user.setFirst_name(regRequest.getFirst_name());
            user.setLast_name(regRequest.getLast_name());
            user.setEmail(regRequest.getEmail());
            user.setPassword(passwordEncoder.encode(regRequest.getPassword()));
            user.setRole("ROLE_USER");
            user.setCreatedAt(LocalDateTime.now());
            return user;
        }
        throw new NewUserDataRegistrationNotProvided("Отсутствуют данные для регистрации нового пользователя.");
    }


}
