package bsoHybridations;

import java.util.Arrays;

import bso.Bee;
import bso.Main;
import bso.SatData;
import bso.Solution;
import fIM_R.AprioriFIMAlgorithme;
import fIM_R.ItemSupport;

public class FrequentItemsetMiningBeeVerticallyR extends Bee{
	
	public FrequentItemsetMiningBeeVerticallyR(SatData satData, int max, int beeNum) 
	{
		super(satData, max, beeNum);
	}
	
	/**
	 * @return Cette méthode sera Utilisée avant l'extraction des règles d'association (c'est la recherche de bsoClassique).
	 */
	public void searchWithoutFIM() {
		int i,best, k = 0;
		best = sol.getFitness();
		Solution lastSol = sol; // by me
		int lastSolFitness = sol.getFitness(); // by me.
		boolean changement=false;  //by me
		while (k++ < max && changement==false) 
		{
			for (i = 0; i < satData.getVariableNumber(); i++) {
				sol.flip(i);
				sol.setFitness(satData.fitness(sol.getSolution()));
				if (sol.getFitness() > best) {
					changement=true; 
					lastSol = sol; // by me.
					lastSolFitness = sol.getFitness(); // by me.
					best = sol.getFitness();
				}
				else 
				{
					sol.flip(i);
					sol.setFitness(lastSolFitness);
				}
			}
			if(changement == true) changement=false;
			else changement=true;
		}
	}
	
	/**
	 * 
	 * @param bestItemFound
	 * @return Une fois l'item le plus fréquent extrait, cette méthode n'évalue que les solutions qui contiennent cet item.
	 */
	public void searchWithFIM(int [][] bestItemFound)
	{
		int i,best, k = 0;
		best = sol.getFitness();
		Solution lastSol = sol; // by me
		int lastSolFitness = sol.getFitness(); // by me.
		boolean changement=false;  //by me
		while (k++ < max && changement==false) 
		{
			for (i = 0; i < satData.getVariableNumber(); i++) {
				sol.flip(i);
				/////////
				boolean satisfyAll=true;
				int partie=0;
				
				while(partie<FrequentItemsetMiningVerticallyR.nbrParties && satisfyAll)
				{
					//if(AprioriFIMAlgorithme.containAllItems(sol.getSolution(), bestItemFound[partie])) auMoinsUneQuiSatisfait=true;
					if(!AprioriFIMAlgorithme.containAllItems(sol.getSolution(), bestItemFound[partie])) satisfyAll=false;
					partie++;
				}
	
				////////
				if(satisfyAll)
				    {
					
					//System.out.println("satisfy all");
				sol.setFitness(satData.fitness(sol.getSolution()));
				if (sol.getFitness() > best) 
				{
					changement=true; 
					lastSol = sol; // by me.
					lastSolFitness = sol.getFitness(); // by me.
					best = sol.getFitness();
				
				}
				     }
				else 
				{
					//System.out.println("don't satisfy no one.");
					sol.flip(i);
					sol.setFitness(lastSolFitness);
				}
			          
			}
			if(changement == true) changement=false;
			else changement=true;
		}
	
	}
	
	public Solution dance() {
		return sol;
	}

	
	public void setSol(Solution sol) {
		this.sol = sol;
	}

	public Solution getSolution() {
		return this.sol;
	}

}
