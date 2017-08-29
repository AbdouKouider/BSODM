package bsoHybridations;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import bso.Main;
import bso.SatData;
import bso.Solution;
import bso.Swarm;
import bso_dm_2017.FirstController;
import fIM_R.AprioriFIMAlgorithme;
import fIM_R.ItemSupport;

public class FrequentItemsetMiningVerticallyR extends Swarm{

	protected int flip; // number of variable to inverse
	private double minimalSupport;
	private int longueurMinimaleDesItems,longueurMaximaleDesItems;
	private int extraireFimAfterHowManyIterations;
	private String repertoireFichiersSolutionsDance;
	private ItemSupport [] bestItemFound=null;  //Quand on trie l'ensemble des items retrouvés on mait le meilleur ici.
	private int[][] bestItemFoundAsIntArray=null; //le meilleur item sous forme de tableau d'entiers.
	private int [] tailleDesParties=null;//puisque on va faire une FIM verticale donc il faut diviser chaque solution en n parties avec chaque partie de taille 10.
    private String [] tabFichiers=null; //un tableau qui contiendra les noms des fichiers contenant les parties des solutions.
    public static int nbrParties=0;     //parCombienDePartiesOnDiviseUneSolution.
    public static boolean auMoinsUnItemsetFound=false; //ce boolean sera à vrai si au moins nous avons trouvé un itemset qui vérifie les conditions.
    
