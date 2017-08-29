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
import regression_R.RegressionWithRandomForest;
import toolsToManipulateSolutions.SolutionAndFitness;

public class RegressionR extends Swarm{


    
	protected int flip; // number of variable to inverse
    static float seuilDesBonnesSolutions=0;
    static int nbrSolutionsAllBees=0; //c'est le nombre de solutions rencontrées par les abeilles pendant les 'n' premières itérations, avec n=extraireLeModeleDeRegressionAfterHowManyIterations.
	public static boolean memoryProblem=false; /** sera positionné à true si on détectera un "OutOfMemoryError", dans ce cas on stop le remplissage de la liste 'beesSolutionsAndFitness'  **/ 
    
    private String fichiersSolutions,repertoireFichierPmml;
	private int extraireLeModeleDeRegressionAfterHowManyIterations;
	static LinkedList <SolutionAndFitness> beesSolutionsAndFitness=new LinkedList <SolutionAndFitness> (); //C'est la liste qui contient et les solutions et leurs fitness de toutes les abeilles, elle sera utilisée dans l'étape du labeling. 
	 
	/*************************************************************PMML variables*/
	public static  PMML model;
	public static MiningModelEvaluator modelEvaluator;
	public static Evaluator evaluator ;
	/******************************************************************/
	
	/***********************************************************JVM memory ***/
	
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public RegressionR(SatData satData, int heuristic, int maxIter,
			int nbSearchIter, int nBees, int flip, int chance,int afterHowManyIterations,String cheminFichierDesSolutions,String repertoireFichierPmml) 
	{
		super(satData, heuristic, maxIter, nbSearchIter, nBees, chance);
		this.flip = flip;
		this.fichiersSolutions=cheminFichierDesSolutions;
		this.repertoireFichierPmml=repertoireFichierPmml;
		this.extraireLeModeleDeRegressionAfterHowManyIterations=afterHowManyIterations;
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public RegressionR() {
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
			System.out.println("bso  Random Forest Regression");
			int i;
			seuilDesBonnesSolutions=0; nbrSolutionsAllBees=0;

			for (iter = 1; iter <= maxIter && bestSol.getFitness() < satData.getOptimum(); iter++) 
			{  
				
				
				setArea();
				taboo[iter - 1] = (Solution) solRef.clone();			
				for (i = 0; i < bees.length; i++)
				{
					bees[i] = new RegressionBeeR(satData, nbSearchIter, i);
					bees[i].setSol(area[i]);
					if(iter<=this.extraireLeModeleDeRegressionAfterHowManyIterations) 
						{
						((RegressionBeeR) bees[i]).searchWithoutRegression();
						}
					else {((RegressionBeeR) bees[i]).searchWithRegression();}
					dance[i] = bees[i].dance();
				}
				Arrays.sort(dance);
	
				////////////////////////////////////////////////////////////////////
				if(iter==this.extraireLeModeleDeRegressionAfterHowManyIterations)
				{ //c'est la dernière itération sans utilisation de Regression, les prochaines seront en utilisant la Regression.
					
					
					
					///labeling
					System.out.println("Starting labeling:");
					labeling(this.fichiersSolutions);

					RegressionWithRandomForest.createRandomForestModelAndSaveModel(this.fichiersSolutions,repertoireFichierPmml,satData.getVariableNumber());
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
	 * Cette méthode va créer un fichier contenant les solutions ainsi que leurs fitness
	 *  le fichier de sortie est le même que celui d'entrée, il rentre vide et il sort remplit.
	 */
	public void labeling(String fichier)
	{
		PrintWriter sortie = null;
		try {
			sortie = new PrintWriter(new BufferedWriter (new FileWriter(fichier, false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		///////////////////////////////////////////////////////////////
		//System.out.println("moyenne des fitness: "+seuil);

      int indice=0;
		while( indice < beesSolutionsAndFitness.size() && indice <5000 )// && (nbrWritedBonnes<minimalClasseSolutionNumber || nbrWritedMauvaises<minimalClasseSolutionNumber) )
		{
			int fitness=beesSolutionsAndFitness.get(indice).getFitness();
			sortie.print(beesSolutionsAndFitness.get(indice).getSolution());
			sortie.println(", "+ fitness);
		
			
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


