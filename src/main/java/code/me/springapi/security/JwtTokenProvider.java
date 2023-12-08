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

    private final String secret = "keyboardcat-testwerwerwerwerwererwwerwerwerwetgwegwegwegwerwerwerwerwerwerwerwerw234234234wefr2342f234";
    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());

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

    public Long getUserIdFromToken(String token) {
        return getTokenClaim(token, "id", Long.class);
    }

    public int getTokenId(String token) {
        return getTokenClaim(token, "id", Integer.class);
    }

    public <T> T getTokenClaim(String token, String type, Class<T> returnType) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(type, returnType);
    }

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