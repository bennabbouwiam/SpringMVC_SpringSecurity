package ma.jee.jpa.patientsmvc.sec.repositories;

import ma.jee.jpa.patientsmvc.sec.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, String> {
    //methode qui permet de retourner un appuser
    AppUser findByUsername(String username);
}
