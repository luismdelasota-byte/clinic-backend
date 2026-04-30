package com.clinicapp.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component //Esto ya genera el constructor por defecto osea crea public JwtUtil(){}
public class JwtUtil {

    // Clave secreta para firmar el token (en producción usa algo más seguro)
    private final String SECRET_KEY = "EstaEsUnaClaveMuySeguraDeMasDe32Caracteres!!!123";

    // Tiempo de expiración del token (ejemplo: 24 horas)
    private final long EXPIRATION_MS = 24 * 60 * 60 * 1000;

    //Es un metodo privado de la clase auxiliar que encapsula la logica de convertir la SECRE_KEY
    //en un objeto key
    //Usa el algoritmo HMAC con SHA-256
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Genera un token para un username
    //Aqui se contruye el JWT, contiene:
    /* Header -> algoritmo de firma HS256
     * Payload -> username : sub, rol : role, fecha de creacion : iat, fecha de expiracion : exp, signature : firma con mi clave secreta*/
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)//guarda el username
                .claim("role", role) //aquí guardas el rol como claim extra
                .setIssuedAt(new Date())//fecha de creacion
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))//fecha de expiracion
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)//firma con la clave secreta
                .compact(); //devuelve el token como String
    }

    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    // Obtiene el username desde el token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Valida que el token sea válido y no esté expirado
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}