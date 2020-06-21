package skyrimAlchemyLevelingOptimizer;

/**
 * This class represents an ingredient. An ingredient has a name, and a quantity representing
 * how many times this ingredients can be used to form a recipe.
 * 
 * @author Ian Gardea
 *
 */
public class Ingredient {
	private String name;
	private int    quantity;
	
	public Ingredient(String myName, Integer myQuantity) {
		name     = myName.toLowerCase();
		quantity = myQuantity;
	}
	
	public String getName() {
		return name;
	}

	public int getQuantity() {
		return quantity;
	}

	public boolean isDepleted() {
	   return quantity == 0;	
	}
	
	public void deductQuantity() {
		quantity -= 1;
	}
	
	public String toString() {
		String str = name + ": " + quantity;
		return str;
	}

}
