package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.model.domain;

import cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.entity.RolesEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "players")
public class PlayerEntity {

    @Id
    @SequenceGenerator(name="players_seq", sequenceName="player_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "player_seq")
    private Integer userId;
    private String password;
    private String name;
    private Timestamp registerDate;

    @JsonManagedReference
    @OneToMany(mappedBy="player")
    private Set <GameEntity> gamesList = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "player_roles", joinColumns = @JoinColumn(name = "player_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RolesEntity> roles = new HashSet<>();

    public PlayerEntity() {
    }

    public PlayerEntity(String name, String password) {
        this.name = name;
        this.password = password;
        this.registerDate = setRegisterDate();
    }

    private Timestamp setRegisterDate (){
        return new Timestamp(new Date().getTime());
    }
}
