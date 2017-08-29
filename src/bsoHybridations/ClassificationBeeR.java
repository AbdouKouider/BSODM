package bsoHybridations;


import java.util.Arrays;



import toolsToManipulateSolutions.SolutionAndFitness;

import bso.Bee;
import bso.SatData;
import bso.Solution;
import classification_R.ClassificationWithRandomForest;


public class ClassificationBeeR extends Bee{
	
	public ClassificationBeeR(SatData satData, int max, int beeNum) 
	{
		super(satData, max, beeNum);
	}
	
	public void searchWithoutClassification()
	{
		int i,best, k = 0;
		best = sol.getFitness();
		Solution lastSol = sol; 
		int lastSolFitness = sol.getFitness(); 
		boolean changement=false;  

	if(! ClassificationR.memoryProblem) /*** si mémoire suffisante*/
	{
		while (k++ < max && changement==false &&
				!ClassificationR.memoryProblem) /** comme ça même si on sort du for avec le break à cause de la mémoire on va pas réentrer par le while*/
		{
			for (i = 0; i < satData.getVariableNumber(); i++) 
			{
				if(! ClassificationR.memoryProblem)
				{
				sol.flip(i);
				int thisFitness=satData.fitness(sol.getSolution());
			
				//ecriture de la solution et sa fitness à la fin. 
				
				String solution=Arrays.toString(sol.getSolution());solution=solution.replace("[", "");solution=solution.replace("]", "");
				try{
				ClassificationR.beesSolutionsAndFitness.add(new SolutionAndFitness(solution,thisFitness));//mettre la solution et sa fitness dans la liste commune à toutes les abeilles.
				//System.out.println("filling list");
				//System.out.println("heapSize: "+Runtime.getRuntime().totalMemory()/(1024*1024)+"  free: "+Runtime.getRuntime().freeMemory()/(1024*1024)+" Mo");
				
				}
				catch (OutOfMemoryError e)
				{
					 System.out.println("khlassat la ram ........................................................");
					 System.out.println("max: "+Runtime.getRuntime().maxMemory()/(1024*1024)+" heapSize: "+Runtime.getRuntime().totalMemory()/(1024*1024)+"  free: "+Runtime.getRuntime().freeMemory()/(1024*1024)+" Mo");
						
					 ClassificationR.memoryProblem=true;
					 break;
				}
				//////////////////////////////////////////////////////////////////////
				
				sol.setFitness(thisFitness);
				ClassificationR.seuilDesBonnesSolutions+=thisFitness;
				ClassificationR.nbrSolutionsAllBees=ClassificationR.nbrSolutionsAllBees+1;
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
			}
			if(changement == true) changement=false;
			else changement=true;
		}
	}
		
	}

	public void searchWithClassification()
	{
			int i,best, k = 0;
			best = sol.getFitness();
			Solution lastSol = sol; // by me
			int lastSolFitness = sol.getFitness(); // by me.
			boolean changement=false;  //by me
			while (k++ <= max && changement==false) 
			{
				for (i = 0; i < satData.getVariableNumber(); i++) {
					sol.flip(i);

					boolean f=ClassificationWithRandomForest.predictAsolutionClass(sol.getSolution());
					
				
					if(f)
					    {
						
					
					int thisFitness=satData.fitness(sol.getSolution());	
					sol.setFitness(thisFitness);
					if (sol.getFitness() > best) {
						changement=true; 
						lastSol = sol; 
						lastSolFitness = sol.getFitness();
						best = sol.getFitness();
					}
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
