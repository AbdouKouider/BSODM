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

import org.dmg.pmml.PMML;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.mining.MiningModelEvaluator;
import org.jpmml.model.ImportFilter;
import org.jpmml.model.JAXBUtil;
import org.xml.sax.InputSource;

import bso.Main;
import bso.SatData;
import bso.Solution;
import bso.Swarm;
import bso_dm_2017.FirstController;
import classification_R.ClassificationWithRandomForest;
import toolsToManipulateSolutions.SolutionAndFitness;

public class ClassificationR extends Swarm{


    
	protected int flip; // number of variable to inverse
    static float seuilDesBonnesSolutions=0;
    static int nbrSolutionsAllBees=0; //c'est le nombre de solutions rencontrées par les abeilles pendant les 'n' premières itérations, avec n=extraireLeModeleDeClassificationAfterHowManyIterations.
	public static boolean memoryProblem=false; /** sera positionné à true si on détectera un "OutOfMemoryError", dans ce cas on stop le remplissage de la liste 'beesSolutionsAndFitness'  **/ 
    
    private String fichiersSolutions,repertoireFichierPmml;
	private int extraireLeModeleDeClassificationAfterHowManyIterations;
	static LinkedList <SolutionAndFitness> beesSolutionsAndFitness=new LinkedList <SolutionAndFitness> (); //C'est la liste qui contient et les solutions et leurs fitness de toutes les abeilles, elle sera utilisée dans l'étape du labeling. 
	 
	/*************************************************************PMML variables*/
	public static  PMML model;
	public static MiningModelEvaluator modelEvaluator;
	public static Evaluator evaluator ;
	/******************************************************************/
	
