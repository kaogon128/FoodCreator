package com.github.kaogon128.foodcreator;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigManager {

    private static FileConfiguration config = null;
    private static File configFile = null;

    public ConfigManager() {
    }

    public static void reloadConfig() {
        JavaPlugin plugin = FoodCreator.getInstance();
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        InputStream configStream = plugin.getResource("config.yml");
        if (configStream != null) {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new InputStreamReader(configStream));
            config.setDefaults(configuration);
        }
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    public void saveDefaultConfig() {
        JavaPlugin plugin = FoodCreator.getInstance();
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
    }
}
