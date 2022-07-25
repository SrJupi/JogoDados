package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.jwt;

import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.entity.PlayerPrincipal;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(Authentication authentication){
        PlayerPrincipal usuarioPrincipal = (PlayerPrincipal) authentication.getPrincipal();
        return Jwts.builder().setSubject(String.valueOf(usuarioPrincipal.getUserId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getIdFromToken(String token){
         return Jwts.parser().setSigningKey(secret)
                 .parseClaimsJws(token)
                 .getBody()
                 .getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token);
            return true;
        }catch (MalformedJwtException e){
            System.out.println(e.getMessage());
        }catch (UnsupportedJwtException e){
            System.out.println(e.getMessage());
        }catch (ExpiredJwtException e){
            System.out.println(e.getMessage());
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }catch (SignatureException e){
            System.out.println(e.getMessage());
        }
        return false;
    }


}
