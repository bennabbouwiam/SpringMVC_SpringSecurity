package ma.jee.jpa.patientsmvc.sec.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.jee.jpa.patientsmvc.sec.entities.AppRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//entité jpa
@Entity
@Data //Getters et Setters
@NoArgsConstructor //constructeur sans arguments
@AllArgsConstructor // constructeur avec arguments
public class AppUser {

    @Id // clé primaire
    private String userID;
    //le username doit etre unique
    @Column(unique = true)
    private String username;
    private String password;
    private boolean active;
    //attribut fetch = lazy => signifie que à chaque fois que je vais vharger un user à partir de la bdd
    //Hibernate ne vas pas charger automatiquement les roles de cet user en memoire
    // lorsque je veux les roles (getAppRole()) => charger les roles
    // ************************
    //attribut fetch = eager => signifie que à chaque fois que je vais vharger un user à partir de la bdd
    ////Hibernate vas charger automatiquement les roles de cet user au memoire
    @ManyToMany(fetch = FetchType.EAGER)
    private List<AppRole> appRoles = new ArrayList<>();
}
