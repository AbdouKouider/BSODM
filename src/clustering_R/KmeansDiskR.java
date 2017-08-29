package clustering_R;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import bso.Main;

public class KmeansDiskR {
	
	public static double tempsR=0; //c le temps d'execution calcule par R lui meme.
	/**
	 * Cette methode execute kmeans en R et retourne un tableau double de 2 dimensions.
	 * */
	public static double [][] kmeansWithR(int nbrClusters,int nbrVariables,String fileOfSolutionsToCluster)
	{
		tempsR=0;
		
		
		double [][] tabDesCentres=new double [nbrClusters][nbrVariables]; ;
		RConnection connection = null;
	    try {
	      
	        connection = new RConnection();
	        String s="mydata <- read.table("+fileOfSolutionsToCluster+")";
			double tab[]=connection.eval("system.time("+s+", gcFirst = FALSE)").asDoubles();
			tempsR=tempsR+tab[0]+tab[1]; //user +system times.
	        String s1="set.seed(1)";
	        double tab1[]=connection.eval("system.time("+s1+", gcFirst = FALSE)").asDoubles();
	        tempsR=tempsR+tab1[0]+tab1[1];
	     
	        String myCode="result <- kmeans(mydata,"+nbrClusters+")"; // le code suspect de generer une erreur.
	        double tab2[]= connection.eval("system.time("+myCode+", gcFirst = FALSE)").asDoubles();
	        tempsR=tempsR+tab2[0]+tab2[1];
	        
	        REXP r = null;
			try {
				r = connection.parseAndEval("try("+myCode+")");
			} catch (REngineException e) {
				
			}
			if (r.inherits("try-error")) 
			{
				
				//traduction du message d'erreur en francais, si c'est le message de ;"more cluster centers than distinct data points"
				if(r.asString().equals("Error in kmeans(mydata, "+nbrClusters+") : \n  more cluster centers than distinct data points.\n"))
				{
					System.out.println("Le nombre de cluster depasse le nombre d'elements distincts, veuillez choisir un nombre de cluster plus petit.");
				}
				else
				{
					System.err.println(r.asString()); //le message d'origine a partir de R .
					
				}
				System.exit(0);
			}
	        
	        String dollarProblem="centres <-result$centers";
	        double tab3[]= connection.eval("system.time("+dollarProblem+", gcFirst = FALSE)").asDoubles();
	        tempsR=tempsR+tab3[0]+tab3[1];
			
	        double [] centres = connection.eval("centres").asDoubles(); // centres de taille: nbrVar*nbrClusters

	        //on ne mesure pas le temps de cette évaluation car ce n'est rien que la récupération d'une valaur de variable deja calcule ~ 0
	        for(int i=0;i<nbrVariables*nbrClusters;i++){
	        	tabDesCentres[i%nbrClusters][i/nbrClusters]=centres[i];
	        }
	        
	       
	    } 
	    catch (RserveException e) {
	        e.printStackTrace();
	    } 
	    catch (REXPMismatchException f)
	    {
	        f.printStackTrace();
	    }
	    finally{
	    	
	        connection.close();
	    }
	   
	   
	    Main.waktRvrai=Main.waktRvrai+tempsR; 
	    
	    return tabDesCentres;
	}


}
