package morocco.it.TicketSystem.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "my-secret-key"; // Change this in production
    private final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour


    public String generateToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role) // Add role claim
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    public boolean validateToken(String token, String username) {
        DecodedJWT jwt = verifyToken(token);
        return jwt != null && username.equals(jwt.getSubject()) && !isTokenExpired(token);
    }

    public DecodedJWT verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            // Invalid token
            return null;
        }
    }

    public boolean isTokenExpired(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt != null && jwt.getExpiresAt().before(new Date());
    }

    public String extractUsername(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt != null ? jwt.getSubject() : null;
    }

    public String extractRole(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt != null ? jwt.getClaim("role").asString() : null;
    }

}
