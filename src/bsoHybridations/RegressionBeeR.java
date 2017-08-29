package bsoHybridations;

import java.util.Arrays;

import regression_R.RegressionWithRandomForest;
import toolsToManipulateSolutions.SolutionAndFitness;

import bso.Bee;
import bso.SatData;
import bso.Solution;



public class RegressionBeeR extends Bee{
	
	public RegressionBeeR(SatData satData, int max, int beeNum) 
	{
		super(satData, max, beeNum);
	}
	
	public void searchWithoutRegression()
	{
		int i,best, k = 0;
		best = sol.getFitness();
		Solution lastSol = sol; 
		int lastSolFitness = sol.getFitness(); 
		boolean changement=false;  


	if(! RegressionR.memoryProblem) /*** si mémoire suffisante*/
	{
		
		while (k++ < max && changement==false &&
				!RegressionR.memoryProblem) /** comme ça même si on sort du for avec le break à cause de la mémoire on va pas réentrer par le while*/
		{
			for (i = 0; i < satData.getVariableNumber(); i++) 
			{
				if(! RegressionR.memoryProblem)
				{
				sol.flip(i);
				int thisFitness=satData.fitness(sol.getSolution());
			
				//ecriture de la solution et sa fitness à la fin. 
				
				String solution=Arrays.toString(sol.getSolution());solution=solution.replace("[", "");solution=solution.replace("]", "");
				try{
				RegressionR.beesSolutionsAndFitness.add(new SolutionAndFitness(solution,thisFitness));//mettre la solution et sa fitness dans la liste commune à toutes les abeilles.
				//System.out.println("filling list");
				//System.out.println("heapSize: "+Runtime.getRuntime().totalMemory()/(1024*1024)+"  free: "+Runtime.getRuntime().freeMemory()/(1024*1024)+" Mo");
				
				}
				catch (OutOfMemoryError e)
				{
					 System.out.println("khlassat la ram ........................................................");
					 System.out.println("max: "+Runtime.getRuntime().maxMemory()/(1024*1024)+" heapSize: "+Runtime.getRuntime().totalMemory()/(1024*1024)+"  free: "+Runtime.getRuntime().freeMemory()/(1024*1024)+" Mo");
						
					RegressionR.memoryProblem=true;
					 break;
				}
				//////////////////////////////////////////////////////////////////////
				
				sol.setFitness(thisFitness);
				RegressionR.seuilDesBonnesSolutions+=thisFitness;
				RegressionR.nbrSolutionsAllBees=RegressionR.nbrSolutionsAllBees+1;
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

	public void searchWithRegression()
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
					double predictedFitness=RegressionWithRandomForest.predictAsolutionClass(sol.getSolution());
					
					sol.setFitness((int) predictedFitness);
					if (sol.getFitness() > best)
					{
						changement=true; 
						lastSol = sol; 
						lastSolFitness = sol.getFitness();
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
			/** a ce niveau l'abeille est sur le point de retourner sa meilleure solution à la table dance, donc il faut corriger les
			 * choses et retourner la bonne fitness et non pas une fitness approchée (de la régression)**/
			
			int realFitness=satData.fitness(sol.getSolution());
			System.out.println("it was: "+sol.getFitness()+" it becomes: "+realFitness);
			sol.setFitness(realFitness);
			
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
/*
/***
 * 
 * @param clauses
 * @return cette méthode crée l'ensemble de clauses dans l'environnement R.
 */
	/*
	public static void createClausesInR(ArrayList<Clause> clauses,RConnection connection)
	{

		try
		{
			
			for (int i = 0; i < clauses.size(); i++) 
			{
				String clause="c"+i+" <- c(";
				for (int j = 0; j < clauses.get(i).length(); j++) 
				{
					if(j!=clauses.get(i).length()-1) clause=clause+clauses.get(i).getLiteral(j)+",";
					else clause=clause+clauses.get(i).getLiteral(j);
				}
				clause=clause+")";
				connection.eval(clause);//on fait entrer les clauses en R.
	       
			}
			String allClauses="clauses <- list (";
			for(int i=0;i<clauses.size();i++)
			{
				if(i!=clauses.size()-1) allClauses=allClauses+"c"+i+",";
				else allClauses=allClauses+"c"+i;
			}
			allClauses=allClauses+")";
			connection.eval(allClauses);
			
			
		} catch (RserveException e) 
		{
		
			e.printStackTrace();
		}
	
	}
    
	/**
	 * 
	 * @param sol
	 * @return cette méthode prend une solution et la crée dans l'environnement R.
	 */
	/*
	public static void createSolutionInR(Solution sol,RConnection connection,int nbrVar)
	{
		String solution=Arrays.toString(sol.getSolution());solution=solution.replace("[", "");solution=solution.replace("]", "");
		try
		{
			solution="solution <- as.data.frame(matrix(c("+solution+"),nrow=1,ncol="+nbrVar+"))";
		    connection.eval(solution);
			
		} 
		catch (RserveException e) 
		{
			
			e.printStackTrace();
		}
		
	}
*/
}
