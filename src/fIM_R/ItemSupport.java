package fIM_R;
public class ItemSupport  implements Comparable <ItemSupport>
{

	private String itemset; //l'ensemble d'items les plus fr�quents.
	private double support; //le support de l'ensemble d'items les plus fr�quents.
	private int nbrItemsInThisItemSet; //nombre d'�l�ments de cet ensemble d'items les plus fr�quents.
	
	public ItemSupport(String itemset,double support){
		this.itemset=itemset;
		this.support=support;
	}
	
	public ItemSupport(){
		
	}
	
	
	public int nbrItem(){
		String [] lesItems=itemset.split(",");
		return lesItems.length;
	}
	public String getItem() {
		return itemset;
	}
	public void setItem(String itemset) {
		this.itemset = itemset;
	}
	public double getSupport() {
		return support;
	}
	public void setSupport(double support) {
		this.support = support;
	}
	public int getNbrItemsInThisItemSet() {
		return nbrItemsInThisItemSet;
	}
	public void setNbrItemsInThisItemSet(int nbrItemsInThisItemSet) {
		this.nbrItemsInThisItemSet = nbrItemsInThisItemSet;
	}
	
	/***
	 * Cette m�thode fait la comparaison de la mani�re suivante:
	 * l'objet qui a le plus grand support est le meilleur,puis s'il y a �galit� celui qui a le plus grand itemset sera le meilleur.
	 */
	public int compareTo(ItemSupport o) { 
			if( this.getSupport()>o.getSupport())
				{
				return -1;
				}
			else{
				if(this.getSupport()<o.getSupport()){
					return 1;
				}
				else // supports �gaux
				{
					if(this.getNbrItemsInThisItemSet()>o.getNbrItemsInThisItemSet()) return -1;
					else{
						if(this.getNbrItemsInThisItemSet()<o.getNbrItemsInThisItemSet()) return 1;
						else return 0;
					   }
				}
			}
			
		
		}

	}
	
