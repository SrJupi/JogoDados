package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.controller;


import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.AddPlayerDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.PlayerRepository;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.service.AppService;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.dto.JwtDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.dto.LoginPlayerDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.entity.PlayerPrincipal;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.jwt.JwtProvider;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class AuthController {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AppService appService;


    @PostMapping("/login/new")
    public ResponseEntity<PlayerDTO> newPlayer(@RequestBody AddPlayerDTO addPlayerDTO, BindingResult bindingResult){
        PlayerDTO responseDTO = appService.addPlayer(addPlayerDTO);
        return ResponseEntity
                .created(URI.create(String.format("/players/%d", responseDTO.getUserId())))
                .body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> loginPlayer(@RequestBody LoginPlayerDTO loginPlayerDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginPlayerDTO.getUserId(),
                        loginPlayerDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        PlayerPrincipal userDetails = (PlayerPrincipal) authentication.getPrincipal();
        JwtDTO jwtDTO = new JwtDTO(jwt, userDetails.getUserId(), userDetails.getAuthorities());
        return new ResponseEntity<>(jwtDTO, HttpStatus.OK);
    }



}
