package com.github.kaogon128.foodcreator;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class FoodCreator extends JavaPlugin {

    private static FoodCreator instance;
    private ConfigManager configManager;
    private final List<Food> foodList = new ArrayList<>();
    private NamespacedKey matchKey;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        this.matchKey = new NamespacedKey(this, "foodKey");
        this.configManager = new ConfigManager();
        configManager.saveDefaultConfig();
        for (String foodName : configManager.getConfig().getStringList("Foods")) {
            ShapedRecipe recipe = FoodUtil.createRecipe(foodName);
            Bukkit.addRecipe(recipe);
            foodList.add(new Food(foodName, recipe, configManager.getConfig().getInt(foodName + ".FoodLevel")));
        }
        getServer().getPluginManager().registerEvents(new SimpleListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Food food : foodList) {
            Bukkit.removeRecipe(food.getKey());
        }
    }

    public static FoodCreator getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public NamespacedKey getMatchKey() {
        return matchKey;
    }

    public Food getFood(ItemStack hand) {
        ItemMeta meta = hand.getItemMeta();
        if (meta == null) return null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(matchKey, PersistentDataType.STRING)) return null;
        String key = container.get(matchKey, PersistentDataType.STRING);
        for (Food food : foodList) {
            if (food.isMatch(key)) {
                return food;
            }
        }
        return null;
    }
}
