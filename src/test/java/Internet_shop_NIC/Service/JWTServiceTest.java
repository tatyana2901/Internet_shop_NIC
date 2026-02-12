package Internet_shop_NIC.Service;

import Internet_shop_NIC.Entity.Users;
import Internet_shop_NIC.Security.JwtService;
import Internet_shop_NIC.Security.UsDetails;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JWTServiceTest {
    private JwtService jwtService;
    private UsDetails usDetails;
    private Users user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        String testSigningKey = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
        setJwtSigningKey(jwtService, testSigningKey);

        user = new Users();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole("ROLE_USER");

        usDetails = new UsDetails(user);
    }

    private void setJwtSigningKey(JwtService service, String key) {
        try {
            java.lang.reflect.Field field = JwtService.class.getDeclaredField("jwtSigningKey");
            field.setAccessible(true);
            field.set(service, key);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось установить jwtSigningKey", e);
        }
    }

    @Test
    void createToken_ShouldGenerateValidTokenWithUserData() {
        String token = jwtService.createToken(usDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        Claims claims = jwtService.extractClaims(token);

        assertEquals(1, claims.get("id"));
        assertEquals("John", claims.get("firstName"));
        assertEquals("Doe", claims.get("lastName"));
        assertEquals("ROLE_USER", claims.get("role"));
        assertEquals("test@example.com", claims.getSubject());
    }

    @Test
    void extractUserName_ShouldReturnUserEmail() {
        String token = jwtService.createToken(usDetails);

        String email = jwtService.extractUserName(token);

        assertEquals("test@example.com", email);
    }

    @Test
    void extractClaims_ShouldReturnClaimsObject() {
        String token = jwtService.createToken(usDetails);

        Claims claims = jwtService.extractClaims(token);

        assertNotNull(claims);
        assertNotNull(claims.getSubject());
        assertNotNull(claims.getExpiration());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.get("id"));
        assertNotNull(claims.get("firstName"));
        assertNotNull(claims.get("lastName"));
        assertNotNull(claims.get("role"));
    }

    @Test
    void isTokenValid_ShouldReturnTrueForValidToken() {
        String token = jwtService.createToken(usDetails);

        boolean isValid = jwtService.isTokenValid(token);

        assertTrue(isValid);
    }

    @Test
    void isTokenExpired_ShouldReturnFalseForValidToken() {
        String token = jwtService.createToken(usDetails);

        boolean isExpired = jwtService.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    void isTokenValid_ShouldReturnFalseForInvalidSignature() {
        String token = jwtService.createToken(usDetails);

        JwtService invalidJwtService = new JwtService();
        String wrongKey = "1234567890123456789012345678901234567890123456789012345678901234";
        setJwtSigningKey(invalidJwtService, wrongKey);

        assertThrows(io.jsonwebtoken.security.SecurityException.class, () -> {
            invalidJwtService.extractClaims(token);
        });
    }

    @Test
    void createToken_ShouldSetCorrectExpirationDate() {
        String token = jwtService.createToken(usDetails);
        Claims claims = jwtService.extractClaims(token);

        Date expiration = claims.getExpiration();
        Date now = new Date();

        long expectedExpirationTime = 100000L * 60 * 24;
        long actualExpirationTime = expiration.getTime() - now.getTime();

        assertTrue(actualExpirationTime > 0);
        assertTrue(actualExpirationTime <= expectedExpirationTime + 1000);
    }

    @Test
    void createToken_ShouldGenerateDifferentTokensForDifferentUsers() {
        Users user2 = new Users();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setRole("ROLE_ADMIN");

        UsDetails usDetails2 = new UsDetails(user2);

        String token1 = jwtService.createToken(usDetails);
        String token2 = jwtService.createToken(usDetails2);

        assertNotEquals(token1, token2);

        Claims claims1 = jwtService.extractClaims(token1);
        Claims claims2 = jwtService.extractClaims(token2);

        assertEquals("test@example.com", claims1.getSubject());
        assertEquals("user2@example.com", claims2.getSubject());

        assertEquals("John", claims1.get("firstName"));
        assertEquals("Jane", claims2.get("firstName"));

        assertEquals("Doe", claims1.get("lastName"));
        assertEquals("Smith", claims2.get("lastName"));

        assertEquals("ROLE_USER", claims1.get("role"));
        assertEquals("ROLE_ADMIN", claims2.get("role"));
    }
}
