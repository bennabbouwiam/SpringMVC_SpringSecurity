package ma.jee.jpa.patientsmvc.sec.service;

import groovy.lang.GString;
import ma.jee.jpa.patientsmvc.sec.entities.AppRole;
import ma.jee.jpa.patientsmvc.sec.entities.AppUser;

import java.security.SecureRandom;

public interface SecurityService {

    //quelques methodes pour notre app
    //créer un user avec la vérification du mdp
    AppUser saveNewUser(String username, String password, String rePassword);

    //ajouter un role
    AppRole saveNewRole(String roleName, String description);

    //associer un role à un user
    void addRoleToUser(String username, String roleName);

    //supprimer un role à un user
    void removeRoleFromUser(String username, String roleName);

    //chercher un user
    AppUser loadUserByUserName(String username);



}
