/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parametrageCoop;

import ModelesParametrage.DefinitionPeriode;
import ModelesShoebox.CategorieCharge;
import ModelesShoebox.CharteCompte;
import ModelesShoebox.Client;
import ModelesShoebox.Commande;
import ModelesShoebox.Compte;
import ModelesShoebox.Cooperative;
import ModelesShoebox.FournisseurIntrant;
import ModelesShoebox.FournisseurProduit;
import ModelesShoebox.Magasin;
import ModelesShoebox.Produit;
import ModelesShoebox.TransactionCharge;
import ModelesShoebox.TransactionMagasin;
import java.lang.Object;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author guigam
 */
@Stateless
public class serviceParamCoop implements serviceParamCoopLocal {
//@PersistenceContext(unitName="gestion")
//    EntityManager em;

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestion");
    private EntityManager em = emf.createEntityManager();

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
    public void newClient(Client client) {
        persist(client);
    }

    @Override
    public void newFP(FournisseurProduit fp) {
        persist(fp);
    }

    @Override
    public void newFI(FournisseurIntrant fi) {
        persist(fi);
    }

    @Override
    public void newCoop(Cooperative coop) {
        persist(coop);
    }

    @Override
    public void newCommande(Commande commande) {
        persist(commande);
    }

    @Override
    public void newProduit(Produit produit) {
        persist(produit);
    }

    @Override
    public void newCharteCompte(CharteCompte charteCompte) {
        persist(charteCompte);
    }

    @Override
    public void newMagasin(Magasin magasin) {
        persist(magasin);
    }

    @Override
    public void updateClient(Client client) {
        merge(client);
    }

    @Override
    public void updateFP(FournisseurProduit fp) {
        merge(fp);
    }

    @Override
    public void updateFI(FournisseurIntrant fi) {
        merge(fi);
    }

    @Override
    public void updateCoop(Cooperative coop) {
        merge(coop);
    }

    @Override
    public void updateCommande(Commande commande) {
        merge(commande);
    }

    @Override
    public void updateProduit(Produit produit) {
        merge(produit);
    }

    @Override
    public void updateCharteCompte(CharteCompte charteCompte) {
        merge(charteCompte);
    }

    @Override
    public void updateMagasin(Magasin magasin) {
        merge(magasin);
    }

    @Override
    public void deleteClient(Client client) {
        delete(client);
    }

    @Override
    public void deleteFP(FournisseurProduit fp) {
        delete(fp);
    }

    @Override
    public void deleteFI(FournisseurIntrant fi) {
        delete(fi);
    }

    @Override
    public void deleteCoop(Cooperative coop) {
        delete(coop);
    }

    @Override
    public void deleteCommande(Commande commande) {
        delete(commande);
    }

    @Override
    public void deleteProduit(Produit produit) {
        delete(produit);
    }

    @Override
    public void deleteCharteCompte(CharteCompte charteCompte) {
        delete(charteCompte);
    }

    @Override
    public void deleteMagasin(Magasin magasin) {
        delete(magasin);
    }

    @Override
    public List<Client> lstClient(Cooperative coop) {
        Query query = em.createQuery("from Client c where c.coop = ?1");
        query.setParameter(1, coop);
        return query.getResultList();

    }

    @Override
    public List<FournisseurProduit> lstFP(Cooperative coop) {
        Query query = em.createQuery("from FournisseurProduit f where f.coop= ?1");
        query.setParameter(1, coop);
        return query.getResultList();
    }

    @Override
    public List<FournisseurIntrant> lstFI(Cooperative coop) {
        Query query = em.createQuery("from FournisseurIntrant f where f.coop = ?1");
        query.setParameter(1, coop);
        return query.getResultList();
    }

    @Override
    public List<Produit> lstproduit(Cooperative coop) {
        Query query = em.createQuery("from Produit p where p.categorie = ?1 and p.coop = ?2");
        query.setParameter(1, "produitCoop");
        query.setParameter(2, coop);
        return query.getResultList();
    }

    @Override
    public List<Magasin> lstMagasin(Cooperative coop) {
        Query query = em.createQuery("from Magasin m where m.coop = ?1 ");
        query.setParameter(1, coop);
        return query.getResultList();
    }

