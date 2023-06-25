package com.github.kaogon128.foodcreator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;

public class FoodUtil {

    public static ItemStack createHead(String foodName, FileConfiguration config) {
        UUID uuid = UUID.randomUUID();
        PlayerProfile profile = Bukkit.createPlayerProfile(uuid);
        PlayerTextures textures = profile.getTextures();
        try {
            String url = config.getString(foodName + ".Textures");
            if (url != null) {
                textures.setSkin(new URL(url));
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        profile.setTextures(textures);
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        Objects.requireNonNull(skullMeta).setOwnerProfile(profile);
        skullMeta.setDisplayName(config.getString(foodName + ".DisplayName"));
        skullMeta.setLore(config.getStringList(foodName + ".Lore"));
        PersistentDataContainer container = skullMeta.getPersistentDataContainer();
        container.set(FoodCreator.getInstance().getMatchKey(), PersistentDataType.STRING, foodName);
        head.setItemMeta(skullMeta);
        return head;
    }

    public static ShapedRecipe createRecipe(String foodName) {
        FileConfiguration config = FoodCreator.getInstance().getConfigManager().getConfig();
        NamespacedKey key = new NamespacedKey(FoodCreator.getInstance(), foodName);
        ItemStack item = createHead(foodName, config);
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        String top = Objects.requireNonNull(config.getString(foodName + ".Recipe.Top")).replace('N', ' ');
        String middle = Objects.requireNonNull(config.getString(foodName + ".Recipe.Middle")).replace('N', ' ');
        String bottom = Objects.requireNonNull(config.getString(foodName + ".Recipe.Bottom")).replace('N', ' ');
        recipe.shape(top, middle, bottom);
        for (String alphabet : config.getStringList(foodName + ".Recipe.Alphabets")) {
            recipe.setIngredient(alphabet.toCharArray()[0], new RecipeChoice.ExactChoice(new ItemStack(Material.valueOf(config.getString(foodName + ".Recipe." + alphabet)))));
        }
        return recipe;
    }
}
