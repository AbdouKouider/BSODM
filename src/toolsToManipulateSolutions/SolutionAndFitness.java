package toolsToManipulateSolutions;

public class SolutionAndFitness {

	private String solution;
	private int fitness;
	
	public SolutionAndFitness(String solution, int fitness){
		this.solution=solution;
		this.fitness=fitness;
	}

	public int getFitness() {
		return fitness;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}
}
