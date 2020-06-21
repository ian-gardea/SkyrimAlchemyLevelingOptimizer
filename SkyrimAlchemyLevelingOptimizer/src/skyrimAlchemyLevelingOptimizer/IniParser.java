package skyrimAlchemyLevelingOptimizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class will read an ini file, and extract the values in the provided
 * section, and create a nested hash-map so that keys within a section can be referenced, 
 * and extracted by name.
 * 
 * @author Ian Gardea
 *
 */
public class IniParser {

	private Pattern _section = Pattern.compile("\\s*\\[([^]]*)\\]\\s*");
	private Pattern _keyValue = Pattern.compile("\\s*([^=]*)=(.*)");
	private Map<String, Map<String, String>> _entries = new HashMap<>();

	private String lastKey; // Track which key was accessed.

	public IniParser(String path) throws IOException {
		load(path);
	}

	public void load(String path) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line;
			String section = null;

			// parse the file line by line
			while ((line = br.readLine()) != null) {
				Matcher m = _section.matcher(line);
				// is the current line our target section?
				if (m.matches()) {
					// mark its location for future reference.
					section = m.group(1).trim();
				}
				// if we are in our target section
				else if (section != null) {
					// does the line match the regex pattern?
					m = _keyValue.matcher(line);
					if (m.matches()) {
						// found a valid key/value pair, extract each key and value.
						String key = m.group(1).trim();
						String value = m.group(2).trim();
						// add the key/value pair to the map
						Map<String, String> kv = _entries.get(section);
						if (kv == null) {
							_entries.put(section, kv = new HashMap<>());
						}
						if (key != null && !key.isEmpty()) {
							kv.put(key, value);
						}
					}
				}
			}
		}
	}

	public Map<String, Ingredient> getIngredientMap() {
		// get all entries in the "ingredients" section of the ini.
		Map<String, String> kv = _entries.get("ingredients");
		Map<String, Ingredient> im = new HashMap<>();
		
		String str = null;
		for (String key : kv.keySet()) {
			str = key.toLowerCase();
			// The map's key is the ingredient name.
			im.put(str, new Ingredient(str, Integer.parseInt(kv.get(key))));
		}
		
		return im;
	}

	public Map<Integer, Recipe> getRecipeMap() {
		// get all entries in the "potions" section of the ini.
		Map<String, String> kv = _entries.get("potions");
		Map<Integer, Recipe> pm = new HashMap<>();
		
		String str = null;
		for (String key : kv.keySet()) {
			str = key.toLowerCase().replaceAll("\\s","");
			String[] ingredients = str.split(",", SkyrimAlchemyLevelingOptimizer.RECIPELENGTH);
			// The map's key is the recipe's value.
			pm.put(Integer.parseInt(kv.get(key)), new Recipe(ingredients, Integer.parseInt(kv.get(key))));
		}
		
		return pm;
	}	

	public String getString(String section, String key, String defaultvalue) {
		Map<String, String> kv = _entries.get(section);
		if (kv == null) {
			return defaultvalue;
		}
		return kv.get(key);
	}

	public int getInt(String section, String key, int defaultvalue) {
		Map<String, String> kv = _entries.get(section);
		if (kv == null) {
			return defaultvalue;
		}
		return Integer.parseInt(kv.get(key));
	}

	public float getFloat(String section, String key, float defaultvalue) {
		Map<String, String> kv = _entries.get(section);
		if (kv == null) {
			return defaultvalue;
		}
		return Float.parseFloat(kv.get(key));
	}

	public double getDouble(String section, String key, double defaultvalue) {
		Map<String, String> kv = _entries.get(section);
		if (kv == null) {
			return defaultvalue;
		}
		return Double.parseDouble(kv.get(key));
	}

	public String getLastKey() {
		return lastKey;
	}
}