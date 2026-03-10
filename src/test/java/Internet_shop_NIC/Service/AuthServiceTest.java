package Internet_shop_NIC.Service;

import Internet_shop_NIC.DTO.AuthenticationRequest;
import Internet_shop_NIC.DTO.JWTResponse;
import Internet_shop_NIC.Entity.Users;
import Internet_shop_NIC.Security.JwtService;
import Internet_shop_NIC.Security.UsDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthService authService;

    private AuthenticationRequest authRequest;
    private UsDetails usDetails;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        authRequest = new AuthenticationRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("password123");
        usDetails = new UsDetails(new Users());
        authentication = mock(Authentication.class);
    }

    @Test
    void logIn_ShouldReturnJWTResponseSuccess() {
        String testJwtToken = "test.jwt.token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(usDetails);
        when(jwtService.createToken(usDetails)).thenReturn(testJwtToken);

        JWTResponse response = authService.logIn(authRequest);

        assertNotNull(response);
        assertEquals(testJwtToken, response.getToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authentication).isAuthenticated();
        verify(jwtService).createToken(usDetails);
    }

    @Test
    void logIn_ShouldThrowUsernameNotFoundExceptionFailed() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.logIn(authRequest);
        });

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authentication).isAuthenticated();
        verify(jwtService, never()).createToken(any());
    }


}