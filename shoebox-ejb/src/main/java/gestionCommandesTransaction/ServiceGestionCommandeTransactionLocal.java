/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gestionCommandesTransaction;

import ModelesParametrage.DefinitionPeriode;
import ModelesShoebox.Commande;
import ModelesShoebox.Cooperative;
import ModelesShoebox.Produit;
import ModelesShoebox.TransactionAvanceProduit;
import ModelesShoebox.TransactionCaisse;
import ModelesShoebox.TransactionCharge;
import ModelesShoebox.TransactionMagasin;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author guigam
 */
@Local
public interface ServiceGestionCommandeTransactionLocal {

    public void newCommnde(Commande commande);

    public List<Commande> lstCommande(Cooperative coop, DefinitionPeriode periode);

     public List<Commande> allSortisCommandeProduitPrincipal();
     
     public List<Commande> allEntreeCommandeProduitPrincipal();

     public void mergeCommande(Commande commande);

    public void deleteCommande(Commande commande);

    public List<Commande> lstCommandeByType(String type,Cooperative coop);

    public void mergeTransactionMagasin(TransactionMagasin tsx);

    public List<TransactionCharge> lstCharges(Cooperative coop);
    
    public List<TransactionAvanceProduit> lstAvanceProduit(Cooperative coop);

    public void newTransactionCharge(TransactionCharge transactionCharge);
    
    public void newTransactionAvanceProduit(TransactionAvanceProduit transactionAvanceProduit);

     public Long calculTotalQuantiteProduit();

     public List<TransactionMagasin> transactionByProduit(String typeProduit, Cooperative coop, DefinitionPeriode periode);

//     public List<TransactionMagasin> etatMagasinGroupedByMagasin(Cooperative coop, DefinitionPeriode periode);
//
//     public List<TransactionMagasin> etatMagasinGroupedByProduit(Cooperative coop, DefinitionPeriode periode);

     public List<TransactionMagasin> etatMagByProduit(Cooperative coop, DefinitionPeriode periode);
     

}
