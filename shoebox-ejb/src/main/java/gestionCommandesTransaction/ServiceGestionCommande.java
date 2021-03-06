/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionCommandesTransaction;

import ModelesParametrage.DefinitionPeriode;
import ModelesShoebox.Commande;
import ModelesShoebox.Cooperative;
import ModelesShoebox.TransactionAvanceProduit;
import ModelesShoebox.TransactionCharge;
import ModelesShoebox.TransactionMagasin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import parametrageSocodevi.ServiceParamSocoLocal;

/**
 *
 * @author guigam
 */
@Stateless
public class ServiceGestionCommande implements ServiceGestionCommandeTransactionLocal {

    @EJB
    private ServiceParamSocoLocal serviceParamSoco;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestion");
    private EntityManager em = emf.createEntityManager();

//    @PersistenceContext(unitName="gestion")
//    EntityManager em;
    private List<Object[]> entreeProduit(Cooperative coop, DefinitionPeriode periode) {
        Query q = em.createQuery("SELECT  x.magasin, x.produit, x.grade  , SUM(x.quantite) FROM TransactionMagasin x where  x.coop = ?4 and x.defPeriode = ?5 and x.m_commande.type in (?2,?3) group by x.produit");
        q.setParameter(2, "EPS");
        q.setParameter(3, "EPP");
        q.setParameter(4, coop);
        q.setParameter(5, periode);
        return (List<Object[]>) q.getResultList();
    }

    private List<Object[]> sortisProduit(Cooperative coop, DefinitionPeriode periode) {
        Query q = em.createQuery("SELECT  x.magasin, x.produit, x.grade  , SUM(x.quantite) FROM TransactionMagasin x where  x.coop = ?4 and x.defPeriode = ?5 and x.m_commande.type in (?2,?3) group by x.produit");
        q.setParameter(2, "SPS");
        q.setParameter(3, "SPP");
        q.setParameter(4, coop);
        q.setParameter(5, periode);
        return (List<Object[]>) q.getResultList();
    }

    private void persist(Object objet) {
        em.getTransaction().begin();
        em.persist(objet);
        em.getTransaction().commit();
    }

    private void merge(Object objet) {
        em.getTransaction().begin();
        em.merge(objet);
        em.getTransaction().commit();
    }

    private void delete(Object objet) {
        em.getTransaction().begin();
        em.remove(objet);
        em.getTransaction().commit();
    }

    @Override
    public void newCommnde(Commande commande) {
        persist(commande);
    }

    @Override
    public List<Commande> lstCommande(Cooperative coop, DefinitionPeriode periode) {
        Query query = em.createQuery("from Commande c where c.coop = ?1 and c.defPeriode = ?2 ");
        query.setParameter(1, coop);
        query.setParameter(2, periode);
        return query.getResultList();
    }

    @Override
    public List<Commande> allSortisCommandeProduitPrincipal() {
        Query query = em.createQuery("from Commande c where c.type = ?1");
        query.setParameter(1, "SPP");
        return query.getResultList();
    }

    @Override
    public List<Commande> allEntreeCommandeProduitPrincipal() {
        Query query = em.createQuery("from Commande c where c.type = ?1");
        query.setParameter(1, "EPP");
        return query.getResultList();
    }

    @Override
    public void mergeCommande(Commande commande) {
        merge(commande);
    }

    @Override
    public void deleteCommande(Commande commande) {
        Commande commandToDelete = em.find(Commande.class, commande.getId());
        delete(commandToDelete);
    }

    @Override
    public List<Commande> lstCommandeByType(String type, Cooperative coop) {
        Query query = em.createQuery("from Commande c where c.type = ?1 and c.coop = ?2");
        query.setParameter(1, type);
        query.setParameter(2, coop);
        return query.getResultList();
    }

    @Override
    public void mergeTransactionMagasin(TransactionMagasin tsx) {
        merge(tsx);
    }

