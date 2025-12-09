package Internet_shop_NIC.Controller;

import Internet_shop_NIC.DTO.AuthenticationRequestDTO;
import Internet_shop_NIC.DTO.JWTResponseDTO;
import Internet_shop_NIC.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
/*ПОПРАВИТЬ*@RequestMapping("/auth")*/

public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public JWTResponseDTO performLogin(@RequestBody AuthenticationRequestDTO authRequestDTO) {

        JWTResponseDTO v = authService.signIn(authRequestDTO);
        System.out.println(v);
        return v;
    }

}
