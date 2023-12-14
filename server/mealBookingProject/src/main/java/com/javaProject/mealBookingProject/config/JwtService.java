package com.javaProject.mealBookingProject.config;

import com.javaProject.mealBookingProject.customExceptions.InvalidTokenException;
import com.javaProject.mealBookingProject.customExceptions.TokenExpiredException;
import com.javaProject.mealBookingProject.entity.UserTable;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "b0dc797765623353df804979112bc207604137628988f9a5c16f67e4dd189300";
    private final long expirationTimeMillis = 86400000; // 1 day in milliseconds

    public String extractUsername(String jwtTok) {
        return extractClaims(jwtTok, Claims::getSubject);
    }

    public <T> T extractClaims(String jwtTok, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtTok);
        return claimsResolver.apply(claims);
    }


    public String generateToken( UserTable userTable) {
//        Map<String, Object> claims = getStringObjectMap(userDetails, userTable);
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTimeMillis);

        return Jwts.builder()
//                .setClaims(claims)
                .setSubject(userTable.getUsername())
                .claim("Role", userTable.getRole().name())
                .claim("Email", userTable.getEmail())
                .claim("User_ID",userTable.getUserID())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
//    public UserDetails getUserDetails(String jwtToken) {
//        UserTable userDetails = new UserTable();
//        Claims claims = extractAllClaims(jwtToken);
//
//        String subject = claims.getSubject();
//        String roles = (String) claims.get("Role");
//
//        roles = roles.replace("[", "").replace("]", "");
//        String[] roleNames = roles.split(",");
//
//        userDetails.setEmail(subject);
//        for (String roleName : roleNames) {
//            UserTable.UserRole role = UserTable.UserRole.valueOf(roleName.trim());
//            userDetails.setRole(role);
//        }
//
//        userDetails.setEmail((String) claims.get("Email"));
//        userDetails.setUser_Name((String) claims.get("User_name"));
//        userDetails.setUserID((Long) claims.get("User_ID"));
//
//        return userDetails;
//    }


//    public boolean isTokenValid(String jwtTok, UserDetails userDetails) {
//        final String userName = extractUsername(jwtTok);
//        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(jwtTok);
//    }


    private boolean isTokenExpired(String jwtTok) {
        return extractExpiration(jwtTok).before(new Date());
    }

    private Date extractExpiration(String jwtTok) {
        return extractClaims(jwtTok, Claims::getExpiration);
    }

    private Claims extractAllClaims(String jwtTok) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwtTok)
                .getBody();
    }

    public Integer extractUserId(String jwtToken) {
        Claims claims = extractAllClaims(jwtToken);
        return (Integer) claims.get("User_ID");
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token) throws InvalidTokenException, TokenExpiredException, UnsupportedJwtException {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            throw new InvalidTokenException("Invalid JWT signature", e);
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("JWT token expired", e);
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("JWT token is not supported", e);
        } catch (Exception e) {
            throw new InvalidTokenException("Unknown error validating JWT token", e);
        }
    }




}
