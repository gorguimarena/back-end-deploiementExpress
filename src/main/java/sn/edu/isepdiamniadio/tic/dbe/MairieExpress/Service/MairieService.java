package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Mairie;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto.MairieRequest;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.repository.MairieRepository;

import java.util.List;

@Service
public class MairieService {
    @Autowired
    private MairieRepository mairieRepository;

    public Integer createMairie(MairieRequest mairieRequest) {
        Mairie mairie = Mairie.builder()
                .departement(mairieRequest.getDepartement())
                .commune(mairieRequest.getCommune())
                .nom(mairieRequest.getNom())
                .region(mairieRequest.getRegion())
                .build();

        return mairieRepository.save(mairie).getId();
    }

    public List<Mairie> getMairies() {
        return mairieRepository.findAll();
    }
}

