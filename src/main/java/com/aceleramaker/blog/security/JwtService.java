package com.aceleramaker.blog.security;
import com.aceleramaker.blog.model.User;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // Chave está aberta a fins de teste
    // NOSONAR-START
    private static final Key SECRET_KEY = new SecretKeySpec(
            "acelera-maker-key-blog".getBytes(StandardCharsets.UTF_8),
            0,
            "acelera-maker-key-blog".getBytes(StandardCharsets.UTF_8).length,
            "HmacSHA256"
    ); //NOSONAR
    // NOSONAR-END
    private static final long EXPIRATION_TIME = 86400000; // 1 dia

    public String generateToken(User usuario) {
        return Jwts.builder()
                .setSubject(usuario.getUsuario())
                .claim("role", usuario.getTipoUsuario().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}