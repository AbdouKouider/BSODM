package bso;

import java.util.*;

import java.io.*;

public class SatData {

	private int variableNumber; // ====> the number of variable in the instance
	private boolean ERROR; // ====> shows if an error occurred while reading the
							// file
	private ArrayList<Clause> clauses = new ArrayList<Clause>(); // ====> the lists of clauses contained in the sat instance
	private long optimum = 0; // ====> the sum of all the clauses weight

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	
	public SatData(File file) {

		Clause clause;
		BufferedReader buffer; // the buffer we are going to read from.
		String line; // the line we read in from the buffer.
		StringTokenizer st; // tokenizer to split the file lines.

		char[] firstChar = new char[1]; // the char array we are gonna use to clause the first cahr of a line.
		try {

			buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			do {
				line = buffer.readLine();
				line.getChars(0, 1, firstChar, 0);
				// System.out.println(firstChar[0]);
			} while (firstChar[0] == 'c'); //ignorer les lignes commençant par 'c'

			st = new StringTokenizer(line);
			st.nextElement(); // by me , c'est la caractere 'p'
			st.nextElement(); // by me , c'est la chaine 'cnf'
			this.variableNumber = Integer.parseInt(st.nextToken());
			
			int variable;
			// for each elements in the line
			clause = new Clause();
			ArrayList<Clause> clauses = new ArrayList<Clause>();
			int i = 0, j = 0;
			line = buffer.readLine();
			do {
				clause.setNum(i);
				st = new StringTokenizer(line);
				while (st.hasMoreTokens()) {
					
						// we cast it into an integer
						variable = Integer.parseInt(st.nextToken());
						if (variable != 0) {
							clause.addliteral(variable);
							j++;

						} else {
							clauses.add(clause);
							clause = new Clause();

							i++;
						}
				}
				this.clauses = clauses;

				// we do this while there is lines in the file we didn't read
			} while ((line = buffer.readLine()) != null);
			optimum();

			ERROR = false;
		} catch (Exception e) {
			ERROR = true;
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public SatData() {

	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This function calculates the fitness value of a solution according to the
	 * instance
	 * 
	 * @param sol
	 *            ====> the solution to evaluate
	 * @return the fitness value
	 */
	public int fitness(byte[] sol) {
		int fitnessValue = 0;
		for (int i = 0; i < clauses.size(); i++) {
			for (int j = 0; j < clauses.get(i).length(); j++) {
				if (clauses.get(i).getLiteral(j)* sol[Math.abs(clauses.get(i).getLiteral(j)) - 1] > 0) {
					fitnessValue = fitnessValue + clauses.get(i).getWeight();
					break;
				}
			}

		}
		return fitnessValue;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This function calculates the optimum weight for the instance and put it
	 * in the variable optimum
	 */
	private void optimum() {
		/*
		 * for (int j = 0; j<clauses.size() ; j++){ optimum +=
		 * clauses.get(j).getWeight();
		 */

		// Dans MAX-SAT OPTIMUM = NOMBRE DE CLAUSES

		// optimum = clauses.size(); // omitted by me

		for (int j = 0; j < clauses.size(); j++) {
			optimum += clauses.get(j).getWeight();
		}
		System.out.println("L'optimum est: " + optimum);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** @return the number of variable */
	public int getVariableNumber() {
		return variableNumber;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This function returns the i th clause of the instance
	 * 
	 * @param i
	 *            ====> the index of the clause th return
	 * @return the clause in the position i
	 */

	public Clause getClause(int i) {
		return clauses.get(i);
	}
	
	public ArrayList<Clause> getClauses() {
		return clauses;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** @return the number of clause in the instance */
	public int getClauseCount() {
		return clauses.size();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** @return the value of the variable optimum */
	public long getOptimum() {
		return optimum;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/** This function prints on the console the sat instance */
	public void print() {
		// System.out.println(ERROR);
		System.out.println(variableNumber + " " + clauses.size());
		for (int i = 0; i < clauses.size(); i++) {
			System.out.print(clauses.get(i).getWeight() + " | ");
			for (int j = 0; j < clauses.get(i).length(); j++) {
				System.out.print(clauses.get(i).getLiteral(j) + " | ");
			}
			System.out.println("");
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void printInstance() {
		System.out.println(" nbVar = " + variableNumber + " nbClauses = "
				+ clauses.size());

		System.out.println(clauses.toString());
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void setVariableNumber(int variableNumber) {
		// TODO Auto-generated method stub
		this.variableNumber = variableNumber;

	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void addClause(Clause clause) {
		// TODO Auto-generated method stub
		clauses.add(clause);
		optimum();

	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
