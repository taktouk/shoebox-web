/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ModelesParametrage;

import ModelesShoebox.CharteCompte;
import ModelesShoebox.Cooperative;
import enumerationTransaction.EnumTransaction;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author guigamehdi
 */
@Entity
public class ParamTransaction implements Serializable {
    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String type;
    @Enumerated(EnumType.STRING)
    private EnumTransaction abrev;
    @OneToOne
    private CharteCompte charteCompte;
    @ManyToOne
    private Cooperative coop;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParamTransaction)) {
            return false;
        }
        ParamTransaction other = (ParamTransaction) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ModelesParametrage.ParamTransaction[id=" + getId() + "]";
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the charteCompte
     */
    public CharteCompte getCharteCompte() {
        return charteCompte;
    }

    /**
     * @param charteCompte the charteCompte to set
     */
    public void setCharteCompte(CharteCompte charteCompte) {
        this.charteCompte = charteCompte;
    }

    /**
     * @return the abrev
     */
    public EnumTransaction getAbrev() {
        return abrev;
    }

    /**
     * @param abrev the abrev to set
     */
    public void setAbrev(EnumTransaction abrev) {
        this.abrev = abrev;
    }

    /**
     * @return the coop
     */
    public Cooperative getCoop() {
        return coop;
    }

    /**
     * @param coop the coop to set
     */
    public void setCoop(Cooperative coop) {
        this.coop = coop;
    }


}
