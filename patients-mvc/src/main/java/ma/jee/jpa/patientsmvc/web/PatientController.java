package ma.jee.jpa.patientsmvc.web;

import lombok.AllArgsConstructor;
import ma.jee.jpa.patientsmvc.entities.Patient;
import ma.jee.jpa.patientsmvc.repositories.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.naming.Binding;
import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {

    private PatientRepository patientRepository;

    @GetMapping(path="/user/index")
    //pagination
    public String patients(Model model,
                          //specifier la page et size dans l'url + une val par défaut pour les 2 params
                          @RequestParam(name="page", defaultValue="0") int page,
                           @RequestParam(name="keyword", defaultValue="") String keyword,
                           @RequestParam(name="size", defaultValue="5") int size,
                           @RequestParam(name="score", defaultValue="0") int score){
        Page<Patient> pagePatients = patientRepository.findByNomContainsAndScoreGreaterThan(keyword, score, PageRequest.of(page,size));
        //stocker dans model le contenu de la page : liste des patients
        model.addAttribute("listPatients", pagePatients.getContent());
        //stocker le nbr de page
        model.addAttribute("pages", new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("score", score);
        return "patients";
    }

    //suppression
    @GetMapping(path="/admin/delete")
    public String delete(Long id, String keyword, int page)
    {
        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }


    @GetMapping(path="/")
    public String home()
    {
        return "home";
    }

    //la liste des patients
    @GetMapping(path="/user/patients")
    @ResponseBody
    public List<Patient> listPatients(){
        return patientRepository.findAll();
    }

    //créer un patient
    @GetMapping(path="/admin/formPatients")
    public String formPatients(Model model){
        model.addAttribute("patients", new Patient());
        return "formPatients";
    }

    //validation
    //1- ajouter la dependance spring boot starter validation
    //2- ajouter les annotations appliquées sur les attributs
    //3- ajouter dans le controlleur @valid + bindingResult pour stocker les erreurs
    //4- ajouter dans html th:errors

    //valider pour ajouter ou modifier un patient (action save)
    @PostMapping(path="/admin/save")
    //@Valid = validation avant l'ajout ou modif du patient dans la bdd
    //BindingResult= s'in ly a des erreurs on les stocke dans cette interface
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult,
                       @RequestParam(name="page",defaultValue="0") int page,
                       @RequestParam(name="keyword",defaultValue="") String keyword){
        //verification s'il y a des erreurs on retourne vers le formulaire
        if(bindingResult.hasErrors()) return "formPatients";
        patientRepository.save(patient);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;

    }


    //modifier un patient
    @GetMapping(path="/admin/editPatient")
    public String editPatient(Model model, Long id, String keyword, int page){
        Patient patient= patientRepository.findById(id).orElse(null);
        if(patient==null) throw new RuntimeException("Patient Introuvable");
        model.addAttribute("patients", patient);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword", keyword);
        return "editPatient";
    }
}
