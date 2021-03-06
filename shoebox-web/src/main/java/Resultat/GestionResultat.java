/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Resultat;

import Login.login;
import ModelesParametrage.DefinitionPeriode;
import ModelesParametrage.StructureCharge;
import ModelesParametrage.StructureProduit;
import ModelesShoebox.CategorieCharge;
import ModelesShoebox.Client;
import ModelesShoebox.Commande;
import ModelesShoebox.FournisseurIntrant;
import ModelesShoebox.FournisseurProduit;
import ModelesShoebox.SoldeDepart;
import ModelesShoebox.TransactionCaisse;
import ModelesShoebox.TransactionCharge;
import ModelesShoebox.TransactionMagasin;
import com.gfplus.parametrageShoebox.parametrageShoebox;
import gestionCaisse.GestionCaisse;
import gestionCommandes.gestionCommandes;
import gestionCommandesTransaction.ServiceGestionCommandeTransactionLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.inject.Inject;
import parametrageCoop.serviceParamCoopLocal;
import parametrageSocodevi.GestionParametrageSoco;
import parametrageSocodevi.ServiceParamSocoLocal;
import resultat.serviceResultatLocal;
import soldeDepart.gestionSoldeDepart;

/**
 *
 * @author guigam
 */
@Named(value = "gsResultat")
@RequestScoped
public class GestionResultat implements Serializable {

    @EJB
    private ServiceGestionCommandeTransactionLocal serviceGestionCommande;
    @EJB
    private serviceResultatLocal serviceResultat;
    @Inject
    private login session;
    @Inject
    private gestionCommandes gsCommande;
    @Inject
    private parametrageShoebox paramShoebox;
    @Inject
    private GestionCaisse gsCaisse;
    @EJB
    private serviceParamCoopLocal serviceParametrageCoop;
    @EJB
    private ServiceParamSocoLocal serviceParamSoco;
    @Inject
    private gestionSoldeDepart soldeDepart;
    @Inject
    private GestionParametrageSoco gsParamSoco;
    private Client client = new Client();
    private FournisseurProduit fp = new FournisseurProduit();
    private FournisseurIntrant fi = new FournisseurIntrant();

    /** Creates a new instance of GestionResultat */
    public GestionResultat() {
    }

    public List<TransactionCaisse> getetatcompteCoop() {
        List<TransactionCaisse> lstEtatTransactionCoop = new LinkedList<TransactionCaisse>();
        lstEtatTransactionCoop.addAll(serviceResultat.lstTsxCaisseCoop(session.getUser().getCooperative(), session.getCurrentPeriode()));
        return lstEtatTransactionCoop;
    }

    public List<rapportCompte> getcompteApayer() {
        List<rapportCompte> lstRapportCompte = new LinkedList<rapportCompte>();
        compteApayerEntreeProduit(lstRapportCompte);
        compteApayerEntreeIntrant(lstRapportCompte);
        compteApayerSoldeDepart(lstRapportCompte, soldeDepart.getlstRemboursementClient());
        compteApayerSoldeDepart(lstRapportCompte, soldeDepart.getlstRemboursementFP());
        compteApayerSoldeDepart(lstRapportCompte, soldeDepart.getlstRemboursementFI());
        return lstRapportCompte;
    }

    public List<rapportCompte> getcompteArecevoir() {
        List<rapportCompte> lstRapportCompte = new LinkedList<rapportCompte>();
        compteArecevoirSortisProduit(lstRapportCompte);
        compteArecevoirSortisIntrant(lstRapportCompte);
        compteApayerSoldeDepart(lstRapportCompte, soldeDepart.getlstDetteClient());
        compteApayerSoldeDepart(lstRapportCompte, soldeDepart.getlstDetteFI());
        compteApayerSoldeDepart(lstRapportCompte, soldeDepart.getlstDetteFP());
        return lstRapportCompte;
    }