    // /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public FrequentItemsetMiningVerticallyR(SatData satData, int heuristic, int maxIter,
			int nbSearchIter, int nBees, int flip, int chance , double minimalSupport,int longeurMinimaleDesItems,int longueurMaximaleDesItems,int extraireFimAfterHowManyIterations,String repertoireFichiersSolutionsDance) 
	{
		super(satData, heuristic, maxIter, nbSearchIter, nBees, chance);
		this.flip = flip;
		this.minimalSupport=minimalSupport;
		this.longueurMinimaleDesItems=longeurMinimaleDesItems;
		this.longueurMaximaleDesItems=longueurMaximaleDesItems;
		this.extraireFimAfterHowManyIterations=extraireFimAfterHowManyIterations;
		this.repertoireFichiersSolutionsDance=repertoireFichiersSolutionsDance;
		remplirTableauDesTaillesDesParties(satData.getVariableNumber());
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public FrequentItemsetMiningVerticallyR() 
	{
		
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
          System.out.println("bso  FIM");
			int i;

			for (iter = 1; iter <= maxIter && bestSol.getFitness() < satData.getOptimum(); iter++) 
			{
				setArea();
				taboo[iter - 1] = (Solution) solRef.clone();			
				for (i = 0; i < bees.length; i++) 
				{
					bees[i] = new FrequentItemsetMiningBeeVerticallyR(satData, nbSearchIter,i);
					bees[i].setSol(area[i]);
					
					if(iter<=extraireFimAfterHowManyIterations)
						{
						((FrequentItemsetMiningBeeVerticallyR) bees[i]).searchWithoutFIM();
						}
					else  
					{
						
						((FrequentItemsetMiningBeeVerticallyR) bees[i]).searchWithFIM(this.bestItemFoundAsIntArray);
						
					}
					
					dance[i] = (bees[i]).dance();
				}
				Arrays.sort(dance);
				
				/////////////////////////////////////////////////////////////////////
				if(iter<=this.extraireFimAfterHowManyIterations)
				{ 
					int indiceParcoursTotal=0;             //on parcourt la solution et on écrit chaque partie de 10 variables dans un fichier.
					for(int partie=0;partie<tailleDesParties.length;partie++)
					{
						this.tabFichiers[partie]="part"+partie+".csv";
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				PrintWriter sortie = null;
				try {
					sortie = new PrintWriter(new BufferedWriter (new FileWriter("part"+partie+".csv", true))); ////OPEN WITH TRUEEEEEEEEEEEEEEEEE
				} catch (IOException e) {
					e.printStackTrace();
				}
		    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				for(int u=0;u<dance.length;u++)
				{  //écriture des solutions de la table dance dans un fichier.
				
					int [] sol = new int[tailleDesParties[partie]];
					
					for( int l=indiceParcoursTotal;l<(indiceParcoursTotal+tailleDesParties[partie]);l++) 
						{
							sol[l-indiceParcoursTotal]=dance[u].getSolutionElement(l);
						}
					
						
					for(int k=indiceParcoursTotal;k<(indiceParcoursTotal+sol.length);k++){ //pour rendre les solutions sous la forme,ex:-1,2,-3,4
						sol[k-indiceParcoursTotal]=((k+1)*sol[k-indiceParcoursTotal]);
					}
					String s=Arrays.toString(sol);
					
					s=s.replace("[", ""); s=s.replace("]",""); s=s.replace(" ","");
					
					sortie.println(s);
				}
				sortie.close();
									
					indiceParcoursTotal=indiceParcoursTotal+tailleDesParties[partie];
					}
				}
				
				//à ce niveau nous avons des fichiers dans lesquels c'est écrit 10 variable de chaque solutions de la table dance.
				////////////////////////////////////////////////////////////////////
				if(iter==this.extraireFimAfterHowManyIterations)
				{ ///c'est bon nous avons écrit toutes nos solutions,maintenant le FIM.
					executeApriori();
					for(int j=0;j<this.bestItemFoundAsIntArray.length;j++){
						System.out.print(Arrays.toString(this.bestItemFoundAsIntArray[j])+" ");
					}
					System.out.println();
				}
				
				///////////////////////////////////////////////////////////////////

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

    public void executeApriori()
    {
    	for(int i=0;i<this.tailleDesParties.length;i++)
    {
    		
    		
    	ItemSupport [] items=AprioriFIMAlgorithme.fim(minimalSupport,longueurMinimaleDesItems,longueurMaximaleDesItems,this.repertoireFichiersSolutionsDance+"/"+this.tabFichiers[i]);

    	if(items!=null) 
		{

		auMoinsUnItemsetFound=true;
		//Arrays.sort(items);
		////////////////////////////au lieu de trier on parcours et on récupère le maximum en longueur.
		int longueur=0,indice=0;
		for(int o=0;o<items.length;o++)
		{
			if(items[o]!=null)
			   {
				
			if(items[o].getItem().length()>longueur)
			{
				longueur=items[o].getItem().length();
				indice=o;
			}
				}
		}
	
		items[0]=items[indice];//on mais le meilleur (le plus long) dans la case 0.
		/////////////////////////
		this.bestItemFound[i]=items[0];
		
		this.bestItemFoundAsIntArray[i]=AprioriFIMAlgorithme.itemsAsIntArray(items[0]);
		
		}
		else 
		{ //ici items==null, soit parceque vraiment il n'a rien trouvé soit parcqu'il a trouvé des item vide.
		
			ItemSupport empty=new ItemSupport("empty",-1);
			this.bestItemFound[i]=empty;
			int [] emptyItemsetInt=new int[]{0};
			this.bestItemFoundAsIntArray[i]=emptyItemsetInt;
		}	
   }
    	if(auMoinsUnItemsetFound==false) 
    		{
    		System.out.println("Aucun itemset fréquent n'a été trouvé, Veuillez diminuez le support.");
    		System.exit(0);
    		}
    }

    public void remplirTableauDesTaillesDesParties(int nbrVariables)
    {
    	int combienDeParties=(nbrVariables/10);
    	
    	if(nbrVariables%10==0) 
    		{
    		nbrParties=combienDeParties;
    		this.tailleDesParties=new int[combienDeParties];
    		this.tabFichiers=new String[combienDeParties];
    		this.bestItemFound=new ItemSupport[combienDeParties];
    		this.bestItemFoundAsIntArray=new int [combienDeParties][];
    		for(int i=0;i<combienDeParties;i++)
    			{
    			tailleDesParties[i]=10;
    			}
    		}
    	else
    		{
    		nbrParties=combienDeParties+1;
    		this.tailleDesParties=new int[combienDeParties+1];
    		this.tabFichiers=new String[combienDeParties+1];
    		this.bestItemFound=new ItemSupport[combienDeParties+1];
    		this.bestItemFoundAsIntArray=new int [combienDeParties+1][];
    		for(int i=0;i<combienDeParties;i++)
    			{
    			tailleDesParties[i]=10;
    			}
    		tailleDesParties[combienDeParties]=nbrVariables%10;
    		}
    	
    }
}
