package ma.jee.jpa.patientsmvc;

import ma.jee.jpa.patientsmvc.entities.Patient;
import ma.jee.jpa.patientsmvc.repositories.PatientRepository;
import ma.jee.jpa.patientsmvc.sec.service.SecurityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class PatientsMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatientsMvcApplication.class, args);
    }

    //@Bean
    //=> cela veut dire que cette methode ne sera jamais exécutée au démarrage
    CommandLineRunner commandLineRunner(PatientRepository patientRepository)
    {
        return args -> {
            patientRepository.save(
                    new Patient(null, "Hassan", new Date(), false, 122));

            patientRepository.save(
                    new Patient(null, "Mohammed", new Date(), true, 321));

            patientRepository.save(
                    new Patient(null, "Yasmine", new Date(), true, 165));

            patientRepository.save(
                    new Patient(null, "Hanae", new Date(), false, 132));

            patientRepository.findAll().forEach(p->{
                System.out.println(p.getNom());
            });




        };
    }

    //@Bean
    CommandLineRunner saveUsers(SecurityService securityService)
    {
        //l'app qui va gerer les users et les roles

        return args -> {

            //ajouter des users
            securityService.saveNewUser("mohammed", "1234", "1234");
            securityService.saveNewUser("yasmine", "1234", "1234");
            securityService.saveNewUser("hassan", "1234", "1234");

            //ajouter des roles
            securityService.saveNewRole("USER", "");
            securityService.saveNewRole("ADMIN", "");

            //associer un role à user
            securityService.addRoleToUser("mohammed", "USER");
            securityService.addRoleToUser("mohammed", "ADMIN");
            securityService.addRoleToUser("yasmine", "USER");
            securityService.addRoleToUser("hassan", "USER");

            };




        }
    }


