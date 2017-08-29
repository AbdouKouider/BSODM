/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bso_dm_2017;

/**
 *
 * @author kadero
 */
public class resultat {
    private String Strategie ;
    private int qualite;
    private long temps;

    /**
     * @return the Strategie
     */
    public String getStrategie() {
        return Strategie;
    }

    /**
     * @param Strategie the Strategie to set
     */
    public void setStrategie(String Strategie) {
        this.Strategie = Strategie;
    }

    /**
     * @return the qualite
     */
    public int getQualite() {
        return qualite;
    }

    /**
     * @param qualite the qualite to set
     */
    public void setQualite(int qualite) {
        this.qualite = qualite;
    }

    /**
     * @return the temps
     */
    public long getTemps() {
        return temps;
    }

    /**
     * @param temps the temps to set
     */
    public void setTemps(long temps) {
        this.temps = temps;
    }
    
    public resultat(String strategie, int qualite,long temps )
    {
        this.Strategie=strategie;
        this.qualite=qualite;
        this.temps=temps;
        
    }
    
}
