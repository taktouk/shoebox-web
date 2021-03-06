/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionCaisse;

import Login.login;
import ModelesShoebox.TransactionAvanceProduit;
import ModelesShoebox.TransactionCaisse;
import ModelesShoebox.TransactionCharge;
import com.gfplus.parametrageShoebox.parametrageShoebox;
import gestionCommandes.gestionCommandes;
import gestionCommandesTransaction.ServiceGestionCommandeTransactionLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import parametrageSocodevi.ServiceParamSocoLocal;
import serviceSoldeDepart.serviceSoldeDepartLocal;
import soldeDepart.gestionSoldeDepart;

/**
 *
 * @author guigamehdi
 */
@Named(value = "gestionCaisse")
@ConversationScoped
public class GestionCaisse implements Serializable {

    @Inject
    private gestionCommandes gsCommande;
    @Inject
    private gestionSoldeDepart gsSoldeDepart;
    private TransactionCaisse tsxCaisse = new TransactionCaisse();
    private TransactionCharge tsxCaisseCharge = new TransactionCharge();
    private TransactionAvanceProduit tsxCaisseAvanceProduit = new TransactionAvanceProduit();
    private List<TransactionCaisse> lstTsxCaisse = new LinkedList<TransactionCaisse>();
    private String typeRemboursement = null;
    @EJB
    private ServiceGestionCommandeTransactionLocal serviceGsCommande;
    @EJB
    private ServiceParamSocoLocal serviceSoco;
    @EJB
    private serviceSoldeDepartLocal serviceSolde;
    @Inject
    private login session;
    @Inject
    private parametrageShoebox paramShoebox;
    @Inject 
    Conversation conversation;

    /** Creates a new instance of gestionCaisse */
    public GestionCaisse() {
    }

