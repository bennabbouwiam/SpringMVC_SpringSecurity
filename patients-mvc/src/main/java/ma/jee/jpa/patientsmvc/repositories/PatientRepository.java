package ma.jee.jpa.patientsmvc.repositories;

import ma.jee.jpa.patientsmvc.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Page<Patient> findByNomContainsAndScoreGreaterThan(String kw, int score, Pageable pageable);

}
