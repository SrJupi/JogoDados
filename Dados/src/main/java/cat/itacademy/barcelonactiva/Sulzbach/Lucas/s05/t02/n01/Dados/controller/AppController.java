package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.controller;

import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.GameEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain.PlayerEntity;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.AddPlayerDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.GameDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.GamesRepository;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.PlayerRepository;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AppController {

    @Autowired
    private GamesRepository gamesRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private AppService appService;

    @GetMapping("/players")
    public ResponseEntity<List<PlayerDTO>> getPlayerGames(){
        return ResponseEntity.ok(appService.getPlayers());
    }

    @GetMapping("/players/{id}/games")
    public ResponseEntity<List<GameDTO>> getPlayerGames(@PathVariable Integer id){
        return ResponseEntity.ok(appService.getPlayerGames(id));
    }

    @GetMapping("/players/ranking")
    public ResponseEntity<?> getAvgRanking(){
        return appService.getAvgRanking();
    }

    @GetMapping("/players/ranking/loser")
    public ResponseEntity<?> getLoserRanking(){
        return appService.getLoserRanking();
    }

    @GetMapping("/players/ranking/winner")
    public ResponseEntity<?> getWinnerRanking(){
        return appService.getWinnerRanking();
    }

    @PostMapping("/players")
    public ResponseEntity<?> addPlayer(@RequestBody AddPlayerDTO addPlayerDTO){
        return appService.addPlayer(addPlayerDTO);
    }

    @PostMapping("/players/{id}/games")
    public ResponseEntity<GameDTO> playGame(@PathVariable Integer id){
        return ResponseEntity.ok(appService.playGame(id));
    }

}