	/***********************************************************JVM memory ***/
	
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public ClassificationR(SatData satData, int heuristic, int maxIter,
			int nbSearchIter, int nBees, int flip, int chance,int afterHowManyIterations,String cheminFichierDesSolutions,String repertoireFichierPmml) 
	{
		super(satData, heuristic, maxIter, nbSearchIter, nBees, chance);
		this.flip = flip;
		this.fichiersSolutions=cheminFichierDesSolutions;
		this.repertoireFichierPmml=repertoireFichierPmml;
		this.extraireLeModeleDeClassificationAfterHowManyIterations=afterHowManyIterations;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public ClassificationR() {
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
			System.out.println("bso  Random Forest");
			int i;
			seuilDesBonnesSolutions=0; nbrSolutionsAllBees=0;

			for (iter = 1; iter <= maxIter && bestSol.getFitness() < satData.getOptimum(); iter++) 
			{  
				
				
				setArea();
				taboo[iter - 1] = (Solution) solRef.clone();			
				for (i = 0; i < bees.length; i++)
				{
					bees[i] = new ClassificationBeeR(satData, nbSearchIter, i);
					bees[i].setSol(area[i]);
					if(iter<=this.extraireLeModeleDeClassificationAfterHowManyIterations) 
						{
						((ClassificationBeeR) bees[i]).searchWithoutClassification();
						}
					else {((ClassificationBeeR) bees[i]).searchWithClassification();}
					dance[i] = bees[i].dance();
				}
				Arrays.sort(dance);
	
				////////////////////////////////////////////////////////////////////
				if(iter==this.extraireLeModeleDeClassificationAfterHowManyIterations)
				{ //c'est la dernière itération sans utilisation de classification, les prochaines seront en utilisant la classification.
					
					
					
					///labeling
					System.out.println("Starting labeling:");
					labeling(this.fichiersSolutions, seuilDesBonnesSolutions/nbrSolutionsAllBees);

					ClassificationWithRandomForest.createRandomForestModelAndSaveModel(this.fichiersSolutions,repertoireFichierPmml,satData.getVariableNumber());
					/**************à ce niveau nous avons notre modèle sauvegardé dans un fichier pmml*/
					 try {
				            model = loadModel(repertoireFichierPmml+"/randomForestModel.pmml");	
				         }
				     catch(Exception e) 
				         {
				        	e.printStackTrace();
				         }
			
				        modelEvaluator = new MiningModelEvaluator(model);
				        evaluator = modelEvaluator;

					System.out.println("fin creationModel");
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

	

	}

	/**
	 * Cette méthode va créer un fichier contenant les solutions ainsi que leurs classe ('bonne' ou 'mauvaise') en utilisant une liste contenant
	 *  les solutions et leurs fitness et en utilisant un seuil.
	 *  le fichier de sortie est le même que celui d'entrée, il rentre vide et il sort remplit.
	 */
	public void labeling(String fichier, float seuil)
	{
		PrintWriter sortie = null;
		try {
			sortie = new PrintWriter(new BufferedWriter (new FileWriter(fichier, false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		///////////////////////////////////////////////////////////////
		System.out.println("moyenne des fitness: "+seuil);
      String classe="";
      
       /****A fin de garantir une égalité entre les 2 classes "bonne" et "mauvaise" il faut que le nbr de solutions des 2 classes
        * soient égaux donc on parcours d'abord la list pour compter le nbr de ceux qui vont devenir bonnes et mauvaises
        * et on écrit dans le fichier le nbr minimum de ces 2 classes: ex s'il y a 20 bonnes et 5 mauvaises on écrira 5 pour les 2 classes. 
        */
      int nbrBonnes=0,nbrMauvaises=0;
      for(int i=0;i<beesSolutionsAndFitness.size();i++)
		{
    	  if(beesSolutionsAndFitness.get(i).getFitness()>seuil) nbrBonnes++;
    	  else nbrMauvaises++;
		}
      System.out.println("il y a: "+nbrBonnes+" bonnes solutions et :"+nbrMauvaises+" mauvaises");
      
      int nbrWritedBonnes=0,nbrWritedMauvaises=0,minimalClasseSolutionNumber=0;  
      
      if(nbrMauvaises<nbrBonnes) minimalClasseSolutionNumber=nbrMauvaises;
      else                       minimalClasseSolutionNumber=nbrBonnes;
      
      /*** faire un dataset de 5000 lignes et essayer d'extraire beaucoup de connaissance est meilleur que 
       * faire un dataset de 1000000 lignes et un petit arbre qui n'a rien comme connaissance et qui tend plus vers l'aléatoire
       */
      if(minimalClasseSolutionNumber>2500) minimalClasseSolutionNumber=2500;
      
      int indice=0;
		while( ( indice<beesSolutionsAndFitness.size() ) && (nbrWritedBonnes<minimalClasseSolutionNumber || nbrWritedMauvaises<minimalClasseSolutionNumber) )
		{
			int fitness=beesSolutionsAndFitness.get(indice).getFitness();
			
			if(fitness>seuil) 
				{
				//classe="bonne";
				nbrWritedBonnes++;
				if(nbrWritedBonnes<=minimalClasseSolutionNumber)
					{
						sortie.print(beesSolutionsAndFitness.get(indice).getSolution());
						sortie.println(", "+"bonne");
					}
				}
			
			else
				{
				//classe="mauvaise";
				nbrWritedMauvaises++;
				if(nbrWritedMauvaises<=minimalClasseSolutionNumber)
					{
						sortie.print(beesSolutionsAndFitness.get(indice).getSolution());
						sortie.println(", "+"mauvaise");
					}
				}

			indice++;
		}
        
		beesSolutionsAndFitness=new LinkedList <SolutionAndFitness> ();
		sortie.close();
	}
		
	/***
	 * 
	 * @param file
	 * @return Cette méthode charge le modèle à partir d'un fichier pmml.
	 * @throws Exception
	 */
	public static PMML loadModel(final String file) throws Exception {

		   PMML pmml = null;

		   File inputFilePath = new File( file );

		   try( InputStream in = new FileInputStream( inputFilePath ) ){

		     Source source = ImportFilter.apply(new InputSource(in));
		     pmml = JAXBUtil.unmarshalPMML(source);

		   } catch( Exception e) {
		     
		   }
		   return pmml;
		}
	}


