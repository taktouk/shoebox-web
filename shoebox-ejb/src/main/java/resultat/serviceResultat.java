/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultat;

import ModelesParametrage.DefinitionPeriode;
import ModelesParametrage.StructureCharge;
import ModelesParametrage.StructureProduit;
import ModelesShoebox.CharteCompte;
import ModelesShoebox.Client;
import ModelesShoebox.Commande;
import ModelesShoebox.Cooperative;
import ModelesShoebox.FournisseurIntrant;
import ModelesShoebox.FournisseurProduit;
import ModelesShoebox.SoldeDepart;
import ModelesShoebox.TransactionCaisse;
import ModelesShoebox.TransactionMagasin;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author guigam
 */
@Stateless
public class serviceResultat implements serviceResultatLocal {
private EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestion");
    private EntityManager em = emf.createEntityManager();
//@PersistenceContext(unitName="gestion")
//    EntityManager em;
    @Override
    public List<TransactionCaisse> lstTsxCaisseCoop(Cooperative coop, DefinitionPeriode periode) {
        Query query = em.createQuery( "from TransactionCaisse t where t.coop = ?1 and t.defPeriode = ?2");
        query.setParameter(1, coop);
        query.setParameter(2, periode);
        return query.getResultList();
    }

    @Override
    public List<TransactionCaisse> lstTsxCaisseClient(Cooperative coop,Client cli, DefinitionPeriode periode) {
        List<TransactionCaisse> lstTsxCaisse = new LinkedList<TransactionCaisse>();
        Query query = em.createQuery( "from Commande t where t.m_entite = ?1 and t.coop = ?2 and t.defPeriode = ?3");
        query.setParameter(1, cli);
        query.setParameter(2, coop);
        query.setParameter(3, periode);
        for(Commande comm : (List<Commande>)query.getResultList() ){
            lstTsxCaisse.addAll(comm.getLsttransactionCaisse());
        }
        return lstTsxCaisse;
    }

    @Override
    public List<TransactionCaisse> lstTsxCaisseFP(Cooperative coop, FournisseurProduit FP, DefinitionPeriode periode) {
       List<TransactionCaisse> lstTsxCaisse = new LinkedList<TransactionCaisse>();
        Query query = em.createQuery( "from Commande t where t.m_entite = ?1 and t.coop = ?2 and t.defPeriode = ?3");
        query.setParameter(1, FP);
        query.setParameter(2, coop);
        query.setParameter(3, periode);
        for(Commande comm : (List<Commande>)query.getResultList() ){
            lstTsxCaisse.addAll(comm.getLsttransactionCaisse());
        }
        return lstTsxCaisse;
    }

    @Override
    public List<TransactionCaisse> lstTsxCaisseFI(Cooperative coop, FournisseurIntrant FI, DefinitionPeriode periode) {
        List<TransactionCaisse> lstTsxCaisse = new LinkedList<TransactionCaisse>();
        Query query = em.createQuery( "from Commande t where t.m_entite = ?1 and t.coop = ?2 and t.defPeriode = ?3");
        query.setParameter(1, FI);
        query.setParameter(2, coop);
        query.setParameter(3, periode);
        for(Commande comm : (List<Commande>)query.getResultList() ){
            lstTsxCaisse.addAll(comm.getLsttransactionCaisse());
        }
        return lstTsxCaisse;
    }

    @Override
    public List<TransactionCaisse> lstTsxCaisseFPPourSD(Cooperative coop, FournisseurProduit FP, DefinitionPeriode periode) {
        List<TransactionCaisse> lstTsxCaisse = new LinkedList<TransactionCaisse>();
        Query query = em.createQuery( "from SoldeDepart t where t.entite = ?1 and t.coop = ?2 and t.defPeriode = ?3");
        query.setParameter(1, FP);
        query.setParameter(2, coop);
        query.setParameter(3, periode);
        for(SoldeDepart sd : (List<SoldeDepart>)query.getResultList() ){
            lstTsxCaisse.addAll(sd.getLstTransactionSoldeDepart());
        }
        return lstTsxCaisse;
    }

    @Override
    public List<TransactionCaisse> lstTsxCaisseClientPourSD(Cooperative coop, Client client, DefinitionPeriode periode) {
        List<TransactionCaisse> lstTsxCaisse = new LinkedList<TransactionCaisse>();
        Query query = em.createQuery( "from SoldeDepart t where t.entite = ?1 and t.coop = ?2 and t.defPeriode = ?3");
        query.setParameter(1, client);
        query.setParameter(2, coop);
        query.setParameter(3, periode);
        for(SoldeDepart sd : (List<SoldeDepart>)query.getResultList() ){
            lstTsxCaisse.addAll(sd.getLstTransactionSoldeDepart());
        }
        return lstTsxCaisse;
    }


    @Override
    public List<TransactionCaisse> lstTsxCaisseFIPourSD(Cooperative coop, FournisseurIntrant FI, DefinitionPeriode periode) {
        List<TransactionCaisse> lstTsxCaisse = new LinkedList<TransactionCaisse>();
        Query query = em.createQuery( "from SoldeDepart t where t.entite = ?1 and t.coop = ?2 and t.defPeriode = ?3");
        query.setParameter(1, FI);
        query.setParameter(2, coop);
        query.setParameter(3, periode);
        for(SoldeDepart sd : (List<SoldeDepart>)query.getResultList() ){
            lstTsxCaisse.addAll(sd.getLstTransactionSoldeDepart());
        }
        return lstTsxCaisse;
    }

    @Override
    public double rechercheResultat(int periode, String charteCompte, Cooperative coop) {
       Query query = em.createQuery("select  sum(t.montant) from  TransactionCaisse t "
               + "where t.defPeriode.periode = ?1 and t.charteCompte.reference = ?2 and t.coop = ?3"
                      + " group by t.charteCompte.reference, t.defPeriode.periode");
       query.setParameter(1, periode);
       query.setParameter(2, charteCompte);
       query.setParameter(3, coop);
       if(query.getResultList().isEmpty() ){
        return 0;
       }
        return  (Double) query.getSingleResult();
    }

    @Override
    public List<CharteCompte> lstCharteCompte(String type) {
          Query query = em.createQuery("from CharteCompte c where c. type = ?1 ");
       query.setParameter(1, type);
       return query.getResultList();
    }

    @Override
    public List<StructureCharge> lsttructureCharge() {
       Query query = em.createQuery("from StructureCharge s");
       return query.getResultList();
    }

    @Override
    public List<StructureProduit> lsttructureProduit() {
       Query query = em.createQuery("from StructureProduit p");
       return query.getResultList();
    }

    @Override
   public List<TransactionMagasin> statMagasinByProduit(Cooperative coop, DefinitionPeriode periode){
    Query q = em.createQuery("from TransactionMagasin t where t.coop = ?1 and t.defPeriode = ?2 group by t.produit, t.m_commande.type ");
    q.setParameter(1, coop);
    q.setParameter(2, periode);
    return q.getResultList();
    }


    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public List<TransactionCaisse> statCaisseByProduit(Cooperative coop, DefinitionPeriode periode) {
        Query q = em.createQuery("from TransactionCaisse t where t.coop = ?1 and t.defPeriode = ?2 and t.typeTransaction in('E','D') group by t.typeTransaction ");
    q.setParameter(1, coop);
    q.setParameter(2, periode);
    return q.getResultList();
    }
 
}
