package ma.jee.jpa.patientsmvc.sec.service;

import ma.jee.jpa.patientsmvc.sec.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service //cette classe c'est un service
public class UserDetailsServiceImpl implements UserDetailsService {

    //injection
    @Autowired
    private SecurityService securityService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //chercher le user à partir de la bdd avec la couche service
        AppUser appUser=securityService.loadUserByUserName(username);

/*
        //pour la collection des roles
        //GrantedAuthority : spring security pour lui le role est un objet iplémentant l'interface GrantedAuthority
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        //on va faire le mapping
        appUser.getAppRoles().forEach(role->{
            //Pour chaque role on doit créer un objet de type GrantedAuthority
            //On prend le role qui se trouve dans la bdd et on le stocke dans un objet de type GrantedAuthority
            //SimpleGrantedAuthority c'est une classe qui implémente l'interface GrantedAuthority
            // et qui permet de présenter le role pour spring security
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName());
            authorities.add(authority);
        });
*/

        //method 2 en utilisant api stream
        Collection<GrantedAuthority> authorities1 =
                appUser.getAppRoles().stream().map(role->new SimpleGrantedAuthority(role.getRoleName())).
                        collect(Collectors.toList());



        //cette methode doit reourner un objet de type UserDetails
        //User : classe propriétaire de spring security
        //transférer les données de appUser vers user
        //la classe User implémente UserDetails de spring security
        //un user a une connexion authorities
        User user = new User(appUser.getUsername(), appUser.getPassword(), authorities1);

        return user;
    }
}
