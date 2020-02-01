package com.szymonosicinski.tripplanner.Util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    public String generateToken(final Authentication authentication) {
        final UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return generateToken(userPrincipal);
    }

    public String generateToken(final UserPrincipal userPrincipal) {

        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        final String userId = userPrincipal.getId() != null ? userPrincipal.getId().toString() : "-1";

        return Jwts.builder()
                .setSubject(userPrincipal.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("username", userPrincipal.getUsername())
                .claim("authorities", userPrincipal.getAuthorities().toString())
                .claim("userId", userId)
                .claim("name", userPrincipal.getName())
                .claim("surname", userPrincipal.getSurname())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public UUID getUserIdFromJwt(final String token) {
        final Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return UUID.fromString(claims.getSubject());
    }

    public Boolean validateToken(final String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token);
            return true;
        } catch (final SignatureException ex) {
            System.out.println("Invalid JWT signature!");
        } catch (final MalformedJwtException ex) {
            System.out.println("Invalid JWT!");
        } catch (final ExpiredJwtException ex) {
            System.out.println("Expired JWT!");
        } catch (final UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT!");
        } catch (final IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty!");
        }
        return false;
    }
}
