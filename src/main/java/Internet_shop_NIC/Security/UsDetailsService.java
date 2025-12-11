package Internet_shop_NIC.Security;

import Internet_shop_NIC.Entity.Users;
import Internet_shop_NIC.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UsDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findByEmail(email);


        if (!user.isPresent())
            throw new UsernameNotFoundException("User not found");

        return new UsDetails(user.get());
    }
}