    @Override
    public List<Produit> lstproduitIntrant(Cooperative coop) {
        Query query = em.createQuery("from Produit p where p.categorie = ?1 and p.coop = ?2");
        query.setParameter(1, "intrant");
        query.setParameter(2, coop);
        return query.getResultList();
    }

    @Override
    public List<CharteCompte> lstcharteCompte() {
        Query query = em.createQuery("from CharteCompte c ");
        return query.getResultList();
    }

    @Override
    public List<SelectItem> lstItemProduit(Cooperative coop) {
        List<SelectItem> lstproduitItem = new ArrayList<SelectItem>();
        for (Produit p : lstproduit(coop)) {
            lstproduitItem.add(new SelectItem(p, p.getName()));
        }
        return lstproduitItem;
    }

    @Override
    public List<SelectItem> lstItemFP(Cooperative coop) {
        List<SelectItem> lstFPItem = new ArrayList<SelectItem>();
        for (FournisseurProduit p : lstFP(coop)) {
            lstFPItem.add(new SelectItem(p, p.getName()));
        }
        return lstFPItem;
    }

    @Override
    public List<SelectItem> lstItemFI(Cooperative coop) {
        List<SelectItem> lstFIItem = new ArrayList<SelectItem>();
        for (FournisseurIntrant p : lstFI(coop)) {
            lstFIItem.add(new SelectItem(p, p.getName()));
        }
        return lstFIItem;
    }

    @Override
    public List<SelectItem> lstItemProduitIntrant(Cooperative coop) {
        List<SelectItem> lstproduitItemIntrant = new ArrayList<SelectItem>();
        for (Produit p : lstproduitIntrant(coop)) {
            lstproduitItemIntrant.add(new SelectItem(p, p.getName()));
        }
        return lstproduitItemIntrant;
    }

    @Override
    public List<SelectItem> lstItemMagasin(Cooperative coop) {
        List<SelectItem> listMagasin = new ArrayList<SelectItem>();
        for (Magasin p : lstMagasin(coop)) {
            listMagasin.add(new SelectItem(p, p.getName()));
        }
        return listMagasin;
    }

    @Override
    public List<SelectItem> lstitemClient(Cooperative coop) {
        List<SelectItem> listClient = new ArrayList<SelectItem>();
        for (Client c : lstClient(coop)) {
            listClient.add(new SelectItem(c, c.getName()));
        }
        return listClient;
    }

