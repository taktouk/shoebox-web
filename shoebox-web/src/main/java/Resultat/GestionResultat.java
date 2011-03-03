/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Resultat;

import Login.login;
import ModelesParametrage.StructureCharge;
import ModelesShoebox.CharteCompte;
import ModelesShoebox.Client;
import ModelesShoebox.Commande;
import ModelesShoebox.FournisseurIntrant;
import ModelesShoebox.FournisseurProduit;
import ModelesShoebox.SoldeDepart;
import ModelesShoebox.TransactionCaisse;
import ModelesShoebox.TransactionCharge;
import ModelesShoebox.TransactionMagasin;
import gestionCaisse.GestionCaisse;
import gestionCommandes.gestionCommandes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.inject.Inject;
import parametrageCoop.serviceParamCoopLocal;
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
    private serviceResultatLocal resultat;
    @Inject
    private login session;
    @Inject
    private gestionCommandes gsCommande;
    @Inject
    private GestionCaisse gsCaisse;
    @EJB
    private serviceParamCoopLocal parametrageCoop;
    @Inject
    private gestionSoldeDepart soldeDepart;
    private Client client = new Client();
    private FournisseurProduit fp = new FournisseurProduit();
    private FournisseurIntrant fi = new FournisseurIntrant();

    /** Creates a new instance of GestionResultat */
    public GestionResultat() {
    }

    public List<TransactionCaisse> getetatcompteCoop() {
        List<TransactionCaisse> lstEtatTransactionCoop = new LinkedList<TransactionCaisse>();
        lstEtatTransactionCoop.addAll(resultat.lstTsxCaisseCoop(session.getUser().getCooperative(), session.getCurrentPeriode()));
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

    private void compteApayerEntreeProduit(List<rapportCompte> lstRapportCompte) {
        for (Commande comm : gsCommande.getlstCommandeEntreeProduit()) {
            if(comm.getmontantrestant() != 0){
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
            if(comm.getmontantrestant() != 0){
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
            if(comm.getmontantrestant() != 0){
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
            if(comm.getmontantrestant() != 0){
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
            if(sd.getmontantrestant() != 0){
            rapportCompte rpCompte = new rapportCompte();
            rpCompte.setDate(sd.getDate());
            rpCompte.setReference(sd.getEntite().getReference());
            rpCompte.setDescription("Remboursement solde de depart");
            rpCompte.setEntite(sd.getEntite());
            rpCompte.setMontantPaye(sd.getmontantPaye());
            rpCompte.setMontantTotal(sd.getMontant());
            lstRapportCompte.add(rpCompte);
           }
        }
    }

    public Long ResultatCharge(String periode, String charteCompte){
        if(resultat.listResultatCharge(periode, charteCompte) != null){
        return resultat.listResultatCharge(periode, charteCompte);
        }
        return  null;
    }

    public List<StructureCharge> getlstResultatCharge(){
        return resultat.lsttructureCharge();
    }

    public boolean verifTypeChargeAaffichier(StructureCharge sc){
        if(sc.getReference()!= "ppp" && sc.getRefereaffichage().equals(null));
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
        for (Commande com : gsCommande.getlstCommandeEntreeProduit()) {
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

    public float getquantiteProduitPrincipalEntree() {
        float tot = 0;
        for (Commande comm : gsCommande.getlstCommandeEntreeProduit()) {
            for (TransactionMagasin t : comm.getLsttransactionMagasin()) {
                if (t.getProduit().getCategorie().equals("produitCoop")) {
                    tot = tot + t.getQuantite();
                }
            }
        }
        return tot;
    }

    public float getquantiteProduitSecondairelEntree() {
        float tot = 0;
        for (Commande comm : gsCommande.getlstCommandeEntreeProduit()) {
            for (TransactionMagasin t : comm.getLsttransactionMagasin()) {
                if (t.getProduit().getCategorie().equals("intrant")) {
                    tot = tot + t.getQuantite();
                }
            }
        }
        return tot;
    }

    public float getquantiteProduitSecondaireSortie() {
        float tot = 0;
        for (Commande comm : gsCommande.getLstCommandeSortisProduit()) {
            for (TransactionMagasin t : comm.getLsttransactionMagasin()) {
                if (t.getProduit().getCategorie().equals("intrant")) {
                    tot = tot + t.getQuantite();
                }
            }
        }
        return tot;
    }

    public float getquantiteProduitPrincipaleSortie() {
        float tot = 0;
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
        float tot = 0;
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
            lstEtatTransactionFP.addAll(resultat.lstTsxCaisseClientPourSD(session.getUser().getCooperative(), client, session.getCurrentPeriode()));
            lstEtatTransactionFP.addAll(resultat.lstTsxCaisseClient(session.getUser().getCooperative(), client, session.getCurrentPeriode()));
            return lstEtatTransactionFP;
        }
        return null;
    }

    public List<TransactionCaisse> getetatcompteFP() {
        List<TransactionCaisse> lstEtatTransactionFP = new LinkedList<TransactionCaisse>();
        if (fp.getId() != null) {
            lstEtatTransactionFP.addAll(resultat.lstTsxCaisseFPPourSD(session.getUser().getCooperative(), fp, session.getCurrentPeriode()));
            lstEtatTransactionFP.addAll(resultat.lstTsxCaisseFP(session.getUser().getCooperative(), fp, session.getCurrentPeriode()));
            return lstEtatTransactionFP;
        }
        return null;
    }

    public List<TransactionCaisse> getetatcompteFI() {
        List<TransactionCaisse> lstEtatTransactionFI = new LinkedList<TransactionCaisse>();
        if (fi.getId() != null) {
            lstEtatTransactionFI.addAll(resultat.lstTsxCaisseFIPourSD(session.getUser().getCooperative(), fi, session.getCurrentPeriode()));
            lstEtatTransactionFI.addAll(resultat.lstTsxCaisseFI(session.getUser().getCooperative(), fi, session.getCurrentPeriode()));
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
