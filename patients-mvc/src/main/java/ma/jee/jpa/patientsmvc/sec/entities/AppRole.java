package ma.jee.jpa.patientsmvc.sec.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class AppRole {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    //le role doit etre unique
    @Column(unique = true)
    private String roleName;
    private String description;
}
