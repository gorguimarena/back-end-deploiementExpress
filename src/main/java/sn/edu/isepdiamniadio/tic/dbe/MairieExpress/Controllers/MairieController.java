package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Mairie;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service.MairieService;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.dto.MairieRequest;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class MairieController {

    @Autowired
    private MairieService mairieService;


    @PostMapping("mairie")
    public Integer createMairie(@RequestBody MairieRequest mairieRequest) {
        return mairieService.createMairie(mairieRequest);
    }

    @GetMapping("mairie")
    public List<Mairie> getMairie() {
        return mairieService.getMairies();
    }

    @GetMapping("mairies/search")
    public ResponseEntity<List<Mairie>> searchMairies(@RequestParam String query) {
        List<Mairie> mairies = mairieService.searchMairies(query);
        return ResponseEntity.ok(mairies);
    }
}
