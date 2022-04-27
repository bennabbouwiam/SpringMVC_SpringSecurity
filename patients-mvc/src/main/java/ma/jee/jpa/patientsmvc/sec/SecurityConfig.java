package ma.jee.jpa.patientsmvc.sec;

import ma.jee.jpa.patientsmvc.sec.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
//import sun.security.util.Password;

//classe de configuration
@Configuration
//activer web security
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //injecter le meme datasource de l'app (celui qui est déclaré dans application.properties = datasource courant
    @Autowired
    private DataSource dataSource;

    //injecter
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //on va préciser dans cette méthode comment spring security va chercher les users et les roles

        //les users qui onr le droit d'accéder à l'app seront stockés dans la memoire
        //{noop}: c'est pas la peine d'encoder le mdp (hachage)


        //hacher le mdp
        PasswordEncoder passwordEncoder=passwordEncoder();
         /*
        String encodedPWD= passwordEncoder.encode("1234");
        System.out.println(encodedPWD);

        auth.inMemoryAuthentication()
                //.withUser("user1").password("{noop}1234").roles("USER")
                .withUser("user1").password(encodedPWD).roles("USER")
                .and()
                .withUser("user2").password(passwordEncoder.encode("1111")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder.encode("2345")).roles("USER","ADMIN");

         */

/*
        //jdbcAuthentication

        //les users doivent établir une connexion avec la base de données
        //Quand le user va saisir le username + password va éxécuter cet requette (select ...)
        //? = username saisi par l'utilisateur
        //authoritiesByUsernameQuery(): va charger le user, va comparer le mdp récupéré à partir de la bdd avec le mdp saisi par le user
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username as principal, password as credentials, active from users where username=?")
                .authoritiesByUsernameQuery("select username as principal, role as role from users_roles where username=?")
                .rolePrefix("ROLE_")
                .passwordEncoder(passwordEncoder); // lors de la comparaison on doit préciser à spring security l'algo BCrypt

         */



        //Quand le user va saisir le username et mdp pour s'authentifier
        //spring security va faire appel à l'objet : userDetailsService
        //il va faire appel à la methode : loadUserByUsername
        auth.userDetailsService(userDetailsService);


    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

           //Dans cette méthode on va spécifier les droits d'accès

        //demander à spring security d'utiliser un formulaire d'authentification : formulaire par défaut
        http.formLogin();

        //la page index (url / ) ne nécéssite pas une authentification
        http.authorizeRequests().antMatchers("/").permitAll();


        //seul admin qui peut smatcher entre ces urls (/editPatient**, ...) qui se trouvent dans (/admin/***)
        //http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN");

        //Dans l'objet user (UserDetailsServiceImpl) on a travaillé avec authority et non pas role
        //il ya une différence entre hasRole et hasAuthority dans spring security
        //Dans la structure du spring security "authority" peut avoir plusieurs roles
        http.authorizeRequests().antMatchers("/admin/**").hasAuthority("ADMIN");

        //USER peut acceder à l'url /index qui se trouvent dans (/user/***)
        //http.authorizeRequests().antMatchers("/user/**").hasRole("USER");
        http.authorizeRequests().antMatchers("/user/**").hasAuthority("USER");

        //autoriser les ressources statiques qui nécéssitent pas une authentification
        http.authorizeRequests().antMatchers("/webjars/**").permitAll();

        //error 403
        http.exceptionHandling().accessDeniedPage("/403");


        //gestion des droits d'accès
        //toutes les requetes http nécéssite une authentification
        //http.authorizeRequests().anyRequest().authenticated();




    }

    //au démarrage on construit un objet de type passwordEncoder
    @Bean
    PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
