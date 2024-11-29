package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.DocumentEnvoye;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.DocumentEnvoyeRepository;

import java.util.List;

@Service
public class CitoyenService {

    @Autowired
    private DocumentEnvoyeRepository documentEnvoyeRepository;

    public List<DocumentEnvoye> getDocumentsEnvoyes(Integer citoyenId) {
        return documentEnvoyeRepository.findByCitoyenId(citoyenId);
    }
}
