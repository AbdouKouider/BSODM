package bso;

import java.util.HashSet;

public class Clause {

	/**
	 *Attributes :
	 * 							  weight     =====> the weight of the clause
	 *    						  literals   =====> the literals of the clause
	 *  
	 */
	private int weight = 1; 
	private HashSet<Integer> literals= new HashSet<Integer>();
        int num;
	/**
	 * The default constructor for the class
	 */
	public Clause() {

	}

	/**
	 * This constructor creates a new Clause with 
	 * @param weight weight     =====> the weight of the clause
	 */
	public Clause(int num)//, int weight) //omitted by me because the weight is 1.
	{
            
                this.num = num;
                //this.weight = weight;
	}

	/**
	 * 
	 * @return the weight of the current clause
	 */
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight=weight;
	}


	/**
	 * 
	 * @return  the list of literals contained in the clause
	 */
	public HashSet<Integer> getLiterals() {
		return literals;
	}
	public int getLiteral(int i){
		return ((Integer)(literals.toArray())[i]).intValue();
		
	}

	/**
	 * This function add a new literal to the clause
	 * @param literal ====> the literal we want to add to the clause
	 */
	public void addliteral(int literal){
		
		literals.add(literal);
		
	}
	
	/**
	 * 
	 * @return the length of the clause
	 */
	public int length(){
		return literals.size();
	}
	public void setLiterals(HashSet<Integer> literals){
		this.literals = literals;
	}

        public void setNum(int n){
            num = n;
        }
        
        public int gtNum(int n){
            return num;
        }
        
}
