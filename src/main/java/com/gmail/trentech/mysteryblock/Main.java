package com.gmail.trentech.mysteryblock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import com.gmail.trentech.mysteryblock.commands.CommandManager;
import com.gmail.trentech.mysteryblock.utils.ConfigManager;
import com.gmail.trentech.mysteryblock.utils.Resource;
import com.google.inject.Inject;

import me.flibio.updatifier.Updatifier;

@Updatifier(repoName = Resource.NAME, repoOwner = Resource.AUTHOR, version = Resource.VERSION)
@Plugin(id = Resource.ID, name = Resource.NAME, version = Resource.VERSION, description = Resource.DESCRIPTION, authors = Resource.AUTHOR, url = Resource.URL, dependencies = { @Dependency(id = "Updatifier", optional = true) })
public class Main {
	
	@Inject @ConfigDir(sharedRoot = false)
    private Path path;

	@Inject
	private Logger log;

	private static PluginContainer plugin;
	private static Main instance;
	
	@Listener
	public void onPreInitializationEvent(GamePreInitializationEvent event) {
		plugin = Sponge.getPluginManager().getPlugin(Resource.ID).get();
		instance = this;
		
		try {			
			Files.createDirectories(path);		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Listener
	public void onInitialization(GameInitializationEvent event) {
		Sponge.getEventManager().registerListeners(this, new EventManager());
		Sponge.getCommandManager().register(this, new CommandManager().cmdMysteryBlock, "mysteryblock", "mb");

		ConfigManager.init();
	}

	@Listener
	public void onStartedServer(GameStartedServerEvent event) {
		ConfigManager configManager = ConfigManager.get("entities");

		List<String> list = new ArrayList<>();

		for (EntityType entityType : Sponge.getRegistry().getAllOf(EntityType.class)) {
			if ((!entityType.equals(EntityTypes.HUMAN)) && (!entityType.equals(EntityTypes.PLAYER)) && (!entityType.equals(EntityTypes.ITEM))) {
				list.add(entityType.getId());
			}
		}
		
		configManager.getConfig().getNode("entities").setValue(list);
		configManager.save();
		
		configManager = ConfigManager.get("items");

		list = new ArrayList<>();

		for (ItemType itemType : Sponge.getRegistry().getAllOf(ItemType.class)) {
			if (!itemType.equals(ItemTypes.NONE)) {
				list.add(itemType.getId());
			}
		}
		
		configManager.getConfig().getNode("items").setValue(list);
		configManager.save();

		configManager = ConfigManager.get("blocks");

		list = new ArrayList<>();

		for (BlockType blockType : Sponge.getRegistry().getAllOf(BlockType.class)) {
			list.add(blockType.getId());
		}
		
		configManager.getConfig().getNode("blocks").setValue(list);
		configManager.save();
		
		configManager = ConfigManager.get("potions");

		list = new ArrayList<>();

		for (PotionEffectType effectType : Sponge.getRegistry().getAllOf(PotionEffectType.class)) {
			list.add(effectType.getId());
		}

		configManager.getConfig().getNode("potions").setValue(list);
		configManager.save();
	}

	public Logger getLog() {
		return log;
	}

	public Path getPath() {
		return path;
	}
	
	public static PluginContainer getPlugin() {
		return plugin;
	}
	
	public static Main instance() {
		return instance;
	}
}