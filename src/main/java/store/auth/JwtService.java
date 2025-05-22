package store.auth;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${store.jwt.issuer}")
    private String issuer;

    @Value("${store.jwt.secret-key}")
    private String secretKey;

    public String create(String id, Date notBefore, Date expiration) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        String jwt = Jwts.builder()
            .header()
            .and()
            .id(id)
            .issuer(issuer)
            .signWith(key)
            .notBefore(notBefore)
            .expiration(expiration)
            .compact();
        return jwt;
    }

    public String getId(String token) {
        Claims claims = resolveClaims(token);

        Date now = new Date();
        if (claims.getExpiration().before(now)) {
            throw new RuntimeException("Expired token");
        }
        if (claims.getNotBefore().after(now)) {
            throw new RuntimeException("Token is actived yet");
        }
        // check if token is revoked by a CRL
        return claims.getId();
    }

    private Claims resolveClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        JwtParser parser = Jwts.parser().verifyWith(key).build();
        return parser.parseSignedClaims(token).getPayload();
    }
    
}
