package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;




import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String typeDocument;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "mairie_id")
    private Mairie mairie;
}