    public Double calculSommePeriodeCharteCompte(String reference) {
        double calcul = 0;
        try {
            for (DefinitionPeriode d : gsParamSoco.getlstDefinitionPeriode()) {
                calcul = calcul + (double) serviceResultat.rechercheResultat(d.getPeriode(), reference, session.getCurrentCoop());
            }
        } catch (IOException ex) {
            Logger.getLogger(GestionResultat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return calcul;
    }

    private Double calculRW(int periode) {
        return (double) (serviceResultat.rechercheResultat(periode, "RA-1", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "RA-2", session.getCurrentCoop())
                + serviceResultat.rechercheResultat(periode, "RB-1", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "RB-2", session.getCurrentCoop())
                + serviceResultat.rechercheResultat(periode, "RC", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "RD", session.getCurrentCoop())
                + serviceResultat.rechercheResultat(periode, "RD", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "RE", session.getCurrentCoop())
                + serviceResultat.rechercheResultat(periode, "RH", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "RI", session.getCurrentCoop())
                + serviceResultat.rechercheResultat(periode, "RJ", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "RK", session.getCurrentCoop())
                + serviceResultat.rechercheResultat(periode, "RL", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "RP", session.getCurrentCoop())
                + serviceResultat.rechercheResultat(periode, "RQ", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "RS", session.getCurrentCoop()));
    }
    private Double calculTW(int periode) {
        return (double) (serviceResultat.rechercheResultat(periode, "TA-1", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "TA-2", session.getCurrentCoop())
                + serviceResultat.rechercheResultat(periode, "TC", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "TD", session.getCurrentCoop())
                + serviceResultat.rechercheResultat(periode, "TF", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "TE", session.getCurrentCoop())
                + serviceResultat.rechercheResultat(periode, "TH", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "TK", session.getCurrentCoop())
                + serviceResultat.rechercheResultat(periode, "TL", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "TT", session.getCurrentCoop())
                + serviceResultat.rechercheResultat(periode, "TS", session.getCurrentCoop()));
    }

    private Double calculUF(int periode) {
        return (double) (serviceResultat.rechercheResultat(periode, "UA", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "UC", session.getCurrentCoop())
                + serviceResultat.rechercheResultat(periode, "UD", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "UE", session.getCurrentCoop())
                + serviceResultat.rechercheResultat(periode, "UF", session.getCurrentCoop()));
    }
    private Double calculSF(int periode) {
        return (double) (serviceResultat.rechercheResultat(periode, "SA", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "SC", session.getCurrentCoop())
                + serviceResultat.rechercheResultat(periode, "SD", session.getCurrentCoop()));
    }

    private Double calculUO(int periode) {
        return (double) (serviceResultat.rechercheResultat(periode, "UK", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "UL", session.getCurrentCoop())
                + serviceResultat.rechercheResultat(periode, "UM", session.getCurrentCoop())+ serviceResultat.rechercheResultat(periode, "UN", session.getCurrentCoop()));
    }
    private Double calculSO(int periode) {
        return (double) (serviceResultat.rechercheResultat(periode, "SK", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "SL", session.getCurrentCoop())
                + serviceResultat.rechercheResultat(periode, "SM", session.getCurrentCoop()));
    }

    private Double calculSS(int periode) {
        return (double) (serviceResultat.rechercheResultat(periode, "SQ", session.getCurrentCoop()) + serviceResultat.rechercheResultat(periode, "SR", session.getCurrentCoop()));
    }

    private void compteApayerEntreeProduit(List<rapportCompte> lstRapportCompte) {
        for (Commande comm : gsCommande.getLstCommandesEntreProduit()) {
            if (comm.getmontantrestant() != 0) {
                rapportCompte rpCompte = new rapportCompte();
                rpCompte.setDate(comm.getDateCommande());
                rpCompte.setReference(comm.getReference());
                rpCompte.setDescription("Entree Produit");
                rpCompte.setEntite(comm.getM_entite());
                rpCompte.setMontantPaye(comm.getmontantPaye());
                rpCompte.setMontantTotal(comm.getmontantTotal());
                lstRapportCompte.add(rpCompte);
            }
        }
    }

    private void compteArecevoirSortisProduit(List<rapportCompte> lstRapportCompte) {
        for (Commande comm : gsCommande.getLstCommandeSortisProduit()) {
            if (comm.getmontantrestant() != 0) {
                rapportCompte rpCompte = new rapportCompte();
                rpCompte.setDate(comm.getDateCommande());
                rpCompte.setReference(comm.getReference());
                rpCompte.setDescription("Sortis Produit");
                rpCompte.setEntite(comm.getM_entite());
                rpCompte.setMontantPaye(comm.getmontantPaye());
                rpCompte.setMontantTotal(comm.getmontantTotal());
                lstRapportCompte.add(rpCompte);
            }
        }
    }

    private void compteApayerEntreeIntrant(List<rapportCompte> lstRapportCompte) {
        for (Commande comm : gsCommande.getlstCommandeEntreeIntrant()) {
            if (comm.getmontantrestant() != 0) {
                rapportCompte rpCompte = new rapportCompte();
                rpCompte.setDate(comm.getDateCommande());
                rpCompte.setReference(comm.getReference());
                rpCompte.setDescription("Entree Produit Intrant");
                rpCompte.setEntite(comm.getM_entite());
                rpCompte.setMontantPaye(comm.getmontantPaye());
                rpCompte.setMontantTotal(comm.getmontantTotal());
                lstRapportCompte.add(rpCompte);
            }
        }
    }

    private void compteArecevoirSortisIntrant(List<rapportCompte> lstRapportCompte) {
        for (Commande comm : gsCommande.getlstCommandeSortisIntrant()) {
            if (comm.getmontantrestant() != 0) {
                rapportCompte rpCompte = new rapportCompte();
                rpCompte.setDate(comm.getDateCommande());
                rpCompte.setReference(comm.getReference());
                rpCompte.setDescription("Entree Produit Intrant");
                rpCompte.setEntite(comm.getM_entite());
                rpCompte.setMontantPaye(comm.getmontantPaye());
                rpCompte.setMontantTotal(comm.getmontantTotal());
                lstRapportCompte.add(rpCompte);
            }
        }
    }

    private void compteApayerSoldeDepart(List<rapportCompte> lstRapportCompte, List<SoldeDepart> lstSoldeDepart) {
        for (SoldeDepart sd : lstSoldeDepart) {
            if (sd.getmontantrestant() != 0) {
                rapportCompte rpCompte = new rapportCompte();
                rpCompte.setDate(sd.getDate());
                rpCompte.setReference(sd.getEntite().getReference());
                rpCompte.setDescription("Remboursement solde de depart");
                rpCompte.setEntite(sd.getEntite());
                rpCompte.setMontantPaye(sd.getmontantPaye());
                rpCompte.setMontantTotal(sd.getMontantAbsolue());
                lstRapportCompte.add(rpCompte);
            }
        }
    }

    public Double ResultatCharge(int periode, String charteCompte) {
        if (charteCompte.equals("RB-1")) {
            return (double) serviceResultat.rechercheResultat(periode, "RA-1-SD", session.getCurrentCoop()) - (double) serviceResultat.rechercheResultat(periode, "RA-1-SF", session.getCurrentCoop());
        } else if (charteCompte.equals("RB-2")) {
            return (double) serviceResultat.rechercheResultat(periode, "RA-2-SD", session.getCurrentCoop()) - (double) serviceResultat.rechercheResultat(periode, "RA-2-SF", session.getCurrentCoop());
        } else if (charteCompte.equals("RD")) {
            return (double) serviceResultat.rechercheResultat(periode, "RC-SD", session.getCurrentCoop()) - (double) serviceResultat.rechercheResultat(periode, "RC-SF", session.getCurrentCoop());
        } else if (charteCompte.equals("RH")) {
            return (double) serviceResultat.rechercheResultat(periode, "RE-SD", session.getCurrentCoop()) - (double) serviceResultat.rechercheResultat(periode, "RE-SF", session.getCurrentCoop());
        } else if (!charteCompte.equals("ppp") && serviceResultat.rechercheResultat(periode, charteCompte, session.getCurrentCoop()) != 0) {
            return (double) serviceResultat.rechercheResultat(periode, charteCompte, session.getCurrentCoop());
        } else if (charteCompte.equals("RW")) {
            return calculRW(periode);
        } else if (charteCompte.equals("SF")) {
            return calculSF(periode);
        } else if (charteCompte.equals("SH")) {
            return (double) (calculRW(periode) + calculSF(periode));
        } else if (charteCompte.equals("SH")) {
            return calculSO(periode);
        } else if (charteCompte.equals("SS")) {
            return calculSS(periode);
        } else if (charteCompte.equals("ST")) {
            return (double) (calculRW(periode) + calculSF(periode) + calculSO(periode) + calculSS(periode));
        }else if(charteCompte.equals("TW")){
            return calculTW(periode);
        }else if(charteCompte.equals("UF")){
            return calculUF(periode);
        }else if(charteCompte.equals("UH")){
            return (double)(calculTW(periode) + calculUF(periode));
        }else if(charteCompte.equals("UO")){
            return calculUO(periode);
        }else if(charteCompte.equals("UT")){
            return (double)(calculTW(periode) + calculUF(periode)  + calculUO(periode));
        }
        return null;
    }

    //////////////////////// calcul du resultat produit //////////////////////////
    public Double getcalculTB1() {
        return (double) (calculSommePeriodeCharteCompte("TA-1") - calculSommePeriodeCharteCompte("RA-1") - getVariationStock());
    }

    public Double getcalculTB2() {
        return calculSommePeriodeCharteCompte("TA-2") - calculSommePeriodeCharteCompte("RA-2") - (calculSommePeriodeCharteCompte("RA-2-SD") - calculSommePeriodeCharteCompte("RA-2-SF"));
    }

    public Double getcalculTG() {
        return (double) (calculSommePeriodeCharteCompte("TC") + calculSommePeriodeCharteCompte("TD") + calculSommePeriodeCharteCompte("TE") + calculSommePeriodeCharteCompte("TF")
                - calculSommePeriodeCharteCompte("RC") - calculSommePeriodeCharteCompte("RD") - calculSommePeriodeCharteCompte("RE") - calculSommePeriodeCharteCompte("RH"));

    }

    public Double getcalculTN() {
        return getcalculTB1() + getcalculTB2() + getcalculTG() + calculSommePeriodeCharteCompte("TH") + calculSommePeriodeCharteCompte("TK") + calculSommePeriodeCharteCompte("TL")
                - (calculSommePeriodeCharteCompte("RI") + calculSommePeriodeCharteCompte("RJ") + calculSommePeriodeCharteCompte("RK") + calculSommePeriodeCharteCompte("RL"));
    }

    public Double getcalculeTQ() {

        return (double) (getcalculTB1() + getcalculTB2() + getcalculTG() + calculSommePeriodeCharteCompte("TH") + calculSommePeriodeCharteCompte("TK") + calculSommePeriodeCharteCompte("TL")
                - calculSommePeriodeCharteCompte("RI") - calculSommePeriodeCharteCompte("RJ") - calculSommePeriodeCharteCompte("RK") - calculSommePeriodeCharteCompte("RL"));
    }

    public Double getcalculeTX() {
        return (double) (getcalculeTQ() + calculSommePeriodeCharteCompte("TS") + calculSommePeriodeCharteCompte("TT") - calculSommePeriodeCharteCompte("RS"));
    }

    public Double getcalculeUG() {
        return (double)   calculSommePeriodeCharteCompte("UF") - calculSommePeriodeCharteCompte("SF");
    }

    public Double getcalculeUI() {
        return (double) getcalculeTX() + calculSommePeriodeCharteCompte("UG");
    }

    public Double getcalculeUP() {
        return (double)  calculSommePeriodeCharteCompte("UO") - calculSommePeriodeCharteCompte("SO");
    }

    public Double getcalculeUZ() {
        return (double)  getcalculeUI() +  getcalculeUP() - calculSommePeriodeCharteCompte("SS");
    }

//////////////////////////////////////Resultat analyse ////////////////////////////////////////////////
    public Long getcalculQuantiteProduitTotal() {
        return serviceGestionCommande.calculTotalQuantiteProduit();
    }

    public Double getSommeVenteCafeCacao() {
        return (double) calculSommePeriodeCharteCompte("TA-1");
    }

    public Double getSommeAchatProduit() {
        return (double) calculSommePeriodeCharteCompte("RA-1");
    }

    public Double getVariationStock() {
        return (double) calculSommePeriodeCharteCompte("RA-1-SD") - calculSommePeriodeCharteCompte("RA-1-SF");
    }

    public Double getPourcentage1() {
        return (double) calculSommePeriodeCharteCompte("TA-1") / calculSommePeriodeCharteCompte("RA-1");
    }

    public Double calculSommeChargeGroupByCategorie(CategorieCharge categ) {
        return serviceParametrageCoop.calculCategorie(categ);
    }

    public Double coutEtCharge(CategorieCharge categ) {
        return getSommeAchatProduit() + calculSommeChargeGroupByCategorie(categ);
    }

    public Double getmargeBrutSurProduit() {
        return getSommeVenteCafeCacao() - (getSommeAchatProduit()+getVariationStock());
    }

    public Double getmargeBrutExploitation() {
        return getmargeBrutSurProduit() - getsommeFraisExploitation();
    }

    public Double getsommeFraisExploitation() {
        double somme = 0;
        for (CategorieCharge c : paramShoebox.getlstCategorieChargeExploitation()) {
            somme = somme + calculSommeChargeGroupByCategorie(c);
        }
        return somme;
    }

    public Double getsommeFraisCharge() {
        double somme = 0;
        for (CategorieCharge c : paramShoebox.getlstCategorieCharge()) {
            somme = somme + calculSommeChargeGroupByCategorie(c);
        }
        return somme;
    }

    public Double getautresRevenus() {
        return (double) calculSommePeriodeCharteCompte("TA-2") + (double) calculSommePeriodeCharteCompte("TC") + (double) calculSommePeriodeCharteCompte("TD")
                + (double) calculSommePeriodeCharteCompte("TE") + (double) calculSommePeriodeCharteCompte("TF") + (double) calculSommePeriodeCharteCompte("TH")
                + (double) calculSommePeriodeCharteCompte("TK") + (double) calculSommePeriodeCharteCompte("TL") + (double) calculSommePeriodeCharteCompte("TS")
                + (double) calculSommePeriodeCharteCompte("TT") + (double) calculSommePeriodeCharteCompte("UF") + (double) calculSommePeriodeCharteCompte("UO");
    }

    public Double getCoutLie() {
        return (double) 0;
    }

    public Double getresultatConsolide() {
        return (double)getresultatExploitation() + (double)getautresRevenus() - (double)getCoutLie();
    }

    public Double getresultatExploitation(){
        return (double)getmargeBrutExploitation() - (double)getsommeFraisCharge();
    }

    public List<StructureCharge> getlstResultatCharge() {
        return serviceResultat.lsttructureCharge();
    }

    public List<StructureProduit> getlstResultatProduit() {
        return serviceResultat.lsttructureProduit();
    }

    public boolean verifTypeChargeAaffichier(StructureCharge sc) {
        if (!sc.getReference().equals("ppp") && sc.getRefereaffichage() == null);
        return false;
    }

    public float gettotalEntree() {
        float tot = 0;
        for (Commande com : gsCommande.getLstCommandeSortisProduit()) {
            tot = tot + com.getmontantPaye();
        }
        return tot;
    }

    public float gettotalSortis() {
        float tot = 0;
        for (Commande com : gsCommande.getLstCommandesEntreProduit()) {
            tot = tot + com.getmontantPaye();
        }
        return tot;
    }

    public float gettotalCharge() {
        float tot = 0;
        for (TransactionCharge t : gsCaisse.getlstTransactionCharge()) {
            tot = tot + t.getMontant();
        }
        return tot;
    }

    public Long getquantiteProduitPrincipalEntree() {
        long tot = 0;
        for (Commande comm : gsCommande.getLstCommandesEntreProduit()) {
            for (TransactionMagasin t : comm.getLsttransactionMagasin()) {
                if (t.getProduit().getCategorie().equals("produitCoop")) {
                    tot = tot + t.getQuantite();
                }
            }
        }
        return tot;
    }

    public Long getquantiteProduitSecondairelEntree() {
        long tot = 0;
        for (Commande comm : gsCommande.getLstCommandesEntreProduit()) {
            for (TransactionMagasin t : comm.getLsttransactionMagasin()) {
                if (t.getProduit().getCategorie().equals("intrant")) {
                    tot = tot + t.getQuantite();
                }
            }
        }
        return tot;
    }

    public Long getquantiteProduitSecondaireSortie() {
        long tot = 0;
        for (Commande comm : gsCommande.getLstCommandeSortisProduit()) {
            for (TransactionMagasin t : comm.getLsttransactionMagasin()) {
                if (t.getProduit().getCategorie().equals("intrant")) {
                    tot = tot + t.getQuantite();
                }
            }
        }
        return tot;
    }

    public Long getquantiteProduitPrincipaleSortie() {
        long tot = 0;
        for (Commande comm : gsCommande.getLstCommandeSortisProduit()) {
            for (TransactionMagasin t : comm.getLsttransactionMagasin()) {
                if (t.getProduit().getCategorie().equals("produitCoop")) {
                    tot = tot + t.getQuantite();
                }
            }
        }
        return tot;
    }

    public float getquantiteProduitIntrantSortie() {
        long tot = 0;
        for (Commande comm : gsCommande.getlstCommandeSortisIntrant()) {
            tot = tot + comm.getquantiteSortisProduitIntrant();
        }
        return tot;
    }

    public float getquantiteProduitIntrantEntre() {
        float tot = 0;
        for (Commande comm : gsCommande.getlstCommandeEntreeIntrant()) {
            tot = tot + comm.getquantiteEntreeProduitIntrant();
        }
        return tot;
    }

    public List<TransactionCaisse> getetatcompteClient() {
        List<TransactionCaisse> lstEtatTransactionFP = new LinkedList<TransactionCaisse>();
        if (client.getId() != null) {
            lstEtatTransactionFP.addAll(serviceResultat.lstTsxCaisseClientPourSD(session.getUser().getCooperative(), client, session.getCurrentPeriode()));
            lstEtatTransactionFP.addAll(serviceResultat.lstTsxCaisseClient(session.getUser().getCooperative(), client, session.getCurrentPeriode()));
            return lstEtatTransactionFP;
        }
        return null;
    }

    public List<TransactionCaisse> getetatcompteFP() {
        List<TransactionCaisse> lstEtatTransactionFP = new LinkedList<TransactionCaisse>();
        if (fp.getId() != null) {
            lstEtatTransactionFP.addAll(serviceResultat.lstTsxCaisseFPPourSD(session.getUser().getCooperative(), fp, session.getCurrentPeriode()));
            lstEtatTransactionFP.addAll(serviceResultat.lstTsxCaisseFP(session.getUser().getCooperative(), fp, session.getCurrentPeriode()));
            return lstEtatTransactionFP;
        }
        return null;
    }

    public List<TransactionCaisse> getetatcompteFI() {
        List<TransactionCaisse> lstEtatTransactionFI = new LinkedList<TransactionCaisse>();
        if (fi.getId() != null) {
            lstEtatTransactionFI.addAll(serviceResultat.lstTsxCaisseFIPourSD(session.getUser().getCooperative(), fi, session.getCurrentPeriode()));
            lstEtatTransactionFI.addAll(serviceResultat.lstTsxCaisseFI(session.getUser().getCooperative(), fi, session.getCurrentPeriode()));
            return lstEtatTransactionFI;
        }
        return null;
    }

    /**
     * @return the client
     */
    public Client getClient() {
        getetatcompteClient();
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * @return the fp
     */
    public FournisseurProduit getFp() {
        getetatcompteFP();
        return fp;
    }

    /**
     * @param fp the fp to set
     */
    public void setFp(FournisseurProduit fp) {
        this.fp = fp;
    }

    /**
     * @return the fi
     */
    public FournisseurIntrant getFi() {
        return fi;
    }

    /**
     * @param fi the fi to set
     */
    public void setFi(FournisseurIntrant fi) {
        this.fi = fi;
    }
}
