package com.example.demo.providers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(String documentNumber, String documentType) {
        LOGGER.debug("Generating token for documentNumber: {}, documentType: {}", documentNumber, documentType);

        Map<String, Object> claims = new HashMap<>();
        claims.put("documentNumber", documentNumber);
        claims.put("documentType", documentType);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

        LOGGER.debug("Token generated successfully: {}", token);
        return token;
    }

    public String getDocumentNumberFromToken(String token) {
        LOGGER.debug("Parsing token and extracting documentNumber");

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();

            String documentNumber = claims.get("documentNumber", String.class);

            LOGGER.debug("DocumentNumber extracted from token: {}", documentNumber);
            return documentNumber;
        } catch (Exception e) {
            LOGGER.error("Error parsing token: {}", e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        LOGGER.debug("Validating token");

        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            LOGGER.debug("Token is valid");
            return true;
        } catch (Exception e) {
            LOGGER.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
}
