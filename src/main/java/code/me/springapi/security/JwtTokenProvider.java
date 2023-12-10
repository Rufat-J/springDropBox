package code.me.springapi.security;

import code.me.springapi.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@NoArgsConstructor
public class JwtTokenProvider {

    // The secret key used for signing the JWT
    private final String secret = "keyboardcat-testwerwerwerwerwererwwerwerwerwetgwegwegwegwerwerwerwerwerwerwerwerwerw234234234wefr2342f234";

    // The key object derived from the secret
    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());

    /**
     * Generates a JWT token for the provided user.
     *
     * @param user The user for whom the token is generated.
     * @return The generated JWT token.
     */
    public String generateToken(User user) {
        long id = user.getId();
        String username = user.getUsername();

        return Jwts.builder()
                .setSubject(Long.toString(id))
                .claim("id", id)
                .claim("username", username)
                .signWith(key)
                .compact();
    }

    /**
     * Extracts the user ID from the provided JWT token.
     *
     * @param token The JWT token.
     * @return The user ID extracted from the token.
     */
    public Long getUserIdFromToken(String token) {
        return getTokenClaim(token, "id", Long.class);
    }

    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param token      The JWT token.
     * @param type       The type of the claim.
     * @param returnType The expected return type of the claim.
     * @return The extracted claim value.
     */
    public <T> T getTokenClaim(String token, String type, Class<T> returnType) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(type, returnType);
    }

    /**
     * Validates the provided JWT token.
     *
     * @param token The JWT token to be validated.
     * @return True if the token is valid, false otherwise.
     */
    public boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