    public List<TransactionMagasin> rechercheStockProduit(Produit produit, Long grade, Cooperative coop, DefinitionPeriode periode) {
        em.clear();
        List<TransactionMagasin> tsxMagasinEncais = new LinkedList<TransactionMagasin>();
        Query q1 = em.createQuery("from TransactionMagasin t where t.m_commande.type in('EPP','EPS') and t.defPeriode = ?1 and t.coop = ?2 and t.produit = ?3 and t.grade = ?4");
        q1.setParameter(1, periode);
        q1.setParameter(2, coop);
        q1.setParameter(3, produit);
        q1.setParameter(4, grade);
        tsxMagasinEncais.addAll(q1.getResultList());
   Query qq = em.createQuery("from TransactionMagasin t where t.type = 1 and t.coop = ?1 and t.defPeriode = ?2 and t.produit = ?3 and t.grade = ?4");
        qq.setParameter(1, coop);
        qq.setParameter(2, periode);
         qq.setParameter(3, produit);
        qq.setParameter(4, grade);
        tsxMagasinEncais.addAll(qq.getResultList());
        List<TransactionMagasin> tsxMagasindebIntrant = new LinkedList<TransactionMagasin>();
        Query q2 = em.createQuery("from TransactionMagasin t where t.m_commande.type in('SPP','SPS') and t.defPeriode = ?1 and t.coop = ?2 and t.produit = ?3 and t.grade = ?4");
        q2.setParameter(1, periode);
        q2.setParameter(2, coop);
        q2.setParameter(3, produit);
        q2.setParameter(4, grade);
        tsxMagasindebIntrant.addAll(q2.getResultList());


        Map<String, TransactionMagasin> mapT = new HashMap<String, TransactionMagasin>();
        if (tsxMagasinEncais != null) {
            for (TransactionMagasin t : tsxMagasinEncais) {
                String key = t.getMagasin().getId() + "_" + t.getProduit().getId() + "_" + t.getGrade();
                TransactionMagasin tm = mapT.get(key);
                if (tm == null) {
                    mapT.put(key, t);
                } else {
                    tm.setQuantite(tm.getQuantite() + t.getQuantite());
                }
            }
        }
        if (tsxMagasindebIntrant != null) {
            for (TransactionMagasin t : tsxMagasindebIntrant) {
                String key = t.getMagasin().getId() + "_" + t.getProduit().getId()+ "_" + t.getGrade();
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

    @Override
    public List<TransactionMagasin> rechercheStockProduitIntrant(Produit p, Cooperative coop, DefinitionPeriode periode) {

        List<TransactionMagasin> tsxMagasinEncaissIntrant = new LinkedList<TransactionMagasin>();
        Query q1 = em.createQuery("from TransactionMagasin t where t.m_commande.type = 'EPI' and t.defPeriode = ?1 and t.coop = ?2 and t.produit = ?3");
        q1.setParameter(1, periode);
        q1.setParameter(2, coop);
        q1.setParameter(3, p);
        tsxMagasinEncaissIntrant.addAll(q1.getResultList());
        List<TransactionMagasin> tsxMagasindebIntrant = new LinkedList<TransactionMagasin>();
        Query q2 = em.createQuery("from TransactionMagasin t where t.m_commande.type = 'SPI' and t.defPeriode = ?1 and t.coop = ?2 and t.produit = ?3");
        q2.setParameter(1, periode);
        q2.setParameter(2, coop);
        q2.setParameter(3, p);
        tsxMagasindebIntrant.addAll(q2.getResultList());


        Map<String, TransactionMagasin> mapT = new HashMap<String, TransactionMagasin>();
        if (tsxMagasinEncaissIntrant != null) {
            for (TransactionMagasin t : tsxMagasinEncaissIntrant) {
                String key = t.getMagasin().getId() + "_" + t.getProduit().getId();
                TransactionMagasin tm = mapT.get(key);
                if (tm == null) {
                    mapT.put(key, t);
                } else {
                    tm.setQuantite(tm.getQuantite() + t.getQuantite());
                }
            }
        }
        if (tsxMagasindebIntrant != null) {
            for (TransactionMagasin t : tsxMagasindebIntrant) {
                String key = t.getMagasin().getId() + "_" + t.getProduit().getId();
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

//    private List<Object[]> rechercehEntreeProduit(Produit produit, Long grade, Cooperative coop, DefinitionPeriode periode) {
//        if (produit != null && grade != 0) {
//            if (produit.getType().equals("Principal")) {
//                Query q = em.createQuery("SELECT  x.magasin, x.produit, x.grade  , SUM(x.quantite) FROM TransactionMagasin x where x.m_commande.type = ?3  and x.produit = ?1 AND x.grade = ?2 and x.coop = ?4 and x.defPeriode = ?5 group by x.magasin");
//                q.setParameter(1, produit);
//                q.setParameter(2, grade);
//                q.setParameter(3, "EPP");
//                q.setParameter(4, coop);
//                q.setParameter(5, periode);
//                return (List<Object[]>) q.getResultList();
//            } else {
//                Query q = em.createQuery("SELECT  x.magasin, x.produit, x.grade  , SUM(x.quantite) FROM TransactionMagasin x where x.m_commande.type = ?3  and x.produit = ?1 AND x.grade = ?2 and x.coop = ?4 and x.defPeriode = ?5 group by x.magasin");
//                q.setParameter(1, produit);
//                q.setParameter(2, grade);
//                q.setParameter(3, "EPS");
//                q.setParameter(4, coop);
//                q.setParameter(5, periode);
//                return (List<Object[]>) q.getResultList();
//            }
//
//        }
//        return null;
//    }

//    private List<Object[]> rechercehSortisProduit(Produit produit, Long grade, Cooperative coop, DefinitionPeriode periode) {
//        if (produit != null && grade != 0) {
//            Query q = em.createQuery("SELECT  x.magasin, x.produit,x.grade  , SUM(x.quantite) FROM TransactionMagasin x where x.m_commande.type in (?3,?5) and x.produit = ?1 AND x.grade = ?2 and x.coop = ?4 and x.defPeriode = ?6 group by x.magasin");
//            q.setParameter(1, produit);
//            q.setParameter(2, grade);
//            q.setParameter(3, "SPP");
//            q.setParameter(5, "SPS");
//            q.setParameter(4, coop);
//            q.setParameter(6, periode);
//            return (List<Object[]>) q.getResultList();
//        }
//        return null;
//    }

    @Override
    public List<SelectItem> lstitemCompte(Cooperative coop) {
        List<SelectItem> listConpte = new ArrayList<SelectItem>();
        for (Compte c : lstCompte(coop)) {
            listConpte.add(new SelectItem(c, c.getNomCompte()));
        }
        return listConpte;
    }

    @Override
    public List<Compte> lstCompte(Cooperative coop) {
        Query query = em.createQuery("from Compte c where c.coop = ?1");
        query.setParameter(1, coop);
        return query.getResultList();
    }

    @Override
    public List<SelectItem> lstItemCharteCompte() {
        List<SelectItem> listCharteCompte = new ArrayList<SelectItem>();
        for (CharteCompte c : lstcharteCompte()) {
            listCharteCompte.add(new SelectItem(c, c.getNom()));
        }
        return listCharteCompte;
    }

    @Override
    public List<CategorieCharge> lstCategorieChargeByType(Cooperative coop, String type) {
        Query query = em.createQuery("from CategorieCharge f where f.cooperative = ?1 and f.type = ?2");
        query.setParameter(1, coop);
        query.setParameter(2, type);
        return query.getResultList();
    }

    @Override
    public List<CategorieCharge> lstCategorieCharge(Cooperative coop) {
        Query query = em.createQuery("from CategorieCharge f where f.cooperative = ?1");
        query.setParameter(1, coop);
        return query.getResultList();
    }

    @Override
    public List<SelectItem> lstItemCategorieCharge(Cooperative coop) {
        List<SelectItem> lstCategItem = new ArrayList<SelectItem>();
        for (CategorieCharge c : lstCategorieCharge(coop)) {
            lstCategItem.add(new SelectItem(c, c.getNomCategorie()));
        }
        return lstCategItem;
    }

    @Override
    public void newCompte(Compte compte) {
        persist(compte);
    }

    @Override
    public boolean verifdate(Date date, DefinitionPeriode defPer) {
        if (defPer.getDateDebut().before(date) && defPer.getDateFin().after(date)) {
            return true;
        }
        return false;
    }

    @Override
    public Double calculCategorie(CategorieCharge categ) {
        double somme = 0;
        Query query = em.createQuery("from TransactionCharge t  where t.categorieCharge = ?1");
        query.setParameter(1, categ);
        List<TransactionCharge> lst = query.getResultList();
        for (TransactionCharge c : lst) {
            somme = somme + c.getMontant();
        }
        return somme;
    }

    @Override
    public Double sommeCharges(Cooperative coop) {
        double somme = 0;
        for (CategorieCharge c : lstCategorieCharge(coop)) {
            somme = somme + calculCategorie(c);
        }
        return somme;
    }

    @Override
    public List<SelectItem> lstItemCoop() {
        List<SelectItem> lstCategItem = new ArrayList<SelectItem>();
        for (Cooperative c : lstCoop()) {
            lstCategItem.add(new SelectItem(c, c.getName()));
        }
        return lstCategItem;
    }

    @Override
    public List<Cooperative> lstCoop() {
        Query query = em.createQuery("from Cooperative");
        return query.getResultList();
    }

    @Override
    public List<Commande> rechercheCommande(String ref, Date datecom) {
        Query query = em.createQuery("from Commande c where c.reference = ?1 or c.dateCommande = ?2 ");
        query.setParameter(1, ref);
        query.setParameter(2, datecom);
        return query.getResultList();
    }
}
