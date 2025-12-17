package Internet_shop_NIC.Controller;

import Internet_shop_NIC.DTO.AuthenticationRequest;
import Internet_shop_NIC.DTO.JWTResponse;
import Internet_shop_NIC.DTO.RegistrationRequest;
import Internet_shop_NIC.Service.AuthService;
import Internet_shop_NIC.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")

public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public JWTResponse performLogin(@RequestBody AuthenticationRequest authRequest) {
        System.out.println(authRequest);
        return authService.logIn(authRequest);
    }


    @PostMapping("/registration")
    public ResponseEntity<Void> performRegistration(@RequestBody RegistrationRequest registrationRequest) {
        userService.register(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
