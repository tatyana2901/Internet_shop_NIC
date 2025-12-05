package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.AuthenticationRequestDTO;
import Internet_shop_NIC.DTO.JWTResponseDTO;
import Internet_shop_NIC.Security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    //  private final RegistrationService registrationService;
    //   private final PersonValidator personValidator;
    //  private final ModelMapper modelMapper;


    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthService(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JWTResponseDTO signIn(AuthenticationRequestDTO authRequest) {

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                        authRequest.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Incorrect credentials!");
        }


        UserDetails user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        String jwt = jwtService.createToken(user);
        return new JWTResponseDTO(jwt);
    }





    public Person convertToPerson(PersonDTO personDTO) {
        return this.modelMapper.map(personDTO, Person.class);
    }









    /* *//**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     *//*
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

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
*/


}
