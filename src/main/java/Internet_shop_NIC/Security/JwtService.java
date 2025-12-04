package Internet_shop_NIC.Security;

import Internet_shop_NIC.Entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//ЗДЕСЬ МЫ НЕСКОЛЬКО РАЗ ИЗВЛЕКАЕМ ДАННЫЕ ИЗ ТОКЕНА _СНАЧАЛОА ЧТОБЫ ПОЛУЧИТЬ А ПОТОМ ЧТОБЫ ПРОВЕРИТЬ -МОЖНО ИЗБЕЖАТЬ ДВОЙНОГО ИЗВЛЕЧЕНИЯ?
@Service
public class JwtService {

    @Value("${token.signing.key}")
    private String jwtSigningKey;

    public String createToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User) {
            User customUserDetails = (User) userDetails;
            claims.put("id", customUserDetails.getId());
            claims.put("last_name", customUserDetails.getLast_name());
            claims.put("first_name", customUserDetails.getFirst_name());
            claims.put("role", customUserDetails.getRole());
        }
        return generateToken(claims, userDetails);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    // Проверка токена
    private Claims extractClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUserName(String token) {
        return extractClaims(token).getSubject();
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String tokenUsername = extractUserName(token);
        return (userDetails.getUsername().equals(tokenUsername) && !isTokenExpired(token));
    }


    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}









