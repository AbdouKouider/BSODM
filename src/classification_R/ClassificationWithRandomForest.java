package classification_R;

import java.util.HashMap;
import java.util.Map;

import org.dmg.pmml.FieldName;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import bso.Main;
import bsoHybridations.ClassificationR;


public class ClassificationWithRandomForest {
	public static double tempsR=0; //c le temps d'execution calcule par R lui meme.
	/***
	 * 
	 * @param fichierSolutions
	 * @param nbrVariables
	 * @return cette méthode crée le model en R et le sauvegarde pour qu'il soit utilisé dans le cadre du PMML.
	 */
	public static void createRandomForestModelAndSaveModel(String fichierSolutions,String repertoireFichierPmml,int nbrVariables)
	{
		System.out.println("Starting model creation");
		tempsR=0;
		RConnection connection = null;
		try 
		{
			try {
			
				connection=new RConnection();
				connection.eval("rm(list=ls())");
				
				System.out.println("Loading dataset");
				
			String s1="trainingData <- read.table( file=\""+fichierSolutions+"\", header=FALSE, sep=\",\" )";
			double tab[]=connection.eval("system.time("+s1+", gcFirst = FALSE)").asDoubles();
			tempsR=tempsR+tab[0]+tab[1]; //user +system times.
			
			System.out.println("end loading dataset in: "+(tab[0]+tab[1])+" s");

			String s2="library(randomForest)";
			double tab1[]=connection.eval("system.time("+s2+", gcFirst = FALSE)").asDoubles();
			tempsR=tempsR+tab1[0]+tab1[1];
			
			System.out.println("model creation");
			
			String s3="model <-  randomForest(V"+(nbrVariables+1)+"~., data=trainingData,ntree="+4+",nodesize="+(nbrVariables/10)+",proximity=FALSE)";
			double tab2[]=connection.eval("system.time("+s3+", gcFirst = FALSE)").asDoubles();
			tempsR=tempsR+tab2[0]+tab2[1];
			
			System.out.println("end model creation in:"+(tab2[0]+tab2[1])+" s");
			
			/***à ce niveau on a crée le model donc on fait appel au GC, en espérant gagner un peu de RAM */
			System.gc();
		
			
			/**PRINT ERROR MESSAGE *********************
			REXP rResponseObject = null;
			try {
				rResponseObject = connection.parseAndEval("try(model <-  randomForest(V"+(nbrVariables+1)+"~., data=trainingData,ntree=1))");
			} catch (REngineException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (REXPMismatchException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			if (rResponseObject.inherits("try-error")) 
			{
				try {
					rResponseObject.asString();
				} catch (REXPMismatchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				}
			
			
			/******************************************/
			
			
			/**********************************************************Save The Model *******************************/
			String s4_0="library(pmml)";
			double tab4_0[]=connection.eval("system.time("+s4_0+", gcFirst = FALSE)").asDoubles();
			tempsR=tempsR+tab4_0[0]+tab4_0[1];
			
			System.out.println("PMML model saving");
			
			/*************************************************************************************************************************/
			String s44="saveRDS(model,\"modelSerialised.rds\")";
			double tab33[]=connection.eval("system.time("+s44+", gcFirst = FALSE)").asDoubles();
			tempsR=tempsR+tab33[0]+tab33[1];
			System.out.println("FIN serialisation in: "+(tab33[0]+tab33[1])+" s");
			/*************************************************************************************************************************/
			String s4="saveXML(pmml.randomForest(model),\""+repertoireFichierPmml+"/randomForestModel.pmml\")";
			double tab3[]=connection.eval("system.time("+s4+", gcFirst = FALSE)").asDoubles();
			tempsR=tempsR+tab3[0]+tab3[1];
			
			System.out.println("FIN PMML model saving in: "+(tab3[0]+tab3[1])+" s");
			/********************************************************************************************************/
			
			} 
			catch (REXPMismatchException e) 
			{
				e.printStackTrace();
			}
			
		} 
		catch (RserveException e) 
		{

			e.printStackTrace();
		}
		
		connection.close();
		 Main.waktRvrai=Main.waktRvrai+tempsR; 
			}
	

	/***
	 * 
	 * @param solution
	 * @return Cette méthode utilise la liste des règles de décisions de la classe ClassificationR pour classifier une solution donnée.
	 * si la classe convenable est "bonne" alors elle retourne true sinon false
	 */
	public static boolean predictAsolutionClass(byte [] solution)
	{

		boolean bonneSolution=false;
		HashMap<FieldName, Double> params = new HashMap();
		
		for(int i=0;i<solution.length;i++)
		{
        params.put(new FieldName("V"+(i+1)),(double)(solution[i]));
      
		}

        Map results = ClassificationR.evaluator.evaluate( params );
       // System.out.println(results);
       // System.out.println(results.values().toArray()[1]);
        String s=(String) results.values().toArray()[1];
        if(s.equals(" bonne")) 
        	{
        	bonneSolution=true;
        	}
		return bonneSolution;
	}

}
