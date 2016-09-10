package com.gmail.trentech.mysteryblock.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.block.BlockTypes;

import com.gmail.trentech.mysteryblock.Main;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigManager {
	
	private Path path;
	private CommentedConfigurationNode config;
	private ConfigurationLoader<CommentedConfigurationNode> loader;
	
	private static ConcurrentHashMap<String, ConfigManager> configManagers = new ConcurrentHashMap<>();

	private ConfigManager(String configName) {
		try {
			path = Main.instance().getPath().resolve(configName + ".conf");
			
			if (!Files.exists(path)) {		
				Files.createFile(path);
				Main.instance().getLog().info("Creating new " + path.getFileName() + " file...");
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}

		load();
	}
	
	public static ConfigManager get(String configName) {
		return configManagers.get(configName);
	}
	
	public static ConfigManager get() {
		return configManagers.get("config");
	}
	
	public static ConfigManager init() {
		return init("config");
	}
	
	public static ConfigManager init(String configName) {
		ConfigManager configManager = new ConfigManager(configName);
		CommentedConfigurationNode config = configManager.getConfig();

		if (config.getNode("blocks").isVirtual()) {
			if (config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId()).isVirtual()) {
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example1", "percentage").setValue(Double.valueOf(0.2D)).setComment("0.0 to 1.0");
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example1", "type").setValue("ITEM").setComment("ENTITY, ITEM or POTION");
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example1", "data").setValue("minecraft:apple");
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example1", "amplifier").setValue(Integer.valueOf(1));
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example1", "cancel").setValue(Boolean.valueOf(false));
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example2", "percentage").setValue(Double.valueOf(0.2D)).setComment("0.0 to 1.0");
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example2", "type").setValue("ITEM").setComment("ENTITY, ITEM or POTION");
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example2", "data").setValue("minecraft:golden_apple");
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example2", "amplifier").setValue(Integer.valueOf(1));
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example2", "cancel").setValue(Boolean.valueOf(false));
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example3", "percentage").setValue(Double.valueOf(0.2D)).setComment("0.0 to 1.0");
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example3", "type").setValue("ITEM").setComment("ENTITY, ITEM or POTION");
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example3", "data").setValue("minecraft:blaze_powder");
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example3", "amplifier").setValue(Integer.valueOf(1));
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example3", "cancel").setValue(Boolean.valueOf(false));
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example4", "percentage").setValue(Double.valueOf(0.2D)).setComment("0.0 to 1.0");
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example4", "type").setValue("ITEM").setComment("ENTITY, ITEM or POTION");
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example4", "data").setValue("minecraft:pumpkin");
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example4", "amplifier").setValue(Integer.valueOf(1));
				config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example4", "cancel").setValue(Boolean.valueOf(false));
			}
		}
		
		configManager.save();
		
		return configManager;
	}

	public CommentedConfigurationNode getConfig() {
		return config;
	}

	private void load() {
		loader = HoconConfigurationLoader.builder().setPath(path).build();
		
		try {
			config = loader.load();
		} catch (IOException e) {
			Main.instance().getLog().error("Failed to load config");
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			loader.save(config);
		} catch (IOException e) {
			Main.instance().getLog().error("Failed to save config");
			e.printStackTrace();
		}
	}
}