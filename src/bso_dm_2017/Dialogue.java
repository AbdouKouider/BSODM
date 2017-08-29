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
public class Dialogue {
    
    private int howmanycluster ;
    private int nbrtocluster;
    private int nbrparties;
    private int EFAHMIterations;
    private double support;
    
    
    
    
    
     public Dialogue (int howmanycluster,int nbrtocluster , int nbrparties)
    {
        this.howmanycluster=howmanycluster;
        this.nbrtocluster=nbrtocluster;
        this.nbrparties=nbrparties;
       
    }
     public Dialogue (int howmanycluster,int nbrtocluster )
    {
        this.howmanycluster=howmanycluster;
        this.nbrtocluster=nbrtocluster;
       
    }
     public Dialogue (int howmanyCluster)
     {
         this.howmanycluster=howmanyCluster;
     }
      public Dialogue(int IFAHMIterations, double support)
      {
          this.EFAHMIterations=IFAHMIterations;
          this.support=support;          
      }
     
     /**
     * @return the howmanycluster
     */
    public int getHowmanycluster() {
        return howmanycluster;
    }

    /**
     * @param howmanycluster the howmanycluster to set
     */
    public void setHowmanycluster(int howmanycluster) {
        this.howmanycluster = howmanycluster;
    }

    /**
     * @return the nbrtocluster
     */
    public int getNbrtocluster() {
        return nbrtocluster;
    }

    /**
     * @param nbrtocluster the nbrtocluster to set
     */
    public void setNbrtocluster(int nbrtocluster) {
        this.nbrtocluster = nbrtocluster;
    }

    /**
     * @return the nbrparties
     */
    public int getNbrparties() {
        return nbrparties;
    }

    /**
     * @param nbrparties the nbrparties to set
     */
    public void setNbrparties(int nbrparties) {
        this.nbrparties = nbrparties;
    }
    
}
