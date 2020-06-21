package skyrimAlchemyLevelingOptimizer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * This program is a crafting aid for the Elder Scrolls V: Skyrim. It will generate a list of potions based on a provided list of ingredients 
 * defined in the config.ini file. The list of potions is generated to optimally level the Alchemy skill by listing the least valuable recipe 
 * first, leading up to the most valuable. This is because the higher level your Alchemy skill is, the more experience you get for 
 * high-valued potions, so the experience exponentially grows using this method.
 *  
 * The algorithm will assign the ingredient to the highest value potion it can find first, so the generated list is optimal.
 * 
 * Note: If you wish to craft only the highest value potions instead, then follow the generated list in descending order.
 * 
 * The list of recipes are also located in th config.ini file, and likely is not complete. You can add to it, so long as the following
 * format is followed:
 * 
 * For ingredients:
 * In the [ingredients] section of the config.ini file, add
 * youringredientname = quantity
 * 
 * For potions:
 * In the [potions] section of the config.ini file, add
 * ingrdient1, ingredient2, ingredient3 = goldvalue
 * 
 * Note: The gold value is a weight the algorithm uses to prioritize creating the potion, so it MUST be unique.
 * 
 * @author Ian Gardea
 *
 */
public class SkyrimAlchemyLevelingOptimizer {
	public      static final int                     RECIPELENGTH = 3;
	public      static final String                  NONE         = "none";
	public      static final String                  OUTPUTFILE   = "output.txt";
	private                  IniParser               iniConfig;
	private                  Map<String, Ingredient> iniIngredients;
	private                  Map<Integer, Recipe>    iniRecipes;
	PrintWriter              printWriter;

	// main program flow.
	public SkyrimAlchemyLevelingOptimizer() {
		try {
			// create output file
		    FileWriter  fileWriter  = new FileWriter(SkyrimAlchemyLevelingOptimizer.OUTPUTFILE);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		
		    // run routine
			readConfigFile();
			getData();
			runCalculation();
			
			// show results, starting with the least valuable potion
			String results = null;
			Map<Integer, Recipe> iniSortedPotions = new TreeMap<Integer, Recipe>(iniRecipes);
			for (Integer pkey : iniSortedPotions.keySet()) {
				// if the recipe was able to be created.
				if(iniRecipes.get(pkey).getQuantity() > 0) {
					if (results == null) {
						results = iniSortedPotions.get(pkey) + "\n";
					}
					else {
						results += iniSortedPotions.get(pkey) + "\n";
					}
					
				}
			}
			if (results == null) {
				results = "No matching potions found.";
			}
			else {
				results = "For optimal leveling, you can craft the following with your provided ingredients: \n" + results;
			}
		    printWriter.print(results);
		    printWriter.close();		
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readConfigFile() {
        // Initialize file pointers.
		try {       
			iniConfig = new IniParser("./config.ini");
		}
		catch (IOException ex) {
			String iniErrMessage = "An error occurred reading the INI file. " + ex.getLocalizedMessage();
			printWriter.print(iniErrMessage);
			System.exit(1);
		}
		catch (NullPointerException ex) {
			String iniErrMessage = "An error occurred when trying to load INI file key \"" + iniConfig.getLastKey() + "\".";
			printWriter.print(iniErrMessage);
			System.exit(1);
		}
		catch (Exception ex) {
			String iniErrMessage = "An unknown error occurred reading the INI file. " + ex.getLocalizedMessage();
			printWriter.print(iniErrMessage);
			System.exit(1);
		}
	}

	public void getData() {
		// read ingredient list.
		try {
			iniIngredients = iniConfig.getIngredientMap();
		}
		catch (Exception ex) {
			String errMessage = "An error occurred getting the ingredients list. Check the formatting in the ini file. " + ex.getLocalizedMessage();
			printWriter.print(errMessage);
			System.exit(1);
		}

		// read recipe list
		try {
			iniRecipes = iniConfig.getRecipeMap();
		}
		catch (Exception ex) {
			String errMessage = "An error occurred getting the potions list. Check the formatting in the ini file. " + ex.getLocalizedMessage();
			printWriter.print(errMessage);
			System.exit(1);
		}
	}
	
	public void runCalculation() {
		String  ingredients[] = new String[SkyrimAlchemyLevelingOptimizer.RECIPELENGTH];
		boolean keepGoing     = true;

		// Make a sorted list of recipes so we process the most valuable ones first.
		Map<Integer, Recipe> iniSortedRecipes = new TreeMap<Integer, Recipe>(Collections.reverseOrder());
		iniSortedRecipes.putAll(iniRecipes);
		
		for (Integer pkey : iniSortedRecipes.keySet()) {
			// get the ingredients required for this recipe.
			ingredients = iniSortedRecipes.get(pkey).getIngredients();
			
			// keep checking the ingredient quantities, and see if the recipe can be made.
			keepGoing = true;
			while(keepGoing) {
				for(int i=0; i<ingredients.length; i++) {
					try {	
						if (iniIngredients.get(ingredients[i]).isDepleted()) {
							// Cannot make this recipe.
							keepGoing = false;
						}
					}
					// The ingredient was not in the list.
					catch (NullPointerException e) {
						keepGoing = false;
					}
					// Something terrible happened...so...skip?
					catch (Exception e) {
						keepGoing = false;
					}
				}		
				
				// Did we make a potion?
				if (keepGoing) {
					// decrement ingredient count.
					for (int i=0; i<ingredients.length; i++) {
						iniIngredients.get(ingredients[i]).deductQuantity();
					}
					// increment potion count.
					iniRecipes.get(pkey).addQuantity();
				}
			}
		}
	}
	
	// program entry point.
	public static void main(String args[]) {
		new SkyrimAlchemyLevelingOptimizer();
	}
}