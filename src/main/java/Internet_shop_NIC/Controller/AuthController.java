package Internet_shop_NIC.Controller;

import Internet_shop_NIC.DTO.AuthenticationRequestDTO;
import Internet_shop_NIC.DTO.JWTResponseDTO;
import Internet_shop_NIC.DTO.RegistrationRequestDTO;
import Internet_shop_NIC.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")

public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public JWTResponseDTO performLogin(@RequestBody AuthenticationRequestDTO authRequestDTO) {
        return authService.logIn(authRequestDTO);
    }


    @PostMapping("/registration")
    public JWTResponseDTO performRegistration(@RequestBody RegistrationRequestDTO registrationRequestDTO) {
        return authService.registrate(registrationRequestDTO);
    }

}
