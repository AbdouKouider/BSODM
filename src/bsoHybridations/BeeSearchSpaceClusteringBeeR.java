package bsoHybridations;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import bso.Bee;
import bso.Main;
import bso.SatData;
import bso.Solution;
import toolsToManipulateSolutions.UsefulMethods;

public class BeeSearchSpaceClusteringBeeR extends Bee{
	
	
	public BeeSearchSpaceClusteringBeeR(SatData satData, int max, int beeNum) {
		super(satData,max,beeNum);
	}
	
	/** 
	 * Mettre nbrSolToCluster % des solutions rencontrees par la Bee dans un fichier	 */
	public void search(int nbrSolToCluster){
		
		HashSet <Solution> toAvoidSolutionRedundancy=new HashSet <Solution> ();
	
		////////////////////////////////////////////
		PrintWriter sortie = null;
		try {
			sortie = new PrintWriter(new BufferedWriter (new FileWriter("sol.csv", false)) );
		} catch (IOException e) {
			e.printStackTrace();
		}
		////////////////////////////////////////////
    /** il faut voir s'il reste de la ram avant d'ajouter*/
		
		int i, k = 0,cpt=0,
				inversedPosition=-1; // Pour eviter de doubler les solutions. 
		Solution saveSol=sol;
		while (k++ < max && cpt<(int)(nbrSolToCluster*max*satData.getVariableNumber()/100))  //normalement on a en tous nbrVar*max solutions
		{                                                                                       // on va consulte nbrSolToCluster% seulement
			
			for (i =0; i < satData.getVariableNumber(); i++)
			{
				
				if(i!=inversedPosition)
				{
					toAvoidSolutionRedundancy.add(UsefulMethods.flip(sol, i));
				cpt++;
				if(cpt>=(int)(nbrSolToCluster*max*satData.getVariableNumber()/100)) break;
				}
			}

			saveSol.flip(k);
			inversedPosition=k;
			sol=saveSol;
			saveSol.flip(k);//pour garder toujours la sol d'origine.
		}
	
		
		//ecrire les solutions du hashset en fichier une fois pour tout
				
				for(Solution actualSol: toAvoidSolutionRedundancy){
					sortie.println(UsefulMethods.byteArrayToString(actualSol.getSolution()));
				}
				sortie.close();
			
				
			
	}

}
