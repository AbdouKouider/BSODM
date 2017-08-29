package bso;

public class BeeInit{

	SatData satData ;  //  the Sat instance

	
	public BeeInit(SatData satData ){
		this.satData = satData ; 
	} 

	/** This function build a solution following four different algorithms @return a solution to the instance */
	public Solution heuristic (int HEURISTIC ){

		if (HEURISTIC == 1 ) return john1();
		if (HEURISTIC == 2 ) return john2a();
		if (HEURISTIC == 3 ) return john2b();
		if (HEURISTIC == 4 ) return localSearch();
		byte[] s = random();
		return new Solution(s,satData.fitness(s));

	}

	/**
	 * This function do a local search 
	 * @param sol     =====> the start point of the local search
	 * @param satData =====> the sat instance we search in
	 * @return the best solution found
	 */
	public Solution localSearch(){
		int i,  // index to path the instance variable
		best, // best fitness found
		pos; // the position to flip to have the the solution with the best fitness
		byte[] s = random();
		Solution sol = new Solution(s, satData.fitness(s));
		best = sol.getFitness();
		while ( true ){
			pos=-1 ;// non best solution found
			// for all the variables of the instance
			for ( i=0 ;i<satData.getVariableNumber();i ++){
				// we flip on variable at time 
				sol.flip(i);
				sol.setFitness(satData.fitness(sol.getSolution()));
				// if the new solution improve the fitness we save the flip position in posi and the best fitness in posi
				if (sol.getFitness() > best ){
					pos=i;
					best=sol.getFitness();
				}
				// we undo the changes in sol

				sol.flip(i);
				sol.setFitness(satData.fitness(sol.getSolution()));
			}
			// if there is an improvement, we restart the research from the solution that improve
			if ( pos !=-1) {
				// we flip on variable at time 
				sol.flip(pos);
				sol.setFitness(satData.fitness(sol.getSolution()));
			}
			// if there no improvement means that we reached the local optimum
			else break;

		}
		return sol ;
	}

	/**
	 * This function find a solution following the john1 algorithm
	 * @return the solution found after executing the algorithm
	 */
	public Solution john1(){
		int i,
		j,
		max,
		positif, // count how many the variable i appear positive
		negatif, // count how many the variable i appear negative 
		pos=0 ,
		signe=1;
		// X represents the set of variable
		byte variables[]=new byte[satData.getVariableNumber()] ;

		// all the variables are intialised to zero witch means that they are not 
		// value affected yet
		for (i=0;i<variables.length;i++)
			variables[i]=0;
		// clauseSup represent the set of clauses
		boolean clauseSup[]=new boolean[satData.getClauseCount()];
		// all clauses are initialized to false witch means that they
		// have not been treated yet
		for (i=0;i<clauseSup.length;i++)
			clauseSup[i]=false; 

		while ( true ){
			max = 0 ;
			// browse the variable set
			for ( i=1 ; i<=variables.length ; i++ ) 
				// if the variable has not been treated yet initialize positive and negative to 0
				if(variables[i-1]==0){
					positif=0 ; 
					negatif=0;
					// for all the clauses 
					for (j=0 ; j<clauseSup.length ; j++) 
						// if the clause is not treated yet
						if (!clauseSup[j]){
							// if i appears positive in J we add its weight to the variable positive
							if (exists(i,j)) 
								positif = positif + satData.getClause(j).getWeight();
							// if i appears positive in J we add its weight to the variable positive
							else if (exists(-i,j)) 
								negatif = negatif +satData.getClause(j).getWeight();
						}//for j
					// if the accumulated positive weight is greater than max 
					// we update the max found
					// we keep the position of the variable
					// the sign of the current variable is put to positive
					if (positif > max){
						max=positif;
						pos=i ;
						signe=1; 
					}
					// if the accumulated negative weight is greater than max 
					// we update the max found
					// we keep the position of the variable
					// the sign of the current variable is put to negative
					if (negatif > max){ 
						max=negatif ; 
						pos=i ; 
						signe=-1; 
					}
				}//for i
			// all the variables or all the clauses has been treated
			if ( max ==0 )
				break;
			// affecting the sign to the most repeated variable
			variables[pos-1]=(byte) signe;

			// put all the clauses witch contains the variable i to true to say they have been treated
			for ( j=0;j<clauseSup.length;j++) 
				if (!clauseSup[j])
					if (exists(signe*pos,j)) 
						clauseSup[j]=true;
		}
		// for all the variable that has not been treated yet put them to true
		for (i=0 ; i<variables.length ; i++) 
			if(variables[i]==0)
				variables[i]=1 ;

		//return the result
		return new Solution(variables,satData.fitness(variables));
	}

