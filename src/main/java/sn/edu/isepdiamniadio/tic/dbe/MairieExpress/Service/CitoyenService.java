package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Demande;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.DocumentEnvoye;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.DemandeRepository;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.DocumentEnvoyeRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public ResponseEntity<byte[]> getFile(Long id) {
        Optional<DocumentEnvoye> docEnvoyeOpt = documentEnvoyeRepository.findById(id);
        if (docEnvoyeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        DocumentEnvoye documentEnvoye = docEnvoyeOpt.get();
        Path pdfPath = Paths.get(documentEnvoye.getPdfUrl());
        byte[] pdfData;
        try {
            pdfData = Files.readAllBytes(pdfPath);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "demande-" + id + ".pdf");
        return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
    }

    public ResponseEntity<List<Demande>> getDemandes(Integer citoyenId) {
        Optional<List<Demande>> demandeList = demandeRepository.findByCitoyenId(citoyenId);
        return demandeList.map(demandes -> ResponseEntity.status(200).body(demandes)).orElseGet(() -> ResponseEntity.status(404).body(null));
    }
}
