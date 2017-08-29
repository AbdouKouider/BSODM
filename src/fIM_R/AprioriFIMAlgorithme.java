package fIM_R;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import bso.Main;

public class AprioriFIMAlgorithme {
	 /***
     * 
     * @param minimalSupport
     * @param longeurMinimaleDesItems
     * @param longueurMaximaleDesItems
     * @param cheminFichierDonnees
     * @return cette méthode va exécuter l'algorithme "eclat" de la technique FIM.
     */
	public static double tempsR=0; //c le temps d'execution calcule par R lui meme.
	public static boolean itemsNotEmpty=false; //ce boolean nous aide à éviter le problème des items vides que l'algo est en train de récupérer, donc si on trouve au moins un item non vide il sera à true.
	
public static ItemSupport [] fim(double minimalSupport,int longeurMinimaleDesItems,int longueurMaximaleDesItems,String cheminFichierDonnees)
	{
		tempsR=0;
				int nbrItemsFound=0;
			ItemSupport [] tabFrequentItemsetAndTheirSupport=null;
			RConnection connection = null;
		    try {
		    	try {
		        // Create a connection to Rserve instance running on default port 6311
		        connection = new RConnection();
		        	//String s1="library(arules)";
		        	//double tab[]= connection.eval("system.time("+s1+", gcFirst = FALSE)").asDoubles();
					//tempsR=tempsR+tab[0]+tab[1]; //user +system times.
					String s2="library(arules)";
					double tab1[]= connection.eval("system.time("+s2+", gcFirst = FALSE)").asDoubles();
					tempsR=tempsR+tab1[0]+tab1[1];
					String s3="tr <- read.transactions(\""+cheminFichierDonnees+"\",format=\"basket\",sep=',')";
					double tab2[]= connection.eval("system.time("+s3+", gcFirst = FALSE)").asDoubles();
					tempsR=tempsR+tab2[0]+tab2[1];
			     
				} catch (REXPMismatchException e2) 
				{
					e2.printStackTrace();
				}
		    
		        
		        String u=minimalSupport+"",y=longeurMinimaleDesItems+"",h=longueurMaximaleDesItems+"";
		     
		        String myCode="f <- apriori(tr, parameter = list(support = "+u+",minlen="+y+",maxlen="+h+",target=\"maximally frequent items\"))";
		        //System.out.println("Fin Apriori ...");
		        
		        REXP r = null;
				try {
					
						try
						{
							r = connection.parseAndEval("try("+myCode+")");
						}
						catch (REXPMismatchException e) 
						{
							
							e.printStackTrace();
						}
					
				 } 
				catch (REngineException e) 
				{
					e.printStackTrace();
				}
				if (r.inherits("try-error")) 
				{
					try {
						
						if(r.asString().equals("Error in apriori(tr, parameter = list(support = "+minimalSupport+", minlen = "+ longeurMinimaleDesItems+", maxlen = "+longueurMaximaleDesItems+")) : \n  not enough memory\n\n"))
						{
							System.out.println("Le nombre d'items trouvée est trop grand, Veuillez augmenter le support minimal des items");
						}
					} catch (REXPMismatchException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.exit(0);
				}
		        //////////////////////////////////////////////////////////////////////
				
				
				try {
					String s4="inspect <- inspect(f)";
					double tab3[]= connection.eval("system.time("+s4+", gcFirst = FALSE)").asDoubles();
					tempsR=tempsR+tab3[0]+tab3[1];
				} catch (REXPMismatchException e2)
				{
					e2.printStackTrace();
				}
		
		        //System.out.println("Fin inspect ...");
		       
		        //vérifier la taille de l'objet inspect pour voir si c'est 0 alors aucun itemset n'a été trouvé.///////////
		        double tailleInspect=0;
		        try {
		        	String s5="taille <- object.size(inspect)";
		        	double tab4[]= connection.eval("system.time("+s5+", gcFirst = FALSE)").asDoubles();
					tempsR=tempsR+tab4[0]+tab4[1];
					
					tailleInspect=connection.eval("taille").asDouble();
					//System.out.println("Taille inspect: "+tailleInspect+" bytes");
				} catch (REXPMismatchException e1)
		        {
					e1.printStackTrace();
				}
		        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
		        
		       if(tailleInspect>0)
		       {
		    	   
		        try {
		        	String s6="longueur <- lengths(inspect)";
		        	double tab5[]= connection.eval("system.time("+s6+", gcFirst = FALSE)").asDoubles();
					tempsR=tempsR+tab5[0]+tab5[1];
		        	
		        	
				nbrItemsFound= connection.eval("longueur").asInteger(); 
				//System.out.println(nbrItemsFound+" items found.");
				//a ce niveau nous avons combien on a trouvé d'items, donc on peut créer notre tableau récapitulatif.
				tabFrequentItemsetAndTheirSupport=new ItemSupport[nbrItemsFound];
				
				for(int i=0;i<nbrItemsFound;i++)
				{  //à cause de ce i on est en train de sauter on mais dans 0 puis on ne mais pas dans 1 (puisque c'est un item vide) et on mais dans 3
					
					String s7="s <- toString(inspect[["+(i+1)+","+1+"]])";
		        	double tab6[]= connection.eval("system.time("+s7+", gcFirst = FALSE)").asDoubles();
					tempsR=tempsR+tab6[0]+tab6[1];
		        	
					
					String item=connection.eval("s").asString();
					if(!item.equals("{}"))
					{
						itemsNotEmpty=true;
						//System.out.println("itemsNotEmpty=true "+item);
					tabFrequentItemsetAndTheirSupport[i]=new ItemSupport();
					item=item.replace("{", "");item=item.replace("}", "");
					tabFrequentItemsetAndTheirSupport[i].setItem(item);
					double support=connection.eval("inspect[["+(i+1)+","+2+"]]").asDouble();
					tabFrequentItemsetAndTheirSupport[i].setSupport(support);
					tabFrequentItemsetAndTheirSupport[i].setNbrItemsInThisItemSet(tabFrequentItemsetAndTheirSupport[i].nbrItem());
					
				   }
					
				}
			      } 
		       catch (REXPMismatchException e)
		       {
				e.printStackTrace();
			   }
		       
		       
		       }//tailleInspect>0
		       else
		       {
		    	  // System.out.println("L'ensemble d'items qui répond à votre requête est vide.\n Conseils: Veuillez soit:\n-diminuer le support \n  ou bien \n-diminuer la longueur minimale ");
		   		
		       }
		 
		    } //le tout premier try
		    catch (RserveException e) {
		        e.printStackTrace();
		    } 
		   
		    finally{
		    	
		        connection.close();
  
		    }
		    if(itemsNotEmpty==false) //si on n'a rien trouvé on retourne null.
			   {
			   tabFrequentItemsetAndTheirSupport=null;
			   }
		    itemsNotEmpty=false;//il faut la rendre à false ici car c'est une variable de classe.
		    
		    Main.waktRvrai=Main.waktRvrai+tempsR; 
		    return tabFrequentItemsetAndTheirSupport;
	}
	
	/** @param objet de type ItemSupport
	 * @return Retourner les items trouvés sous forme de tableau d'entiers.
	 */
	public static int [] itemsAsIntArray(ItemSupport o){
		int []p;
		if(o.getItem().contains(","))
		{
		String t[]=o.getItem().split(",");
		p=new int[t.length];
		for(int i=0;i<t.length;i++){
			p[i]=Integer.parseInt(t[i]);
		}
		
		}
		else //cela veut dire que la taille de l'itemset est égale à 1.
		{
			p=new int[1];
			p[0]=Integer.parseInt(o.getItem());
		}
		return p;
	}
   
	public static boolean containAllItems(byte [] solution,int []lesItems){
		boolean tousLesItemsSontDansCetteSolution=true;
		int parcours=0;
		while(tousLesItemsSontDansCetteSolution && parcours<lesItems.length)
		{
			
			if( (lesItems[parcours]!=0) //pour le empty itemset, on écrit un '0' car c'est un caractère qui ne peut pas être prit par les autres cas.
					&& (solution[Math.abs(lesItems[parcours])-1]!=( lesItems[parcours]/Math.abs(lesItems[parcours]) ))) 
				tousLesItemsSontDansCetteSolution=false;
			parcours++;
		}
		return tousLesItemsSontDansCetteSolution;
	}

}
