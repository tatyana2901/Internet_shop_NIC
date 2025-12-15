package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.AuthenticationRequest;
import Internet_shop_NIC.DTO.JWTResponse;
import Internet_shop_NIC.Security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    //  private final RegistrationService registrationService;
    //   private final PersonValidator personValidator;
    //  private final ModelMapper modelMapper;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public JWTResponse logIn(AuthenticationRequest authRequest) {

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                        authRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(authInputToken);

        if (authentication.isAuthenticated()) {
            String jwt = jwtService.createToken((UserDetails) authentication.getPrincipal());
            return new JWTResponse(jwt);
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }


    }






 /* public Person convertToPerson(PersonDTO personDTO) {
        return this.modelMapper.map(personDTO, Person.class);
    }*/


}