    @Override
    public List<TransactionCharge> lstCharges(Cooperative coop) {
        Query query = em.createQuery("from TransactionCharge t where t.coop = ?2 ");
        query.setParameter(2, coop);
        return query.getResultList();

    }

    @Override
    public void newTransactionCharge(TransactionCharge transactionCharge) {
        persist(transactionCharge);
    }

    @Override
    public Long calculTotalQuantiteProduit() {
        Query query = em.createQuery("select sum(quantite) from TransactionMagasin t where t.produit.type = ?1");
        query.setParameter(1, "Principal");
        return (Long) query.getSingleResult();
    }

    @Override
    public void newTransactionAvanceProduit(TransactionAvanceProduit transactionAvanceProduit) {
        persist(transactionAvanceProduit);
    }

    @Override
    public List<TransactionAvanceProduit> lstAvanceProduit(Cooperative coop) {
        Query query = em.createQuery("from TransactionAvanceProduit t where t.coop = ?2 ");
        query.setParameter(2, coop);
        return query.getResultList();
    }

    @Override
    public List<TransactionMagasin> transactionByProduit(String typeProduit, Cooperative coop, DefinitionPeriode periode) {
        Query query = em.createQuery("from TransactionMagasin t  where t.produit.type = ?1 and t.coop = ?2 and t.defPeriode = ?3");
        query.setParameter(1, typeProduit);
        query.setParameter(2, coop);
        query.setParameter(3, periode);
        return query.getResultList();
    }

    @Override
    public List<TransactionMagasin> etatMagByProduit(Cooperative coop, DefinitionPeriode periode) {
        em.clear();
        List<TransactionMagasin> lstObject = new LinkedList<TransactionMagasin>();

        List<TransactionMagasin> tsxMagasinEncaiss = new LinkedList<TransactionMagasin>();
        Query q = em.createQuery("from TransactionMagasin t where t.m_commande.type in ('EPP','EPS') and t.coop = ?1 and t.defPeriode = ?2 ");
        q.setParameter(1, coop);
        q.setParameter(2, periode);
        tsxMagasinEncaiss = q.getResultList();
        Query qq = em.createQuery("from TransactionMagasin t where t.type = 1 and t.coop = ?1 and t.defPeriode = ?2");
        qq.setParameter(1, coop);
        qq.setParameter(2, periode);
        tsxMagasinEncaiss.addAll(qq.getResultList());
        List<TransactionMagasin> tsxMagasindeb = new LinkedList<TransactionMagasin>();
        Query q2 = em.createQuery("from TransactionMagasin t where t.m_commande.type in ('SPS', 'SPP') and t.coop = ?1 and t.defPeriode = ?2");
        q2.setParameter(1, coop);
        q2.setParameter(2, periode);
        tsxMagasindeb = q2.getResultList();


        Map<String, TransactionMagasin> mapT = new HashMap<String, TransactionMagasin>();
        if (tsxMagasinEncaiss != null) {
            for (TransactionMagasin t : tsxMagasinEncaiss) {
                String key = t.getMagasin().getId() + "_" + t.getProduit().getId() + "_" + t.getGrade();
                TransactionMagasin tm = mapT.get(key);
                if (tm == null) {
                    mapT.put(key, t);
                } else {
                    tm.setQuantite(tm.getQuantite() + t.getQuantite());
                }
            }
        }
        if (tsxMagasindeb != null) {
            for (TransactionMagasin t : tsxMagasindeb) {
                String key = t.getMagasin().getId() + "_" + t.getProduit().getId() + "_" + t.getGrade();
                TransactionMagasin tm = mapT.get(key);
                if (tm == null) {
                    mapT.put(key, t);
                } else {
                    tm.setQuantite(tm.getQuantite() - t.getQuantite());
                }
            }
        }

        if (mapT != null) {
            List<TransactionMagasin> tmag = new ArrayList(mapT.values());
            return tmag;
        }






        return null;
    }


}
