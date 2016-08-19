package com.gmail.trentech.mysteryblock.utils;

import java.io.File;
import java.io.IOException;

import org.spongepowered.api.block.BlockTypes;

import com.gmail.trentech.mysteryblock.Main;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigManager {
	
	private File file;
	private CommentedConfigurationNode config;
	private ConfigurationLoader<CommentedConfigurationNode> loader;

	public ConfigManager() {
		String folder = "config" + File.separator + Resource.ID;

		if (!new File(folder).isDirectory()) {
			new File(folder).mkdirs();
		}
		file = new File(folder, "config.conf");

		create();
		load();
	}

	public ConfigManager(String configName) {
		String folder = "config" + File.separator + Resource.ID;

		if (!new File(folder).isDirectory()) {
			new File(folder).mkdirs();
		}
		file = new File(folder, configName + ".conf");

		create();
		load();
	}

	public static ConfigManager get(String configName) {
		return new ConfigManager(configName);
	}

	public static ConfigManager get() {
		return new ConfigManager();
	}

	public CommentedConfigurationNode getConfig() {
		return config;
	}

	public void save() {
		try {
			loader.save(config);
		} catch (IOException e) {
			Main.getLog().error("Failed to save " + file.getName());
			e.printStackTrace();
		}
	}

	public ConfigManager init() {
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
		save();
		return this;
	}

	private void create() {
		if (!file.exists()) {
			Main.getLog().info("Creating file " + file.getName());
			try {
				file.createNewFile();
			} catch (IOException e) {
				Main.getLog().error("Failed to create " + file.getName());
				e.printStackTrace();
			}
		}
	}

	private void load() {
		loader = HoconConfigurationLoader.builder().setFile(file).build();
		try {
			config = loader.load();
		} catch (IOException e) {
			Main.getLog().error("Failed to load " + file.getName());
			e.printStackTrace();
		}
	}
}