	/**
	 * This function find a solution following the john2a  algorithm
	 * @return the solution found after executing the algorithm
	 */
	public Solution john2a(){
		int i, //index to browse the variable set
		j, // index to browse the clause set
		pos=0, // position of the variable to affect
		signe=1; // the value to affect to the variable posi

		double positif, // the cumulated weight where the variable appears positive
		negatif, // the cumulated weight where the variable appears negative
		max,// the maximum cumulated weight
		bestPositive = 0,
		bestNegative = 0; 
		byte variables[]=new byte[satData.getVariableNumber()] ; // the final solution
		// intialiwing th final solution to a zero vector. Zero means that the variable has not been treated yet
		for (i=0;i<variables.length;i++)
			variables[i]=0;
		// the vector that will show if the clauses are treated or not
		boolean clauseSup[]=new boolean[satData.getClauseCount()]; 
		// Initializing the vector to false
		for (i=0;i<clauseSup.length;i++)
			clauseSup[i]=false; 
		// the vector that will keep the john2a weight of every clause
		double weights[]=new double [clauseSup.length] ; 
		// calculating the weight of every cluase
		for (i=0 ; i<weights.length; i++)
			weights[i]=1/Math.pow(2,satData.getClause(i).length());

		while ( true ){
			max = 0 ;
			// For all the variable set
			for ( i=1 ; i<=variables.length ; i++ )
				// if the variable i has no value affected yet
				if(variables[i-1]==0){
					// we initialize the positif and negatif variable to zero
					positif=0;
					negatif=0;
					// and then we calculate the cumualted weight for the negative an positive appearance of the variable
					for (j=0 ; j<clauseSup.length; j++) 
						if (!clauseSup[j]){
							if (exists(i,j))  
								positif = positif +satData.getClause(j).getWeight()*weights[j];
							else if (exists(-i,j))
								negatif = negatif +satData.getClause(j).getWeight()*weights[j];
						}//for j
					// we keep the variable that have the lowest difference between the positive cumulated weight and the negative one
					if( Math.abs(positif-negatif) >= max ){
						max=Math.abs(positif-negatif) ;
						pos=i;
						bestNegative= negatif;
						bestPositive=positif;
					}
				}
			// if all the clauses are treated then we quit
			if ( max ==0 ) 
				break;
			// recalculating the positive and negative cumulated weight, INUTILE

			// affecting the sign to the current variable
			if (bestPositive > bestNegative )
				signe=1;
			else 
				signe=-1;            
			variables[pos-1]=(byte) signe;

			// if the variable satisfy any clause it is considered as treated
			for ( j=0;j<clauseSup.length;j++) 
				if (!clauseSup[j]){
					if (exists(signe*pos,j)) 
						clauseSup[j]=true;
				}
			// else updating the weight of the clause J
				else if(exists(-signe*pos,j)){
					weights[j]=weights[j]*2;
				}

		}
		// If there variables that has no values, they get the value true
		for (i=0 ; i<variables.length; i++) 
			if(variables[i]==0) 
				variables[i]=1;
		return new Solution(variables,satData.fitness(variables));
	}


