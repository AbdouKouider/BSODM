package fIM_R;
public class ItemSupport  implements Comparable <ItemSupport>
{

	private String itemset; //l'ensemble d'items les plus fréquents.
	private double support; //le support de l'ensemble d'items les plus fréquents.
	private int nbrItemsInThisItemSet; //nombre d'éléments de cet ensemble d'items les plus fréquents.
	
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
	 * Cette méthode fait la comparaison de la manière suivante:
	 * l'objet qui a le plus grand support est le meilleur,puis s'il y a égalité celui qui a le plus grand itemset sera le meilleur.
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
				else // supports égaux
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
	
