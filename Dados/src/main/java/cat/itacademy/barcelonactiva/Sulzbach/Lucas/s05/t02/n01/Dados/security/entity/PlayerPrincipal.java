package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.entity;

import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.PlayerEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerPrincipal implements UserDetails {

    private Integer userId;
    private String password;
    private String name;
    private Collection<? extends GrantedAuthority> authorities;

    public PlayerPrincipal(Integer userId, String password, String name, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.authorities = authorities;
    }

    public static PlayerPrincipal build (PlayerEntity player){
        List<GrantedAuthority> authorityList = player.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getRoleName())).collect(Collectors.toList());
        return new PlayerPrincipal(player.getUserId(), player.getPassword(), player.getName(), authorityList);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    public int getUserId(){return userId;}

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
