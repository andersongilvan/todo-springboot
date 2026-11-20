package users.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import users.entity.User;

import java.time.Instant;

@Service
public class JwtService {

    private static final String ISSUER = "users-api";

    @Value("${JWT_EXPIRATION}")
    private long accessTokenExpiration;

    @Value("${REFRESH_TOKEN_EXPIRATION}")
    private long refreshTokenExpiration;

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    public String generateAccessToken(User user) {
        return generateToken(user, TokenType.ACCESS_TOKEN, accessTokenExpiration);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, TokenType.REFRESH_TOKEN, refreshTokenExpiration);
    }

    private String generateToken(User user, TokenType type, long expirationSeconds) {
        try {
            Instant now = Instant.now();
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getId().toString())
                    .withClaim("role", user.getRole().name())
                    .withClaim("type", type.name())
                    .withIssuedAt(now)
                    .withExpiresAt(now.plusSeconds(expirationSeconds))
                    .sign(Algorithm.HMAC256(jwtSecret));
        } catch (JWTCreationException e) {
            throw new IllegalStateException("Erro ao gerar o token JWT", e);
        }
    }

    /**
     * Valida um ACCESS token (recusa refresh tokens) e retorna o id do usuário (subject).
     */
    public String validateAccessToken(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(this.jwtSecret))
                .withIssuer(ISSUER)
                .withClaim("type", TokenType.ACCESS_TOKEN.name())
                .build()
                .verify(token)
                .getSubject();
    }

    /**
     * Valida assinatura, issuer e expiração. Lança JWTVerificationException se inválido.
     */
    public String validateToken(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(this.jwtSecret))
                .withIssuer(ISSUER)
                .build()
                .verify(token)
                .getSubject(); // Retorna o ID do usuário (subject)
    }
}