	/**
	 * This function find a solution following the john2b  algorithm
	 * @return the solution found after executing the algorithm
	 */
	public Solution john2b(){
		int i, //index to browse the variable set
		j, // index to browse the clause set
		pos=0,  
		signe=1; // the value to affect to the variable posi

		double positif, // the cumulated weight where the variable appears positive
		negatif, // the cumulated weight where the variable appears negative
		max,// the maximum cumulated weight
		bestPositive = 0,
		bestNegative = 0; 
		byte variables[]=new byte[satData.getVariableNumber()] ; // the final solution
		// intialiwing th final solution to a zero vector. Zero means that the variable has not been treated yet
		for (i=0;i<variables.length;i++)
			variables[i]=0;
		// the vector that will show if the clauses are treated or not
		boolean clauseSup[]=new boolean[satData.getClauseCount()]; 
		// Initializing the vector to false
		for (i=0;i<clauseSup.length;i++)
			clauseSup[i]=false; 
		// the vector that will keep the john2a weight of every clause
		double weights[]=new double [clauseSup.length] ; 
		// calculating the weight of every cluase
		for (i=0 ; i<weights.length; i++)
			weights[i]=1/Math.pow(2,satData.getClause(i).length());

		while ( true ){
			max = 0 ;
			// For all the variable set
			for ( i=1 ; i<=variables.length ; i++ )
				// if the variable i has no value affected yet
				if(variables[i-1]==0){
					// we initialize the positif and negatif variable to zero
					positif=0;
					negatif=0;
					// and then we calculate the cumualted weight for the negative an positive appearance of the variable
					for (j=0 ; j<clauseSup.length; j++) 
						if (!clauseSup[j]){
							if (exists(i,j))  
								positif = positif +satData.getClause(j).getWeight()/satData.getClause(j).length();
							else if (exists(-i,j))
								negatif = negatif +satData.getClause(j).getWeight()/satData.getClause(j).length();
						}
					// we keep the variable that have the lowest difference between the positive cumulated weight and the negative one
					if( Math.abs(positif-negatif) >= max ){
						max=Math.abs(positif-negatif) ;
						pos=i;
						bestNegative= negatif;
						bestPositive=positif;
					}
				}
			// if all the clauses are treated then we quit
			if ( max ==0 ) 
				break;
			// recalculating the positive and negative cumulated weight, INUTILE

			// affecting the sign to the current variable
			if (bestPositive > bestNegative )
				signe=1;
			else 
				signe=-1;            
			variables[pos-1]=(byte) signe;

			// if the variable satisfy any clause it is considered as treated
			for ( j=0;j<clauseSup.length;j++) 
				if (!clauseSup[j]){
					if (exists(signe*pos,j)) 
						clauseSup[j]=true;
				}
			// else updating the weight of the clause J
				else if(exists(-signe*pos,j)){
					weights[j]=weights[j]*2;
				}

		}
		// If there variables that has no values, they get the value true
		for (i=0 ; i<variables.length; i++) 
			if(variables[i]==0) 
				variables[i]=1;
		return new Solution(variables,satData.fitness(variables));
	}


	/**
	 * This function check if a literal appears in a clause
	 * @param literal =====> the literal we want to see if it appears
	 * @param clause  =====> the clause we check in
	 * @return true if it appears
	 */
	boolean exists ( int literal , int clause){
		int i;
		for (i=0 ; i< satData.getClause(clause).length() ; i++ ) 
			if (satData.getClause(clause).getLiteral(i)==literal) 
				return true;
		return false ;
	}


	/**
	 * This function generates a random number in the set {-1,1}  @return the random number generated  */
	public static byte rand(){
		int r=  ( (int)(Math.random()*2) );
		if(r==0) 
			return -1;
		return 1;
	}

	/** This function generates a random solution  @return the random solution */
	public byte[] random(){
		byte sol[]=new byte[satData.getVariableNumber()];
		for(int i=0;i<satData.getVariableNumber();i++){ 
			sol[i]=rand();
		}
		return sol ;
	}
}