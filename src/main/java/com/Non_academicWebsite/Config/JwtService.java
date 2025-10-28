package com.Non_academicWebsite.Config;

import com.Non_academicWebsite.Entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "MjJEQkMyQTkzNkZEMkJGMkY0RjM3NDYyQjZBQkMzMjJOMzJOMzJOM0oyTkozSjIzSlNOMzIzRU5EMjM4MzI0TkRKMlM4U1c";
//    private String SECRET_KEY = "";

//    public JwtService() throws NoSuchAlgorithmException {
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
//        SecretKey secretKey = keyGenerator.generateKey();
//        SECRET_KEY = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        try{
            return Jwts
                    .builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        }catch (Exception e){
            throw new IllegalStateException("Invalid jwt token", e);
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails, Role role) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return generateToken(new HashMap<>(), userDetails);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userEmail = extractUserEmail(token);
        return (userEmail.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
