package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.*;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private AdminMairieRepository adminMairieRepository;

    @Autowired
    private CitoyenRepository citoyenRepository;

    @Autowired
    private OfficierRepository officierRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private MairieRepository mairieRepository;

    List<Utilisateur> utilisateurList = new ArrayList<>();

    @Autowired
    private AgentRepository agentRepository;


    public ResponseEntity<?> getIdMairie(int idUtilisateur) {
        Optional<AdminMairie> user = adminMairieRepository.findById(idUtilisateur);
        return user.isPresent() ? ResponseEntity.ok().body(user.get().getMairie().getId()) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public List<Utilisateur> getAllPersMairie() {
        List<Officier> list = officierRepository.findAll();
        List<Citoyen> listCitoyen = citoyenRepository.findAll();
        List<AdminMairie> listAdminMairie = adminMairieRepository.findAll();
        utilisateurList.addAll(listAdminMairie);
        utilisateurList.addAll(listCitoyen);
        utilisateurList.addAll(list);
        return utilisateurList;
    }


    public Officier saveSignature(Integer officierId, MultipartFile file) throws IOException {
        Officier officier = officierRepository.findById(officierId).orElseThrow(() -> new RuntimeException("Officier not found"));
        if (file.getSize() > 10485760) { // Par exemple, limite de 10 MB
            throw new RuntimeException("File is too large");
        }
        try {
            officier.setSignature(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error processing file", e);
        }
        return officierRepository.save(officier);
    }


    public byte[] getSignature(Integer officierId) {
        Officier officier = officierRepository.findById(officierId).orElseThrow(() -> new RuntimeException("Officier not found"));
        return officier.getSignature();
    }

    public List<AdminMairie> getAdminMairies() {
        return adminMairieRepository.findAll();
    }

    public List<Object> getUsersMairie(Integer mairieId) {
        Mairie mairie = mairieRepository.findById(mairieId).orElseThrow(() -> new RuntimeException("Mairie not found"));
        Optional<Officier> officiers = officierRepository.findByMairie(mairie);
        Optional<AdminMairie> adminMairies = adminMairieRepository.findByMairie(mairie);
        Optional<Agent> agents = agentRepository.findByMairie(mairie);
        List<Object> allUsers = new ArrayList<>();
        if (agents.isPresent()){
            allUsers.add(agents);
        }
        if (adminMairies.isPresent()){
            allUsers.add(adminMairies);
        }
        if (officiers.isPresent()){
            allUsers.add(officiers);
        }
        return allUsers;
    }
}

