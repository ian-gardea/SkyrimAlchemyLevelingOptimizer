package skyrimAlchemyLevelingOptimizer;

/**
 * This class represents a Recipe. By default, it will be created with a quantity of zero. 
 * A recipe contains a list of ingredients, and has a set value in gold. The quantity represents 
 * how many times this recipe has been created.
 * 
 * @author Ian Gardea
 * 
 */
public class Recipe {
	private String ingredients[] = new String[SkyrimAlchemyLevelingOptimizer.RECIPELENGTH];
	private int    value;
	private int    quantity;
	
	public Recipe(String myIngredients[], Integer myValue) {
		for(int i=0; i<ingredients.length; i++) {
			ingredients[i] = myIngredients[i].toLowerCase();
		}
		value       = myValue;
		quantity    = 0;
	}

	public String[] getIngredients() {
		return ingredients;
	}
	
	public int getValue() {
		return value;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void addQuantity() {
		quantity += 1;
	}
	
	public String toString() {
		String str = quantity + " x ";
		for(int i=0; i<ingredients.length; i++) {
			str += ingredients[i];
			if (i < ingredients.length-1) {
				str += ", ";
			}
		}
		return str;
	}
}
