package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class JwtDTO {

    private String token;
    private String bearer = "Bearer";
    private Integer userID;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtDTO(String token, Integer userID, Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.userID = userID;
        this.authorities = authorities;
    }

    public JwtDTO(String jwt, int userID) {
        this.token = token;
        this.userID = userID;
    }
}
