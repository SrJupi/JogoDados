package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.controller;


import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.AddPlayerDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.GameDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.PlayerGamesDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.exceptions.NotAuthorizedError;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.GamesRepository;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.PlayerRepository;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.service.AppService;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.dto.JwtDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.dto.LoginPlayerDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.entity.PlayerPrincipal;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.jwt.JwtProvider;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
public class AppController {

    @Autowired
    private GamesRepository gamesRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private AppService appService;

    @Autowired
    JwtTokenFilter jwtTokenFilter;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/players")
    public ResponseEntity<List<PlayerDTO>> getPlayerGames(){
        return ResponseEntity.ok(appService.getPlayers());
    }

    @GetMapping("/players/{id}/games")
    public ResponseEntity<PlayerGamesDTO> getPlayerGames(@PathVariable Integer id, HttpServletRequest request){
        String header = jwtTokenFilter.getToken(request);
        if (id != Integer.parseInt(jwtProvider.getIdFromToken(header))) throw new NotAuthorizedError();
        return ResponseEntity.ok(appService.getPlayerGames(id));
    }

    @GetMapping("/players/ranking")
    public ResponseEntity<Float> getAvgRanking(){
        return appService.getAvgRanking();
    }

    @GetMapping("/players/ranking/loser")
    public ResponseEntity<PlayerDTO> getLoserRanking(){
        return ResponseEntity.ok(appService.getLoserRanking());
    }

    @GetMapping("/players/ranking/winner")
    public ResponseEntity<PlayerDTO> getWinnerRanking(){
        return ResponseEntity.ok(appService.getWinnerRanking());
    }

    @PostMapping("/players")
    public ResponseEntity <PlayerDTO> addPlayer(@RequestBody AddPlayerDTO addPlayerDTO){
        addPlayerDTO.setPassword(passwordEncoder.encode(addPlayerDTO.getPassword()));
        PlayerDTO responseDTO = appService.addPlayer(addPlayerDTO);
        return ResponseEntity
                .created(URI.create(String.format("/players/%d", responseDTO.getUserId())))
                .body(responseDTO);
    }

    @PostMapping("/players/{id}/games")
    public ResponseEntity<GameDTO> playGame(@PathVariable Integer id, HttpServletRequest request){
        String header = jwtTokenFilter.getToken(request);
        if (id != Integer.parseInt(jwtProvider.getIdFromToken(header))) {
            throw new NotAuthorizedError();
        }
        return ResponseEntity.ok(appService.playGame(id));
    }

    @PutMapping("/players")
    public ResponseEntity <PlayerDTO> updatePlayer(@RequestBody PlayerDTO playerDTO, HttpServletRequest request){
        String header = jwtTokenFilter.getToken(request);
        if (playerDTO.getUserId() != Integer.parseInt(jwtProvider.getIdFromToken(header))) throw new NotAuthorizedError();
        return appService.updatePlayer(playerDTO);
    }

    @DeleteMapping("/players/{id}/games")
    public ResponseEntity<String> deletePlayerGames(@PathVariable Integer id, HttpServletRequest request){
        String header = jwtTokenFilter.getToken(request);
        if (id != Integer.parseInt(jwtProvider.getIdFromToken(header))) throw new NotAuthorizedError();
        appService.deletePlayerGames(id);
        return ResponseEntity.ok(String.format("Games from player %d were deleted", id));
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
