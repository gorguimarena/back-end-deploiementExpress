package sn.edu.isepdiamniadio.tic.dbe.MairieExpress.Models;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.Timer;

@Entity
@Data
public class MariageDocument extends Document {


    private String numeroActeMariage;
    @Column(nullable = false)
    private Date date;
    @Column(nullable = false)
    private LocalDate datemary;

//information epoux
    @Column(nullable = false)
    private String prenomEpoux;
    @Column(nullable = false)
    private String nomEpoux;
    @Column(nullable = false)
    private String naissanceEpoux;
    @Column(nullable = false)
    private String lieunaissanceEpoux;
    @Column(nullable = false)
    private String domicileEpoux;
    @Column(nullable = false)
    private String residenceEpoux;
    @Column(nullable = false)
    private String professionEpoux;
    @Column(nullable = false)
    private String nomparentepoux;
    @Column(nullable = false)
    private String naissanceparentepoux;
    @Column(nullable = false)
    private String lieunaissanceparentepoux;
    @Column(nullable = false)
    private String professionparentepoux;
    @Column(nullable = false)
    private String domicileparent;
    @Column(nullable = true)
    private String marie;


    //information epouse
    @Column(nullable = false)
    private String prenomEpouse;
    @Column(nullable = false)
    private String nomEpouse;
    @Column(nullable = false)
    private String naissanceEpouse;
    @Column(nullable = false)
    private String lieunaissanceEpouse;
    @Column(nullable = false)
    private String domicileEpouse;
    @Column(nullable = false)
    private String residenceEpouse;
    @Column(nullable = false)
    private String professionEpouse;
    @Column(nullable = false)
    private String nomparentepouse;
    @Column(nullable = false)
    private String naissanceparentepouse;
    @Column(nullable = false)
    private String lieunaissanceparentepouse;
    @Column(nullable = false)
    private String professionparentepouse;
    @Column(nullable = false)
    private String domicileparentepouse;
    @Column(nullable = true)
    private String mariprecedent;

    private String opter;
    private Integer dote;
    private String regimeMatrimonial;

//temoin

    private String premiertemoin;
    private String deuxiemetemoin;

    @ManyToOne
    private Demande demande;

}