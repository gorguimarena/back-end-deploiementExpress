package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models.Notification;
import sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Service.DemandeService;

import java.util.List;

@RestController
@RequestMapping("/notifs")
public class NotificationController {

    @Autowired
    private DemandeService demandeService;

    // Récupérer les notifications d'un citoyen
    @GetMapping("/citoyen/{citoyenId}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Integer citoyenId) {
        List<Notification> notifications = demandeService.getNotificationsByCitoyen(citoyenId);
        return ResponseEntity.ok(notifications);
    }

    // Marquer une notification comme lue
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Integer notificationId) {
        demandeService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().build();
    }
}
