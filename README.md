# SkyrimAlchemyLevelingOptimizer

**Written and designed by:** Ian A. Gardea

---

**Requirements:** 
Java

**Description:** 
This program is a crafting aid for the Elder Scrolls V: Skyrim. It will generate a list of potions based on a provided list of ingredients 
defined in the config.ini file. The list of potions is generated to optimally level the Alchemy skill by listing the least valuable recipe first, leading up to the most valuable. This is because the higher level your Alchemy skill is, the more experience you get for 
high-valued potions, so the experience exponentially grows using this method.
 
The algorithm will assign the ingredient to the highest value potion it can find first, so the generated list is optimal.

---

**Instructions:**
Edit the config.ini file with the quantity of the ingredients you have in stock that you want to use in an
alchemy crafting session.

Click on the .jar file.

Open the output.txt file to view the optimized list of potions that you can create.

The list of recipes are also located in th config.ini file, and likely is not complete. You can add to it, so long as the following format is followed:

For ingredients:
In the [ingredients] section of the config.ini file, add
youringredientname = quantity

For potions:
In the [potions] section of the config.ini file, add
ingrdient1, ingredient2, ingredient3 = goldvalue

**Note:** 
The gold value is a weight the algorithm uses to prioritize creating the potion, so it MUST be unique.

If you wish to craft only the highest value potions instead, then follow the generated list in descending order.

########################################################################################
