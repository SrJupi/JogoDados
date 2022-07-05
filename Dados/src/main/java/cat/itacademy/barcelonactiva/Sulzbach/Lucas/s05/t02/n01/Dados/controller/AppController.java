package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.controller;


import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.AddPlayerDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.GameDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.dto.PlayerGamesDTO;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.GamesRepository;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.repository.PlayerRepository;
import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/players")
    public ResponseEntity<List<PlayerDTO>> getPlayerGames(){
        return ResponseEntity.ok(appService.getPlayers());
    }

    @GetMapping("/players/{id}/games")
    public ResponseEntity<PlayerGamesDTO> getPlayerGames(@PathVariable Integer id){
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
        PlayerDTO responseDTO = appService.addPlayer(addPlayerDTO);
        return ResponseEntity
                .created(URI.create(String.format("/players/%d", responseDTO.getUserId())))
                .body(responseDTO);
    }

    @PostMapping("/players/{id}/games")
    public ResponseEntity<GameDTO> playGame(@PathVariable Integer id){
        return ResponseEntity.ok(appService.playGame(id));
    }

    @PutMapping("/players")
    public ResponseEntity <PlayerDTO> updatePlayer(@RequestBody PlayerDTO playerDTO){
        return appService.updatePlayer(playerDTO);
    }

    @DeleteMapping("/players/{id}/games")
    public ResponseEntity<String> deletePlayerGames(@PathVariable Integer id){
        appService.deletePlayerGames(id);
        return ResponseEntity.ok(String.format("Games from player %d were deleted", id));
    }

}
