package ma.jee.jpa.patientsmvc.sec.repositories;

import ma.jee.jpa.patientsmvc.sec.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    //methode qui permet de retourner un role
    AppRole findByRoleName(String rolename);
}
