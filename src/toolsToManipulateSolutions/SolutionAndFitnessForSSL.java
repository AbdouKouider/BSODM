package toolsToManipulateSolutions;

public class SolutionAndFitnessForSSL {

	private byte [] solution;
	private int fitness;
	
	public SolutionAndFitnessForSSL(byte [] solution, int fitness){
		this.solution=solution;
		this.fitness=fitness;
	}

	public int getFitness() {
		return fitness;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	public byte [] getSolution() {
		return solution;
	}

	public void setSolution(byte [] solution) {
		this.solution = solution;
	}

}
