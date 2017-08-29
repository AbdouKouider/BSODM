 package semiSupervised_R;
import java.util.HashMap;
import java.util.Map;


import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import bso.Main;
import bsoHybridations.SSL_R;




public class RSSL {
	public static double tempsR=0; //c le temps d'execution calcule par R lui meme.
	/***
	 * 
	 * @param fichierSolutions
	 * @param nbrVariables
	 * @return cette méthode crée le model en R et retourne les prédictions des données non labélisées.
	 */
	public static int [] createSelfLearningModelAndPredictUnlabeledDataClass(String fichierX,String fichierY,String fichierX_U,int nbrVariables)
	{
		tempsR=0;
		int [] tabPredictions = null;  // la table qui va contenir les prédictions des données non labelisées, 1 pour bonne et 2 pour mauvaise.
		//RConnection connection = null;
		try 
		{
			try {
			
				//connection=new RConnection();
				SSL_R.connection.eval("rm(list=ls())");
				
			/** X **/	
			String s1="X <- read.table( file=\""+fichierX+"\", header=FALSE, sep=\",\" )";
			double tab[]=SSL_R.connection.eval("system.time("+s1+", gcFirst = FALSE)").asDoubles();
			tempsR=tempsR+tab[0]+tab[1];
			
			String s1_="X <- as.matrix(X)";
			double tab_[]=SSL_R.connection.eval("system.time("+s1_+", gcFirst = FALSE)").asDoubles();
			tempsR=tempsR+tab_[0]+tab_[1];
			
			/**fin X  **/
			
			/** X_u **/	
			String s2="X_u <- read.table( file=\""+fichierX_U+"\", header=FALSE, sep=\",\" )";
			double tab2[]=SSL_R.connection.eval("system.time("+s2+", gcFirst = FALSE)").asDoubles();
			tempsR=tempsR+tab2[0]+tab2[1];
			
			String s2_="X_u <- as.matrix(X_u)";
			double tab2_[]=SSL_R.connection.eval("system.time("+s2_+", gcFirst = FALSE)").asDoubles();
			tempsR=tempsR+tab2_[0]+tab2_[1];
			
			/**fin X_u  **/
			
			/** y **/	
			String s3="y <- read.table( file=\""+fichierY+"\", header=FALSE, sep=\",\" )";
			double tab3[]=SSL_R.connection.eval("system.time("+s3+", gcFirst = FALSE)").asDoubles();
			tempsR=tempsR+tab3[0]+tab3[1];
			
			String s3_="y <- as.matrix(y)";
			double tab3_[]=SSL_R.connection.eval("system.time("+s3_+", gcFirst = FALSE)").asDoubles();
			tempsR=tempsR+tab3_[0]+tab3_[1];
			
			String s3__="y <- factor(y)";
			double tab3__[]=SSL_R.connection.eval("system.time("+s3__+", gcFirst = FALSE)").asDoubles();
			tempsR=tempsR+tab3__[0]+tab3__[1];
			
			/**fin y  **/
			String s4="library(RSSL)";
			double tab4[]=SSL_R.connection.eval("system.time("+s4+", gcFirst = FALSE)").asDoubles();
			tempsR=tempsR+tab4[0]+tab4[1];
			
			//System.out.println("model creation");	
			
			//String s5="model<- SelfLearning(X,y,X_u,prob=FALSE,max_iter=0,method=NearestMeanClassifier)";
			//String s5="model <-WellSVM(X, y, X_u, C1 = 1, C2 = 0.1, gamma = 1, x_center = FALSE,scale = FALSE, use_Xu_for_scaling = FALSE, max_iter = 10)";
			
			String s5="model <-svmlin(X, y, X_u = NULL, algorithm = 1, lambda = 1, lambda_u = 1,max_switch = 10000, pos_frac = 0.5, Cp = 1, Cn = 1, verbose = FALSE,intercept =FALSE, scale = FALSE, x_center = FALSE)";
			double tab5[]=SSL_R.connection.eval("system.time("+s5+", gcFirst = FALSE)").asDoubles();
			tempsR=tempsR+tab5[0]+tab5[1];
			

			
			/*REXP r=null;
			try {
				r = SSL_R.connection.parseAndEval("try("+s5+",silent=TRUE)");
			} catch (REngineException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (r.inherits("try-error")) System.err.println("Error: "+r.asString());*/
			

			/** Prediction Unlabeled Data*****/
			String s6="predictions<- predict(model,X_u)";
			double tab6[]=SSL_R.connection.eval("system.time("+s6+", gcFirst = FALSE)").asDoubles();
			tempsR=tempsR+tab6[0]+tab6[1];
		
			//System.out.println("end of predictions");
	        /**   */
		    tabPredictions=SSL_R.connection.eval("predictions").asIntegers();
		   

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
		
		//connection.close();
		 Main.waktRvrai=Main.waktRvrai+tempsR; 
		 return tabPredictions;
			}

	/***
	 * 
	 * @param fichierX
	 * @param fichierY
	 * @param fichierX_U
	 * @param nbrVariables
	 * @return cette méthode utilise le model déjà crée pour ne faire que des prédictions.
	 */
	public static int [] predictUnlabeledDataClass(String fichierX_U,int nbrVariables){
		int [] tabPredictions = null;  // la table qui va contenir les prédictions des données non labelisées, 1 pour bonne et 2 pour mauvaise.
		//RConnection connection = null;
		tempsR=0;
		try 
		{
			//connection=new RConnection();
			/** Prediction Unlabeled Data*****/
			String s6="predictions<- predict(model,X_u)";
			double tab6[];
			try {
				tab6 = SSL_R.connection.eval("system.time("+s6+", gcFirst = FALSE)").asDoubles();
				tempsR=tempsR+tab6[0]+tab6[1];
			    tabPredictions=SSL_R.connection.eval("predictions").asIntegers();
			} catch (REXPMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		} catch (RserveException e) 
		{
		
			e.printStackTrace();
		}
		
		//connection.close();
		Main.waktRvrai=Main.waktRvrai+tempsR; 
		return tabPredictions;
	}

}
