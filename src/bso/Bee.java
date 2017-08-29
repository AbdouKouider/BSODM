package bso;



public class Bee //implements Runnable 
{
	/**
	 * Attributes :
	 * 
	 * SatData satData =====> the Sat instance. 
	 * int sol[] =====> the solution affected to the bee. 
	 * int qualite =====> the quality of the solution.
	 * affected to the be.
	 * int max =====> max iteration if the research.
	 */
	protected SatData satData; // the Sat instance
	protected Solution sol;
	protected int max; // max iteration of the research
	protected int beeNum;

	/**
	 * Constructor using field
	 * 
	 * @param satData
	 * @param max
	 */
	public Bee(SatData satData, int max, int beeNum) {
		this.satData = satData;
		this.max = max;
		this.beeNum = beeNum;

	}

	protected Bee() {

	}

	/**
	 * This function performs the local bee search
	 */
	public void search() {
		// method 2
		int i,best, k = 0;
		best = sol.getFitness();
		Solution lastSol = sol; // by me
		int lastSolFitness = sol.getFitness(); // by me.
		boolean changement=false;  //by me
		while (k++ <= max && changement==false) 
		{
			for (i = 0; i < satData.getVariableNumber(); i++)
			{
				sol.flip(i);
				int fitness=satData.fitness(sol.getSolution());
				sol.setFitness(fitness);
				if(((fitness*100)/satData.getOptimum())<Main.minimumQuality) Main.minimumQuality=(int)((fitness*100)/satData.getOptimum());
				if(((fitness*100)/satData.getOptimum())>Main.maximumQuality)   Main.maximumQuality=(int)((fitness*100)/satData.getOptimum());
				if (sol.getFitness() > best) 
				{
					changement=true; 
					lastSol = sol; // by me.
					lastSolFitness = sol.getFitness(); // by me.
					best = sol.getFitness();
				}
				else 
				{
					sol.flip(i);
					sol.setFitness(lastSolFitness);
				}
			}
			if(changement == true) changement=false; //pour lui pousser à poursuivre la recherche car il y a quelque améliorations.
			else changement=true;                    //pour lui pousser à terminer la recherche car il n y a pas d'améliorations.
		}
		
	}

	/**
	 * This function returns the best solution found
	 * 
	 * @return
	 */
	public Solution dance() {
		return sol;
	}

	/**
	 * This function is a setter for the attribute sol
	 * 
	 * @param sol
	 */
	public void setSol(Solution sol) {
		// TODO Auto-generated method stub
		this.sol = sol;
	}

	public Solution getSolution() {
		return this.sol;
	}

}