package bso;

public abstract class Swarm {


	protected SatData satData ;        // the sat instance 
	protected int maxIter ;    		   // maximum iterations for the methauristic
	protected int nbSearchIter;        //the number of search iteration for evrey bee
	protected Solution solRef ;        // the reference solution 
	protected Bee bees[];              // foragers bees
	protected BeeInit beeInit ;        // initial bee
	protected Solution area [] ;       // the solutions we affect to every foarger
	protected Solution taboo[];        // the taboo list
	protected Solution dance[] ;       // best solutions found by foragers
	protected int iter;                // how many iterations were done
	protected int chance;              // max chances we allows to a none improving solution
	protected int penality;            // the chances this solution had
	protected Solution bestSol  ;      // the best solution found
	protected int lastVal;             // the fitness of best solution found

	protected Swarm me;                //l ref de l'objet courant, on l'aura besoin dans les strategies, added by me
	

	/**
	 * Constructor using fields
	 * @param satData
	 * @param heuristic
	 * @param maxIter
	 * @param nbsearchiter
	 * @param NBees
	 * @param flip
	 * @param chance
	 */
	public Swarm(SatData satData , int heuristic,int maxIter,int nbSearchIter,int nBees ,int chance){

		this.me=this; //added by me.
		this.satData=satData;
		this.maxIter =maxIter;
		this.nbSearchIter=nbSearchIter;
		this.chance=chance;
		this.beeInit = new BeeInit(satData);

		solRef = beeInit.heuristic(heuristic);
                //System.out.println(" BSO : beeinit = "+ solRef.getFitness());
                
		bestSol = (Solution)solRef.clone();
		lastVal=0; 
		penality=0;
		bees = new Bee[nBees];
		area = new Solution[nBees];
		taboo = new Solution[maxIter];
		dance = new Solution[nBees];

	}

	public Swarm() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * This function generates a new search area from the reference solution and store it in the table area
	 */
	public abstract void setArea ();

	/**
	 * This function check if a value exists in an array 
	 * @param k 	=====> the value to check
	 * @param tabou =====> the array we do the search in
	 * @param N		=====> the size of the array
	 * @return true if it exists
	 */
	public boolean existe( int k , int[] taboo , int N ){

		if ( N<0 ) 
			return false ;
		for (int i=0 ; i<N ; i++)
			if(taboo[i] == k)
				return true;
		return false ;

	}

	/**
	 * This method is the heart of the class, it use the algorithm bso to solve the sat instancs
	 */
	public abstract void bso();

	/**
	 * This method select the next reference solution
	 * @return the next reference solution
	 */
	public Solution bestBee(){

		// if the best solution found by bees improve the reference solution, it is the next reference solution and its not penalized 
		if (dance[0].getFitness() > lastVal ){
			penality=0 ;
			return bestBeeQuality();
		}
		penality++ ;
		// if it doesn't improve we check if there is a chance left
		// if yes we return the best solution in quality
		if (penality < chance ) 
			return bestBeeQuality(); 
		// if all the chances are taken we return the best solution on diversity
		penality = 0 ; 
		return bestBeeDiversity();

	}

	/**
	 * This method find the best solution in diversification in dance table
	 * @return best solution in diversification 
	 */
	public Solution bestBeeQuality(){
		int i=0 ,distance=0 ,pos=-1 ,max ;

		while ( i<bees.length ){
			max =  dance[i].getFitness();
			while ( i<bees.length && dance[i].getFitness()==max ){
				if (distance(dance[i]) > distance){
					distance = distance (dance[i]);
					pos = i ;
				}
				i++;
			}
			if ( pos!= -1 ) 
				return dance[pos] ;
		}
		byte[] s = beeInit.random();
		System.out.println(".......");  //bsoClustering:il vient ici qlq soit amelioration de solRef ou pas, dans bso s'il n y a pas d'amelioration il vient ici.
		return new Solution(s, satData.fitness(s)) ;
	}


	/**
	 * This function return the best diversifying solution found by the bees
	 * @return best diversifying solution
	 */
	public Solution bestBeeDiversity(){
            
            System.out.println("diversification");
            
		int max=0,// the max distance from the taboo list found 
				i,j = 0; 
		// for all the solution in dance
		for (i=0 ; i<bees.length ; i++ ) 
			// if the distance is bigger than the actual distance we save the index of the solution and we update the max distance 
			if ( distance(dance[i]) > max ){  
				max=distance ( dance[i] );
				j=i;
			}
		// if all the found by the bees are in the taboo list return a random solution
		if (max==0) {
			byte[] s = beeInit.random();
			return new Solution(s, satData.fitness(s)) ;
		}
		// else we return the solution with the biggest distance
		return dance[j];

	}


	/**
	 * This function calculates the minimal distance between X and the taboo list
	 * @param X ====> Represents the solution we want to know the distance about
	 * @return
	 */
	public int distance (Solution X){
		int cpt, // the distance between a solution and a entry of the taboo list
		distanceMin=satData.getVariableNumber(); //  the minimal distance found between the solution and taboo list entries
		// for all the elements in the taboo list
		for ( int i=0 ; i<iter ; i++ ){
			cpt=0 ; 
			// for all the variable
			for (int j=0; j<satData.getVariableNumber() ; j++) 
				// calculates the difference between X and taboo elements
				if  (X.getSolutionElement(j) !=taboo[i].getSolutionElement(j) ) 
					cpt++ ;
			// if the solution is in the taboo list the minimal distance is 0
			if ( cpt < 1 ) 
				return 0;
			if ( cpt < distanceMin ) 
				distanceMin = cpt ;
		}
		return distanceMin ;

	}
	/**
	 * This method is a getter for the attribute bestSol
	 * @return bestSol
	 */

	public Solution getBestSol() {
		return bestSol;
	}
	/**
	 * This method is a getter for the attribute taboo
	 * @return taboo
	 */
	public Solution[] getTaboo(){
		return taboo;
	}


	public void setSolRef(Solution solRef){
	this.solRef=solRef;
}

}