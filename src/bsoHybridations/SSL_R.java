 package bsoHybridations;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;

import javax.xml.transform.Source;

import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.xml.sax.InputSource;

import bso.Main;
import bso.SatData;
import bso.Solution;
import bso.Swarm;
import bso_dm_2017.FirstController;

import semiSupervised_R.RSSL;
import toolsToManipulateSolutions.SolutionAndFitnessForSSL;



public class SSL_R extends Swarm{
	  
		protected int flip; // number of variable to inverse
		public static  int seuilDesBonnesSolutions=0;///ici on sommes les fitness évaluées
		public static int nbrSolutionsOfBee=0;    ///ici on somme le nombre de fitness évaluées.
	    private String fichierX, fichierY, fichierX_U;
	    public static LinkedList <SolutionAndFitnessForSSL> beesSolutionsAndFitness=new LinkedList <SolutionAndFitnessForSSL> (); //C'est la liste qui contient et les solutions et leurs fitness de  l'abeille, elle sera utilisée dans l'étape du labeling. 
        public static int bestSolutionFoundInThisIteration=0; /** à chaque itération les abeilles mettent ici leurs best s'il est meilleur que le best actuel**/
		
       public static  RConnection connection = null;
        
        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		public SSL_R(SatData satData, int heuristic, int maxIter,
				int nbSearchIter, int nBees, int flip, int chance,String cheminFichierX,String cheminFichierY,String cheminFichierX_U) 
		{
			super(satData, heuristic, maxIter, nbSearchIter, nBees, chance);
			this.flip = flip;
			this.fichierX=cheminFichierX;
			this.fichierY=cheminFichierY;
			this.fichierX_U=cheminFichierX_U;
		}

		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		public SSL_R() {
			// TODO Auto-generated constructor stub
		}

		// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		public void print() {
			System.out.println(maxIter + " | " + nbSearchIter + " | " + bees.length
					+ " | " + chance + " | " + flip);

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
		System.out.println("bso  SSL");
		int i;
		
