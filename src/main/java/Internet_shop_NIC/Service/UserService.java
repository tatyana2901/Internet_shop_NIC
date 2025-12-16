package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.RegistrationRequest;
import Internet_shop_NIC.Entity.Users;
import Internet_shop_NIC.Exception.NewUserDataRegistrationNotProvided;
import Internet_shop_NIC.Exception.UserAlreadyExistException;
import Internet_shop_NIC.Mapper.RegistrationRequestMapper;
import Internet_shop_NIC.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RegistrationRequestMapper registrationRequestMapper;


    @Autowired
    public UserService(UserRepository userRepository, RegistrationRequestMapper registrationRequestMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.registrationRequestMapper = registrationRequestMapper;
    }

    public void create(Users user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException("Пользователь с таким email уже существует");
        }
        save(user);
    }

    private void save(Users user) {
        userRepository.save(user);
    }

    public void register(RegistrationRequest regRequest) {
        Users user = registrationRequestMapper.toUser(regRequest);
        create(user);
    }


}
