 package bsoHybridations;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import toolsToManipulateSolutions.SolutionAndFitnessForSSL;

import bso.Bee;
import bso.SatData;
import bso.Solution;



public class SSL_Bee_R extends Bee
{

private boolean memoryProblem=false; /** sera positionné à true si on détectera un "OutOfMemoryError", dans ce cas on stop le remplissage de la liste 'beesSolutionsAndFitness'  **/ 
private int nbrUnlabeled=0; //c'est pour limiter le nombre des solutions non labelisées.

	public SSL_Bee_R(SatData satData, int max, int beeNum) 
	{
		super(satData, max, beeNum);
	}
	
	public void firstSearchWithSSL()
	{
	
		//System.out.println("***************bee*****************");
		int i,best, k = 0;
		best = sol.getFitness();
		Solution lastSol = sol; 
		int lastSolFitness = sol.getFitness(); 
		boolean changement=false;  
		Random d=new Random();
		

	if(!this.memoryProblem) /*** si mémoire suffisante*/
	{
		while (k++ < max && changement==false &&
				!this.memoryProblem) /** comme ça même si on sort du for avec le break à cause de la mémoire on va pas réentrer par le while*/
		{
			for (i = 0; i < satData.getVariableNumber(); i++) 
			{
				if(!this.memoryProblem)
				{
				int thisFitness;
				sol.flip(i);
				/** on génère un nombre aléatoire, et s'il est supérieur à 0.95 on évalue sinon no evaluation*/
				if(d.nextDouble() >=0.7)
				{
				thisFitness=satData.fitness(sol.getSolution());
				}
				else
				{
				thisFitness=-1;
				}
			
				//ecriture de la solution et sa fitness à la fin. 
				
				//String solution=Arrays.toString(sol.getSolution());solution=solution.replace("[", "");solution=solution.replace("]", "");
				try
				{			
					if(thisFitness==-1)
					{
						if(nbrUnlabeled < satData.getVariableNumber())
						{ /**ne pas inscrire tous les unlabeled dans la liste pour gagner dutemps**/
							SSL_R.beesSolutionsAndFitness.add(new SolutionAndFitnessForSSL(sol.getSolution().clone(),thisFitness));//mettre la solution et sa fitness dans la liste commune à toutes les abeilles.
							nbrUnlabeled++;
						}
					
					}
					else
					{
						SSL_R.beesSolutionsAndFitness.add(new SolutionAndFitnessForSSL(sol.getSolution().clone(),thisFitness));//mettre la solution et sa fitness dans la liste commune à toutes les abeilles.
					}
				}
				catch (OutOfMemoryError e)
				{
					 System.out.println("khlassat la ram ........................................................");
					 System.out.println("max: "+Runtime.getRuntime().maxMemory()/(1024*1024)+" heapSize: "+Runtime.getRuntime().totalMemory()/(1024*1024)+"  free: "+Runtime.getRuntime().freeMemory()/(1024*1024)+" Mo");
					 this.memoryProblem=true;
					 break;
				}
				//////////////////////////////////////////////////////////////////////
				
				sol.setFitness(thisFitness);
				if(thisFitness!=-1)
				{
					SSL_R.seuilDesBonnesSolutions+=thisFitness;
					SSL_R.nbrSolutionsOfBee=SSL_R.nbrSolutionsOfBee+1;
				}
			
				
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
			}
			if(changement == true) changement=false;
			else changement=true;
		}
		if(best>SSL_R.bestSolutionFoundInThisIteration) SSL_R.bestSolutionFoundInThisIteration=best;/* pour garantir qu'à la fin de chaque itération
		                                                                                            le best des best sera dans cette variables**/
	}
	

	
		
		/******************************* on a terminé avec cette abeille, il reste maintenant à classifier les solutions non labelisées
		 pour essayer de trouver une solution qui est meilleure que le best courant, et cela en évaluant avec la fonction fitness, 
		 les solutions qui auront un label "bonne" ****************************
		///labeling
		//System.out.println("Starting labeling:");
		labeling(fichierX,fichierY,fichierX_U,this.seuilDesBonnesSolutions/this.nbrSolutionsOfBee,this.beeSolutionsAndFitness);
		//System.out.println("Starting prediction:");
		int [] tabPredictions=RSSL.createSelfLearningModelAndPredictUnlabeledDataClass(fichierX,fichierY,fichierX_U,satData.getVariableNumber());
		//for(int y=0;y<tabPredictions.length;y++) System.out.print(tabPredictions[y]+" ");
		//System.out.println("");
		//System.out.println("end of prediction:");
		/** A ce niveau, après avoir récupérer les prédictions, ce qu'on va faire c'est de parcourir la liste que l'abeille a déjà construit
		 et cela dans le but d'évaluer en utilisant la fct fitness, les solutions qui seront labelisées par "bonne" 
		int indiceParcourTabPredictions=0;
		for(int j=0;j<this.beeSolutionsAndFitness.size();j++)
		{
			if(this.beeSolutionsAndFitness.get(j).getFitness()==-1)
			{ /** on ne s'intéresse qu'aux solutions non labelisées *
				if(indiceParcourTabPredictions<tabPredictions.length && tabPredictions[indiceParcourTabPredictions]==1)
				{ /* qui veut dire bonne**
					
					int fitness=satData.fitness(this.beeSolutionsAndFitness.get(j).getSolution());
					if(fitness>best)
					{
						System.out.println("yeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeees "+best+"  "+fitness);
						sol=new Solution(this.beeSolutionsAndFitness.get(j).getSolution(),fitness);
					}
				}
				indiceParcourTabPredictions++;
			}
			
		}*/
		
	
		
	}

	

	public void nextSearchWithSSL()
	{
	
		//System.out.println("***************bee*****************");
		int i,best, k = 0;
		best = sol.getFitness();
		Solution lastSol = sol; 
		int lastSolFitness = sol.getFitness(); 
		boolean changement=false;  
		Random d=new Random();
		

	if(!this.memoryProblem) /*** si mémoire suffisante*/
	{
		while (k++ < max && changement==false &&
				!this.memoryProblem) /** comme ça même si on sort du for avec le break à cause de la mémoire on va pas réentrer par le while*/
		{
			for (i = 0; i < satData.getVariableNumber(); i++) 
			{
				if(!this.memoryProblem)
				{
				int thisFitness;
				sol.flip(i);
				/** on génère un nombre aléatoire, et s'il est supérieur à 0.95 on évalue sinon no evaluation*/
				if(d.nextDouble() >=0.7)
				{
				thisFitness=satData.fitness(sol.getSolution());
				}
				else
				{
				thisFitness=-1;
				}
			
				//ecriture de la solution et sa fitness à la fin. 
				
				//String solution=Arrays.toString(sol.getSolution());solution=solution.replace("[", "");solution=solution.replace("]", "");
				try
				{			
					if(thisFitness==-1)
					{
						if(nbrUnlabeled < satData.getVariableNumber())
						{ /**ne pas inscrire tous les unlabeled dans la liste pour gagner dutemps**/
							SSL_R.beesSolutionsAndFitness.add(new SolutionAndFitnessForSSL(sol.getSolution().clone(),thisFitness));//mettre la solution et sa fitness dans la liste commune à toutes les abeilles.
							nbrUnlabeled++;
						}
					
					}
					/*else  on n'écrit dans la liste que les unlabeled
					{
						//SSL_R.beesSolutionsAndFitness.add(new SolutionAndFitnessForSSL(sol.getSolution().clone(),thisFitness));//mettre la solution et sa fitness dans la liste commune à toutes les abeilles.
					}*/
				}
				catch (OutOfMemoryError e)
				{
					 System.out.println("khlassat la ram ........................................................");
					 System.out.println("max: "+Runtime.getRuntime().maxMemory()/(1024*1024)+" heapSize: "+Runtime.getRuntime().totalMemory()/(1024*1024)+"  free: "+Runtime.getRuntime().freeMemory()/(1024*1024)+" Mo");
					 this.memoryProblem=true;
					 break;
				}
				//////////////////////////////////////////////////////////////////////
				
				sol.setFitness(thisFitness);
				if(thisFitness!=-1)
				{
					SSL_R.seuilDesBonnesSolutions+=thisFitness;
					SSL_R.nbrSolutionsOfBee=SSL_R.nbrSolutionsOfBee+1;
				}
			
				
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
			}
			if(changement == true) changement=false;
			else changement=true;
		}
		if(best>SSL_R.bestSolutionFoundInThisIteration) SSL_R.bestSolutionFoundInThisIteration=best;/* pour garantir qu'à la fin de chaque itération
		                                                                                            le best des best sera dans cette variables**/
	}
	

	
		
		/******************************* on a terminé avec cette abeille, il reste maintenant à classifier les solutions non labelisées
		 pour essayer de trouver une solution qui est meilleure que le best courant, et cela en évaluant avec la fonction fitness, 
		 les solutions qui auront un label "bonne" ****************************
		///labeling
		//System.out.println("Starting labeling:");
		labeling(fichierX,fichierY,fichierX_U,this.seuilDesBonnesSolutions/this.nbrSolutionsOfBee,this.beeSolutionsAndFitness);
		//System.out.println("Starting prediction:");
		int [] tabPredictions=RSSL.createSelfLearningModelAndPredictUnlabeledDataClass(fichierX,fichierY,fichierX_U,satData.getVariableNumber());
		//for(int y=0;y<tabPredictions.length;y++) System.out.print(tabPredictions[y]+" ");
		//System.out.println("");
		//System.out.println("end of prediction:");
		/** A ce niveau, après avoir récupérer les prédictions, ce qu'on va faire c'est de parcourir la liste que l'abeille a déjà construit
		 et cela dans le but d'évaluer en utilisant la fct fitness, les solutions qui seront labelisées par "bonne" 
		int indiceParcourTabPredictions=0;
		for(int j=0;j<this.beeSolutionsAndFitness.size();j++)
		{
			if(this.beeSolutionsAndFitness.get(j).getFitness()==-1)
			{ /** on ne s'intéresse qu'aux solutions non labelisées *
				if(indiceParcourTabPredictions<tabPredictions.length && tabPredictions[indiceParcourTabPredictions]==1)
				{ /* qui veut dire bonne**
					
					int fitness=satData.fitness(this.beeSolutionsAndFitness.get(j).getSolution());
					if(fitness>best)
					{
						System.out.println("yeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeees "+best+"  "+fitness);
						sol=new Solution(this.beeSolutionsAndFitness.get(j).getSolution(),fitness);
					}
				}
				indiceParcourTabPredictions++;
			}
			
		}*/
		
	
		
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
