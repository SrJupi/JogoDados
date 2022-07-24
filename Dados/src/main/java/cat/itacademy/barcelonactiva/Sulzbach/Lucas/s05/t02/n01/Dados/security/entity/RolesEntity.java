package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class RolesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String roleName;

    public RolesEntity() {
    }

    public RolesEntity(String roleName) {
        this.roleName = roleName;
    }
}
