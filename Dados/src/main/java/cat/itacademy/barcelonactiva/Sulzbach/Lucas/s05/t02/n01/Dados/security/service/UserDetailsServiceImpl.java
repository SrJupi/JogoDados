package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.service;

import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.PlayerEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.exceptions.ResourceNotFoundException;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.PlayerRepository;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.entity.PlayerPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    PlayerRepository playerRepository;

    public UserDetails loadUserByUsername (Integer id){
        PlayerEntity player = playerRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("player", "id", String.valueOf(id)));
        return PlayerPrincipal.build(player);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            Integer id = Integer.parseInt(username);
            return loadUserByUsername(id);
        }catch (Exception e)
        {
            Optional<PlayerEntity> player = playerRepository.findByName(username);
            if (player.isEmpty()) throw new ResourceNotFoundException();
            return PlayerPrincipal.build(player.get());
        }

    }
}
