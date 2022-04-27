package ma.jee.jpa.patientsmvc.sec.service;

import groovy.util.logging.Slf4j;
import lombok.AllArgsConstructor;
import ma.jee.jpa.patientsmvc.sec.entities.AppRole;
import ma.jee.jpa.patientsmvc.sec.entities.AppUser;
import ma.jee.jpa.patientsmvc.sec.repositories.AppRoleRepository;
import ma.jee.jpa.patientsmvc.sec.repositories.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;


//la couche service concernant la partie sécurité


//cette classe est un service
@Service
//pour loguer des informations
@Slf4j
@AllArgsConstructor //
//annotation transactional de spring
// spring qui va gérer les transactions
@Transactional  //=> les methodes doivent etre transactionnelles
public class SecurityServiceImpl implements SecurityService {
    /*
    //faire l'injection des dépendances par constructeur ou par annotation @Autowired
    //ce constructeur = @AllArgsConstructors
    public SecurityServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
    }
*/

    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    //injecter passwordEncoder
    private PasswordEncoder passwordEncoder;


    @Override
    public AppUser saveNewUser(String username, String password, String rePassword) {
        //Pour ajouter un user il faut vérifier les deux mdps s'ils sont les memes

        //si les 2 mdps ne sont pas identiques on va générer une exception
        if(!password.equals(rePassword)) throw new RuntimeException("Passwords not match");
        //Sinon on va hacher le mdp
        String hashedPWD= passwordEncoder.encode(password);
        //enregistrer le user dans la bdd
        AppUser appUser=new AppUser();
        //il faut générer un id string
        appUser.setUserID(UUID.randomUUID().toString());
        appUser.setUsername(username);
        appUser.setPassword(hashedPWD);
        appUser.setActive(true);
        AppUser savedAppUser = appUserRepository.save(appUser);
        return savedAppUser;
    }

    @Override
    public AppRole saveNewRole(String roleName, String description) {
        //Pour enregistrer un role il faut vérifier tout d'abord s'il existe ou pas
        AppRole appRole =  appRoleRepository.findByRoleName(roleName);
        if(appRole!=null) throw new RuntimeException("Role " + roleName+" already exist");
        appRole=new AppRole();
        appRole.setRoleName(roleName);
        appRole.setDescription(description);
        AppRole savedAppRole= appRoleRepository.save(appRole);

        return savedAppRole;
    }



    //Lorsqu'on appelle addRoleToUser spring avant d'appeler la methode il va faire begin transaction
    //il va charger le user et le role à partir de la bdd
    //toujours on est dans la meme transaction
    //après lorsqu'on va ajouter le role à la liste des roles de cet user
    //donc à la fin de la méthode il fait commit et automatiquement toutes les modifications éffectuées sur les objet
    //user et role vont etre enresistrées = update

    @Override
    public void addRoleToUser(String username, String roleName) {
        //chercher username
        AppUser appUser = appUserRepository.findByUsername(username);
        //verification
        if(appUser==null) throw new RuntimeException("User not found");
        //chercher roleName
        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        //verification
        if(appRole==null) throw new RuntimeException("Role not found");
        //associer role à user
        appUser.getAppRoles().add(appRole);
        //appUserRepository.save(appUser); // on peut le pas faire

    }

    @Override
    public void removeRoleFromUser(String username, String roleName) {
        //chercher username
        AppUser appUser = appUserRepository.findByUsername(username);
        //verification
        if(appUser==null) throw new RuntimeException("User not found");
        //chercher roleName
        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        //verification
        if(appRole==null) throw new RuntimeException("Role not found");
        //supprimer un role de user
        appUser.getAppRoles().remove(appRole);

    }

    @Override
    public AppUser loadUserByUserName(String username) {

        return appUserRepository.findByUsername(username);
    }
}
