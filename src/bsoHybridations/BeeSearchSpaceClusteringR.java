package bsoHybridations;

import java.util.Arrays;
import bso.Main;
import bso.SatData;
import bso.Solution;
import bso.Swarm;
import bso_dm_2017.FirstController;
import clustering_R.KmeansDiskR;


public class BeeSearchSpaceClusteringR extends Swarm{
	protected int flip; // number of variable to inverse
    private int nbrToCluster;
    private int nbees;
    private int howManyCluster;
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public BeeSearchSpaceClusteringR(SatData satData, int heuristic, int maxIter,
			int nbSearchIter, int nBees, int flip, int chance,int nbrToCluster,int howManyCluster) {
		super(satData, heuristic, maxIter, nbSearchIter, nBees, chance);
		this.flip = flip;
		this.nbrToCluster=nbrToCluster;
		this.nbees=nBees;
		this.howManyCluster=howManyCluster;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public BeeSearchSpaceClusteringR() {
		// TODO Auto-generated constructor stub
	}

    public int getNbrToCluster() {
		return nbrToCluster;
	}

	public void setNbrToCluster(int nbrToCluster) {
		this.nbrToCluster = nbrToCluster;
	}

	public int getNbees() {
		return nbees;
	}

	public void setNbees(int nbees) {	
    this.nbees = nbees;
	}
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void setArea() {
		int i = 0, h = 0, k;

		// first strategy
		while (i < bees.length && i < flip) {
			area[i] = (Solution) solRef.clone();

			for (k = 0; flip * k + h < satData.getVariableNumber(); k++)
				area[i].flip(flip * k + h);
			area[i].setFitness(satData.fitness(area[i].getSolution()));
			i++;
			h++;
		}
		h = 0;
		// second strategy
		while (i < bees.length && i < 2 * flip) {
			area[i] = (Solution) solRef.clone();
			for (k = 0; k < satData.getVariableNumber() / flip; k++)
				area[i].flip((satData.getVariableNumber() / flip) * h + k);
			area[i].setFitness(satData.fitness(area[i].getSolution()));

			i++;
			h++;
		}
		// Third startegy
		while (i < bees.length) {
			int[] tabou = new int[satData.getVariableNumber() / flip];
			area[i] = (Solution) solRef.clone();

			for (int j = 1; j <= satData.getVariableNumber() / flip; j++) {

				k = (int) (Math.random() * satData.getVariableNumber());
				while (existe(k, tabou, j - 2))
					k = (k + 1) % satData.getVariableNumber();

				tabou[j - 1] = k;
				area[i].flip(k);
			}

			i++;
		}

	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void bso() {
		System.out.println("bso clustering with R:");
		int i;
		//int best = 0;

		for (iter = 1; iter <= maxIter && bestSol.getFitness() < satData.getOptimum(); iter++) {
			setArea();
			taboo[iter - 1] = (Solution) solRef.clone(); // putting the reference solution in the taboo list.
			for (i = 0; i < bees.length; i++) {
				bees[i] = new BeeSearchSpaceClusteringBeeR(satData, nbSearchIter, i);
				bees[i].setSol(area[i]);

				((BeeSearchSpaceClusteringBeeR) bees[i]).search(this.getNbrToCluster());
		
				dance[i] = executeKmeans(howManyCluster,satData);
	
			}

			Arrays.sort(dance);
			for(int u=0;u<dance.length;u++)
			{
				System.out.print(dance[u].getFitness()+" ");
			}
			System.out.println("");
			
			for(int u=0;u<taboo.length && taboo[u]!=null;u++){
				System.out.print(taboo[u].getFitness()+" ");
			}
			System.out.println("");

			// saving the bestSol
			if (dance[0].getFitness() > bestSol.getFitness()) {
				bestSol = (Solution) dance[0].clone();
				if (bestSol.getFitness() == satData.getOptimum())
					break;
			}
			// choosing the next solRef
			solRef = bestBee(); 

			// by me
			System.out.println("solRef: " + solRef.getFitness());
			//System.out.println("bestsol = " + bestSol.getFitness() + " optimum: " + satData.getOptimum());

			
			//if (solRef.getFitness() > best)best = bestSol.getFitness();
			lastVal = solRef.getFitness();
		}
		          FirstController.best = bestSol.getFitness();
				}
	
	/////////////////////////////////methodes R
/**
 * Lancer Kmeans avec R et traite les resultats pour retourner le meilleur centre.
 * */
	public Solution executeKmeans(int nbrClusters,SatData satData) { 
		byte [] meilleurCentre=new byte[satData.getVariableNumber()],manageCentres=new byte[satData.getVariableNumber()];
		double [][] tabDesCentres=KmeansDiskR.kmeansWithR(nbrClusters, satData.getVariableNumber(),"\"C:/Users/Vi Incorporated/Documents/NetBeansProjects/Bso_DM_2017/sol.csv\"");
		int meilleureFitness=0;
		// D'abord il faut rendre toutes les elements 1 ou -1.
		for(int i=0;i<tabDesCentres.length;i++)
		{
			for(int j=0;j<satData.getVariableNumber();j++)
			{
				if(tabDesCentres[i][j]>=0 && tabDesCentres[i][j]<=1) tabDesCentres[i][j]=1;
				else tabDesCentres[i][j]=-1;
			}
		}
		
		//Ensuite on evalue tous les centres pour prendre le meilleur.
		for(int i=0;i<tabDesCentres.length;i++)
		{
			for(int j=0;j<satData.getVariableNumber();j++){ //conversion en byte.
				manageCentres[j]=(byte) tabDesCentres[i][j];
			}
			
			if(satData.fitness(manageCentres)>meilleureFitness) 
				{
				meilleureFitness=satData.fitness(manageCentres);
				for(int k=0;k<satData.getVariableNumber();k++)
					meilleurCentre[k]=manageCentres[k];
				}		
		}

		Solution bestSol=new Solution(meilleurCentre,meilleureFitness);
		return bestSol;
	}


}
