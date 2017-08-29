package toolsToManipulateSolutions;

import java.util.LinkedList;

import bso.SatData;
import bso.Solution;


public class UsefulMethods {
	
	/**@return compare int [] solutions if equals.*/
	public static void compare(LinkedList <byte []> m)  
	{
		System.out.println("compare byte[]");
		for(int i=0;i<m.size();i++)
		{
			for(int j=i+1;j<m.get(0).length;j++){
				boolean egal=true;
				for(int k=0;k<m.get(0).length;k++)
				{
					if(m.get(i)[k]!=m.get(j)[k]) 
						{
							egal=false;
						}
				}
				if(egal==true) System.out.println("egalite");
			}
		}
		System.out.println("end compare");
	}
	
	/**@return compare strings if equals */
	public static void compareString(LinkedList <String> m)
	{
		System.out.println("compare String");
		for(int i=0;i<m.size();i++)
		{
			for(int j=i+1;j<m.size();j++){
				boolean egal=false;
					if(m.get(i).equals(m.get(j))) 
						{
							egal=true;
						}
				if(egal==true) System.out.println("egalite String "+i+" "+j);
			}
		}
	}
	
	
	/**@return String to int array */
	static public byte[] stringToIntArray(String s)
	{
		
		String[] array = s.split(",");
		byte[] ints = new byte[array.length];
		for(int i=0; i<array.length; i++)
		{
		    try {
		        ints[i] = (byte) Integer.parseInt(array[i]);           
		    } catch (NumberFormatException nfe) {
		        //Not an integer 
		    }
		}
		return ints;
	}
	
	/**@return String to float array */
	static public float [] stringToFloatArray(String s)
	{
		
		String[] array = s.split(",");
		float[] floats = new float[array.length];
		for(int i=0; i<array.length; i++)
		{
		    try {
		        floats[i] = Float.parseFloat(array[i]);           
		    } catch (NumberFormatException nfe) { 
		    }
		}
		return floats;
	}

	/**@return convertit un tableau de byte en String */
	static public String byteArrayToString(byte [] sol){
		String solEnChaine="";
		
		for(int i=0;i< sol.length;i++){
			solEnChaine=solEnChaine+sol[i]+" ";
		}
		return solEnChaine;
	}
	 
	
	/**@return elle recoit une solution et une position a flipper -> elle retourne une copie de la nouvelle solution flippee */
	static public Solution flip(Solution solution,int i){
			
		    solution.getSolution()[i]=(byte) (solution.getSolution()[i]*-1);
			return (Solution) solution.clone();
		}

	/*static public VariableValeurInClassificationRule getRuleVariable(String line){
		VariableValeurInClassificationRule ruleVariable=new VariableValeurInClassificationRule();
		
		return ruleVariable;
	}*/
	
	/*static public String getRuleClass(String line){
		String classe = null;
		
		return classe;
	}*/
	
}
