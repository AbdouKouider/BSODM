package bso;

import bso_dm_2017.FirstController;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import toolsToManipulateSolutions.UsefulMethods;


public class SwarmClassic extends Swarm {

	protected int flip; // number of variable to inverse

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public SwarmClassic(SatData satData, int heuristic, int maxIter,
			int nbSearchIter, int nBees, int flip, int chance) {
		super(satData, heuristic, maxIter, nbSearchIter, nBees, chance);
		this.flip = flip;
		// TODO Auto-generated constructor stub
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public SwarmClassic() {
		// TODO Auto-generated constructor stub
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
		System.out.println("bso  classic");
			int i;
			

			for (iter = 1; iter <= maxIter && bestSol.getFitness() < satData.getOptimum(); iter++) {
				setArea();
				taboo[iter - 1] = (Solution) solRef.clone();			
				for (i = 0; i < bees.length; i++) {
					bees[i] = new Bee(satData, nbSearchIter, i);
					bees[i].setSol(area[i]);
					bees[i].search();
					dance[i] = bees[i].dance();
				}
				Arrays.sort(dance);
				
				/////////////////////////////////////////////////////////////////////
		
				////////////////////////////////////////////////////////////////////

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
            //System.out.println("solRef "+solRef.getFitness());
				lastVal = solRef.getFitness();
			}
			//by me
			System.out.println("best: "+bestSol.getFitness());
			FirstController.best=bestSol.getFitness();
			//by me

	

	}
}

// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

