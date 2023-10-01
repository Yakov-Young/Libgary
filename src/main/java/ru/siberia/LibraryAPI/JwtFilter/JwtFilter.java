package ru.siberia.LibraryAPI.JwtFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import ru.siberia.LibraryAPI.JwtFilter.Error.Unauthentication;

import javax.crypto.SecretKey;

public class JwtFilter {
    private static final String JWT_KEY = "CGuZtKUBu3JIvXOCwWIyJYSS4cP+TNiDIDdvhr6aqnpQ45y3nw0qC9WY4cJPiHXcKKKlILZhpJI8hX5MTRn9QQ==";

    public static Claims getBody(String token) {
        try {

            SecretKey secret = Keys.hmacShaKeyFor(
                    Decoders.BASE64.decode(JWT_KEY)
            );

            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build();

            Jws<Claims> claimsJws = parser.parseClaimsJws(token);
            System.out.println(claimsJws.getBody().get("role"));
            return claimsJws.getBody();
        } catch (Exception e) {
            throw new Unauthentication("Invalid token");
        }
    }
}
