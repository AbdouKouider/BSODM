package toolsToManipulateSolutions;

import java.util.LinkedList;

public class ClassificationRule {
	
	private LinkedList <VariableValeurInClassificationRule> listDesConditions;
	private String classe;            

	public ClassificationRule(){
		this.listDesConditions=new LinkedList <VariableValeurInClassificationRule> ();
		this.classe=new String();
	}
	
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public LinkedList <VariableValeurInClassificationRule> getListDesConditions() {
		return listDesConditions;
	}
	public void setListDesConditions(LinkedList <VariableValeurInClassificationRule> listDesConditions) {
		this.listDesConditions = listDesConditions;
	}
	
}
