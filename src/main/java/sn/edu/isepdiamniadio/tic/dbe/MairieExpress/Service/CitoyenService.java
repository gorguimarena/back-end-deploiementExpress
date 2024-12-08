package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.DocumentEnvoye;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.DemandeRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.DocumentEnvoyeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CitoyenService {

    @Autowired
    private DocumentEnvoyeRepository documentEnvoyeRepository;

    @Autowired
    private DemandeRepository demandeRepository;

    public List<DocumentEnvoye> getDocumentsEnvoyes(Integer citoyenId) {
        return documentEnvoyeRepository.findByCitoyenId(citoyenId);
    }

    public ResponseEntity<List<Demande>> getDemandes(Integer citoyenId) {
        Optional<List<Demande>> demandeList = demandeRepository.findByCitoyenId(citoyenId);
        return demandeList.map(demandes -> ResponseEntity.status(200).body(demandes)).orElseGet(() -> ResponseEntity.status(404).body(null));
    }
}