		try {
			connection=new RConnection();
			} catch (RserveException e) {
				e.printStackTrace();
		}
		for (iter = 1; iter <= maxIter && bestSol.getFitness() < satData.getOptimum(); iter++) 
		{  
		bestSolutionFoundInThisIteration=0;
		 seuilDesBonnesSolutions=0;
			nbrSolutionsOfBee=0;
					
					setArea();
					taboo[iter - 1] = (Solution) solRef.clone();			
					for (i = 0; i < bees.length; i++)
					{
						bees[i] = new SSL_Bee_R(satData, nbSearchIter, i);
						bees[i].setSol(area[i]);
						if(iter==1)((SSL_Bee_R) bees[i]).firstSearchWithSSL( );
						else  ((SSL_Bee_R) bees[i]).nextSearchWithSSL( );
						dance[i] = bees[i].dance();
					}
					Arrays.sort(dance);
		
					////////////////////////////////////////////////////////////////////
					if(iter==1){
						System.out.println("First SSL itération");
				labeling(fichierX,fichierY,fichierX_U,seuilDesBonnesSolutions/nbrSolutionsOfBee);
				int [] tabPredictions=RSSL.createSelfLearningModelAndPredictUnlabeledDataClass(fichierX,fichierY,fichierX_U,satData.getVariableNumber());
						/** A ce niveau, après avoir récupérer les prédictions, ce qu'on va faire c'est de parcourir la liste commune à toutes les abeilles,
						 *  que les abeilles ont déjà construit et cela dans le but d'évaluer en utilisant la fct fitness, 
						 *  les solutions qui seront labelisées par "bonne" */
						int indiceParcourTabPredictions=0;
						for(int j=0;j<beesSolutionsAndFitness.size();j++)
						{
							if(beesSolutionsAndFitness.get(j).getFitness()==-1)
							{ /** on ne s'intéresse qu'aux solutions non labelisées **/
								if(indiceParcourTabPredictions<tabPredictions.length && tabPredictions[indiceParcourTabPredictions]==1)
								{ /* qui veut dire bonne**/
									
									int fitness=satData.fitness(beesSolutionsAndFitness.get(j).getSolution());
									if(fitness>bestSolutionFoundInThisIteration)
									{
										System.out.println("yeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeees "+bestSolutionFoundInThisIteration+"  "+fitness);
										Solution sol=new Solution(this.beesSolutionsAndFitness.get(j).getSolution(),fitness);
										dance[0]=sol;
									}
								}
								indiceParcourTabPredictions++;
							}
							
						}
						beesSolutionsAndFitness=new LinkedList <SolutionAndFitnessForSSL> ();
					}
					else{
						System.out.println("other SSL itérations");
						unlabeledLabeling(fichierX_U);
						int [] tabPredictions=RSSL.predictUnlabeledDataClass(fichierX_U,satData.getVariableNumber());
						int indiceParcourTabPredictions=0;
						for(int j=0;j<beesSolutionsAndFitness.size();j++)
						{
							if(beesSolutionsAndFitness.get(j).getFitness()==-1)
							{ /** on ne s'intéresse qu'aux solutions non labelisées **/
								if(indiceParcourTabPredictions<tabPredictions.length && tabPredictions[indiceParcourTabPredictions]==1)
								{ /* qui veut dire bonne**/
									
									int fitness=satData.fitness(beesSolutionsAndFitness.get(j).getSolution());
									if(fitness>bestSolutionFoundInThisIteration)
									{
										System.out.println("yeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeees "+bestSolutionFoundInThisIteration+"  "+fitness);
										Solution sol=new Solution(beesSolutionsAndFitness.get(j).getSolution(),fitness);
										dance[0]=sol;
									}
								}
								indiceParcourTabPredictions++;
							}
							
						}
						beesSolutionsAndFitness=new LinkedList <SolutionAndFitnessForSSL> ();
					}
						
					////////////////////////////////////////////////////////////////////

					for(int u=0;u<dance.length;u++){
						System.out.print(dance[u].getFitness()+" ");
					}
					System.out.println("");

					if (dance[0].getFitness() > bestSol.getFitness()) {
						bestSol = (Solution) dance[0].clone();
						if (bestSol.getFitness() == satData.getOptimum())
							break;
					}
					// choosing the next solRef
					solRef = bestBee();
					
	                /*by me because i note that bestSol is not the real best, there is some solRef best than the final best sol*/
					if (solRef.getFitness()> bestSol.getFitness())
						bestSol=solRef;
					/*end of by me*/
	            System.out.println("solRef "+solRef.getFitness());
					lastVal = solRef.getFitness();
				}
				//by me
				System.out.println("best: "+bestSol.getFitness());
				FirstController.best=bestSol.getFitness();
				//by me

		
connection.close();
		}

	
		/***************************************************************/
		/**
		 * Cette méthode va créer 3 fichiers contenant les solutions ainsi que leurs classe ('bonne' ou 'mauvaise') en utilisant une liste contenant
		 *  les solutions et leurs fitness et en utilisant un seuil, de plus un fichier des solutions sans labels
		 *  les fichiers de sortie sont les mêmes que ceux d'entrée, ils rentrent vide et ils sortent remplits.
		 */
		public void labeling(String fichierX,String fichierY,String fichierX_U, float seuil)
		{
			PrintWriter sortieX = null,sortieY = null,sortieX_U = null;
			/***sortieX et sortieY:c'est les solutions qui ont un labels, les solutions seulement dans sortieX et leurs labels dans sortieY */
			try {
				sortieX = new PrintWriter(new BufferedWriter (new FileWriter(fichierX, false)));
				sortieY = new PrintWriter(new BufferedWriter (new FileWriter(fichierY, false)));
				sortieX_U = new PrintWriter(new BufferedWriter (new FileWriter(fichierX_U, false)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			///////////////////////////////////////////////////////////////
			//System.out.println("moyenne des fitness: "+seuil);
	      
	      
	       /****A fin de garantir une égalité entre les 2 classes "bonne" et "mauvaise" il faut que le nbr de solutions des 2 classes
	        * soient égaux donc on parcours d'abord la list pour compter le nbr de ceux qui vont devenir bonnes et mauvaises
	        * et on écrit dans le fichier le nbr minimum de ces 2 classes: ex s'il y a 20 bonnes et 5 mauvaises on écrira 5 pour les 2 classes. 
	        */
			 int nbrBonnes=0,nbrMauvaises=0,nbrUnlabeled=0;
		      for(int i=0;i<beesSolutionsAndFitness.size();i++)
				{
		    	  if(beesSolutionsAndFitness.get(i).getFitness()>seuil) nbrBonnes++;
		    	  else if(beesSolutionsAndFitness.get(i).getFitness()!=-1) nbrMauvaises++;
		    	  else nbrUnlabeled++;
				}
		     // System.out.println("il y a: "+nbrBonnes+" bonnes solutions et :"+nbrMauvaises+" mauvaises"+nbrUnlabeled+" unlabeled");
		      int nbrWritedBonnes=0,nbrWritedMauvaises=0,minimalClasseSolutionNumber=0;  
		      
		      if(nbrMauvaises<nbrBonnes) minimalClasseSolutionNumber=nbrMauvaises;
		      else                       minimalClasseSolutionNumber=nbrBonnes;
		     
		      
		      if(minimalClasseSolutionNumber>200) minimalClasseSolutionNumber=200;
		      
		      //System.out.println("le minimum entre les 2 est: "+minimalClasseSolutionNumber);
		      
		      
		      
			
	      int indice=0;
			while( (indice<beesSolutionsAndFitness.size()) && (nbrWritedBonnes<minimalClasseSolutionNumber || nbrWritedMauvaises<minimalClasseSolutionNumber) )
			{
				int fitness=beesSolutionsAndFitness.get(indice).getFitness();
				if(fitness==-1)
				{
							byte [] soll=beesSolutionsAndFitness.get(indice).getSolution();
							String solution=Arrays.toString(soll);solution=solution.replace("[", "");solution=solution.replace("]", "");
							sortieX_U.println(solution);
		
					
				}
				else
				{
				if(fitness>seuil) 
					{
					nbrWritedBonnes++;
					if(nbrWritedBonnes<=minimalClasseSolutionNumber)
						{
							byte [] soll=beesSolutionsAndFitness.get(indice).getSolution();
							String solution=Arrays.toString(soll);solution=solution.replace("[", "");solution=solution.replace("]", "");
							sortieX.println(solution);
							sortieY.println("bonne");
						}
							
					}
				
				else
					{
					nbrWritedMauvaises++;
					if(nbrWritedMauvaises<=minimalClasseSolutionNumber)
					{
							byte [] soll=beesSolutionsAndFitness.get(indice).getSolution();
							String solution=Arrays.toString(soll);solution=solution.replace("[", "");solution=solution.replace("]", "");
							sortieX.println(solution);
							sortieY.println("mauvaise");
					}
					}
				}

				indice++;
			}
	        
			sortieX.close();
			sortieY.close();
			sortieX_U.close();
		}
		
			
		public void unlabeledLabeling(String fichierX_U)
		{
			PrintWriter sortieX_U = null;
			/***sortieX et sortieY:c'est les solutions qui ont un labels, les solutions seulement dans sortieX et leurs labels dans sortieY */
			try {
				sortieX_U = new PrintWriter(new BufferedWriter (new FileWriter(fichierX_U, false)));
			} catch (IOException e) {
				e.printStackTrace();
			}		
	      int indice=0;
			while( (indice<beesSolutionsAndFitness.size()))
			{
				byte [] soll=beesSolutionsAndFitness.get(indice).getSolution();
				String solution=Arrays.toString(soll);solution=solution.replace("[", "");solution=solution.replace("]", "");
				sortieX_U.println(solution);
				indice++;
			}
			sortieX_U.close();
		}
		
		
		
			
}
