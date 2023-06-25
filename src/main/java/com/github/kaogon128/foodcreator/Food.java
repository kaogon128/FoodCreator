package com.github.kaogon128.foodcreator;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class Food {

    private final ShapedRecipe recipe;
    private final int foodLevel;
    private final String key;

    public Food(String foodName, ShapedRecipe recipe, int foodLevel) {
        this.recipe = recipe;
        this.foodLevel = foodLevel;
        this.key = foodName;
    }

    public ItemStack getItem() {
        return recipe.getResult();
    }

    public NamespacedKey getKey() {
        return recipe.getKey();
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public boolean isMatch(String key) {
        return this.key.equalsIgnoreCase(key);
    }

}