    public String newtransactionCommande() {
        try {
            if (validerTransaction(tsxCaisse.getDate(), gsCommande.getCommade().getDateCommande(), tsxCaisse.getMontant(), gsCommande.getCommade().getmontantrestant())) {
                tsxCaisse.setCharteCompte((serviceSoco.rechercheParamCharteCompte(gsCommande.getCommade().getType(), session.getCurrentCoop())).getCharteCompte());
                tsxCaisse.setDescription((serviceSoco.rechercheParamCharteCompte(gsCommande.getCommade().getType(), session.getCurrentCoop())).getType());
                tsxCaisse.setCurrentuser(session.getUser());
                tsxCaisse.setCoop(session.getCurrentCoop());
                tsxCaisse.setDefPeriode(session.getCurrentPeriode());
                affectationTypetransactionCaisse();
                gsCommande.getCommade().getLsttransactionCaisse().add(tsxCaisse);
                serviceGsCommande.mergeCommande(gsCommande.getCommade());
                conversation.end();
                return "/caisse/menuCaisse.xhtml";
            }
        } catch (IOException ex) {
            Logger.getLogger(GestionCaisse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void payerRestant(){
            tsxCaisse.setMontant(gsCommande.getCommade().getmontantrestant());
    }
    public void resetMontant(){
            tsxCaisse.setMontant(0);
    }
      private Properties loadFilePropriete() throws IOException {
        Properties properties = new Properties();
        properties.load(session.loadLonguage(session.getUser().getLangue()));
        return properties;
    }

      public String addTransactionCaisseRedirect(){
          conversation.begin();
      return "addTransaction";
      }
      public String addTransactionCaisseSDRedirect(){
          conversation.begin();
      return "addTransactionSD";
      }
      
    private boolean validerTransaction(Date datetsx, Date dateOrigine, float montant, float montantRestant) throws IOException {
            Properties  properties = loadFilePropriete();
        if (!paramShoebox.validerDate(datetsx)) {
            return false;
        } else if (montant > montantRestant) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, properties.getProperty("messageMontantSaisie"), null);
            FacesContext.getCurrentInstance().addMessage("montant saisie", msg);
            return false;
        } else if (dateOrigine.after(datetsx)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,  properties.getProperty("messageValiderfdateParApportCommande"),null);
            FacesContext.getCurrentInstance().addMessage("date", msg);
            return false;
        }
        return true;
    }
    private boolean validerTransactionSD(Date datetsx, Date dateOrigine, float montant, float montantRestant) {
        try {
            if (!paramShoebox.validerDate(datetsx)) {
                return false;
            } else if (montant > (montantRestant < 0 ? -montantRestant:montantRestant)) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, loadFilePropriete().getProperty("messageMontantSaisie"), null);
                FacesContext.getCurrentInstance().addMessage("montant saisie", msg);
                return false;
            }
        } catch (IOException ex) {
            Logger.getLogger(GestionCaisse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }


    public String newtransactionSD() throws IOException {
        if (validerTransactionSD(tsxCaisse.getDate(), gsSoldeDepart.getSd().getDate(), tsxCaisse.getMontant(), gsSoldeDepart.getSd().getmontantrestant())) {
            tsxCaisse.setCharteCompte((serviceSoco.rechercheParamCharteCompte(gsSoldeDepart.getSd().getTypeSolde(), session.getCurrentCoop())).getCharteCompte());
            tsxCaisse.setDescription(serviceSoco.rechercheParamCharteCompte(gsSoldeDepart.getSd().getTypeSolde(), session.getCurrentCoop()).getType());
            tsxCaisse.setCurrentuser(session.getUser());
            tsxCaisse.setCoop(session.getCurrentCoop());
            tsxCaisse.setDefPeriode(session.getCurrentPeriode());
            affectationTypetransactionSD();
            gsSoldeDepart.getSd().getLstTransactionSoldeDepart().add(tsxCaisse);
            serviceSolde.mergeSolde(gsSoldeDepart.getSd());
            conversation.end();
            return "/caisse/menuCaisse.xhtml";
        }
        return null;
    }

    private void affectationTypetransactionSD() {

        if (gsSoldeDepart.getSd().getTypeSolde().equals("rmbFP") || gsSoldeDepart.getSd().getTypeSolde().equals("rmbFI") || gsSoldeDepart.getSd().getTypeSolde().equals("rmbClient")) {
            tsxCaisse.setTypeTransaction("D");
        } else {
            tsxCaisse.setTypeTransaction("E");
        }
    }

    private void affectationTypetransactionCaisse() {
        if (gsCommande.getCommade().getType().equals("EPP") || gsCommande.getCommade().getType().equals("EPS") || gsCommande.getCommade().getType().equals("EPI")) {
            tsxCaisse.setTypeTransaction("D");
        } else {
            tsxCaisse.setTypeTransaction("E");
        }
    }

    public String newTransactionCharge() throws IOException {
        if (paramShoebox.validerDate(tsxCaisseCharge.getDate())) {
            tsxCaisseCharge.setTypeTransaction("D");
            tsxCaisseCharge.setCurrentuser(session.getUser());
            tsxCaisseCharge.setCoop(session.getCurrentCoop());
            tsxCaisseCharge.setDefPeriode(session.getCurrentPeriode());
            serviceGsCommande.newTransactionCharge(tsxCaisseCharge);
            tsxCaisseCharge = new TransactionCharge();
            return "lstTransactionCharge";
        }
        return null;
    }
    public String newTransactionAvanceproduit() throws IOException {
        if(paramShoebox.validerDate(tsxCaisseAvanceProduit.getDate())) {
            tsxCaisseAvanceProduit.setTypeTransaction("E");
            tsxCaisseAvanceProduit.setCurrentuser(session.getUser());
            tsxCaisseAvanceProduit.setCoop(session.getCurrentCoop());
            tsxCaisseAvanceProduit.setDefPeriode(session.getCurrentPeriode());
            serviceGsCommande.newTransactionAvanceProduit(tsxCaisseAvanceProduit);
            tsxCaisseAvanceProduit = new TransactionAvanceProduit();
            return "/caisse/addAvanceProduit.xhtml";
        }
        return null;
    }

    public List<TransactionCharge> getlstTransactionCharge() {
        return serviceGsCommande.lstCharges(session.getCurrentCoop());
    }
    
    public List<TransactionAvanceProduit> getlstTransactionAvanceProduit() {
        return serviceGsCommande.lstAvanceProduit(session.getCurrentCoop());
    }

    /**
     * @return the tsxCiasse
     */
    public TransactionCaisse getTsxCaisse() {
        return tsxCaisse;
    }

    /**
     * @param tsxCiasse the tsxCiasse to set
     */
    public void setTsxCaisse(TransactionCaisse tsxCiasse) {
        this.tsxCaisse = tsxCiasse;
    }

    /**
     * @return the lstTsxCaisse
     */
    public List<TransactionCaisse> getLstTsxCaisse() {
        return lstTsxCaisse;
    }

    /**
     * @param lstTsxCaisse the lstTsxCaisse to set
     */
    public void setLstTsxCaisse(List<TransactionCaisse> lstTsxCaisse) {
        this.lstTsxCaisse = lstTsxCaisse;
    }

    /**
     * @return the tsxCaisseCharge
     */
    public TransactionCharge getTsxCaisseCharge() {
        return tsxCaisseCharge;
    }

    /**
     * @param tsxCaisseCharge the tsxCaisseCharge to set
     */
    public void setTsxCaisseCharge(TransactionCharge tsxCaisseCharge) {
        this.tsxCaisseCharge = tsxCaisseCharge;
    }

    /**
     * @return the tsxCaisseAvanceProduit
     */
    public TransactionAvanceProduit getTsxCaisseAvanceProduit() {
        return tsxCaisseAvanceProduit;
    }

    /**
     * @param tsxCaisseAvanceProduit the tsxCaisseAvanceProduit to set
     */
    public void setTsxCaisseAvanceProduit(TransactionAvanceProduit tsxCaisseAvanceProduit) {
        this.tsxCaisseAvanceProduit = tsxCaisseAvanceProduit;
    }

    /**
     * @return the typeRemboursement
     */
    public String getTypeRemboursement() {
        return typeRemboursement;
    }

    /**
     * @param typeRemboursement the typeRemboursement to set
     */
    public void setTypeRemboursement(String typeRemboursement) {
        this.typeRemboursement = typeRemboursement;
         if(typeRemboursement.equals("Payer montant restant")){
            tsxCaisse.setMontant(gsCommande.getCommade().getmontantrestant());
        }else{
        tsxCaisse.setMontant(0);
        }
    }

    
}
