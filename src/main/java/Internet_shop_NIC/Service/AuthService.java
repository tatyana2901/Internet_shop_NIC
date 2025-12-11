package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.AuthenticationRequestDTO;
import Internet_shop_NIC.DTO.JWTResponseDTO;
import Internet_shop_NIC.DTO.RegistrationRequestDTO;
import Internet_shop_NIC.Security.JwtService;
import Internet_shop_NIC.Security.UsDetailsService;
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

    private final UsDetailsService usDetailsService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UsDetailsService usDetailsService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.usDetailsService = usDetailsService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public JWTResponseDTO logIn(AuthenticationRequestDTO authRequest) {

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                        authRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(authInputToken);

        if (authentication.isAuthenticated()) {
            String jwt = jwtService.createToken((UserDetails) authentication.getPrincipal());
            return new JWTResponseDTO(jwt);
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }


    }


    public JWTResponseDTO registrate(RegistrationRequestDTO regRequest) {

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }



 /* public Person convertToPerson(PersonDTO personDTO) {
        return this.modelMapper.map(personDTO, Person.class);
    }*/


}
