package com.gmail.trentech.mysteryblock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import com.gmail.trentech.mysteryblock.commands.CommandManager;
import com.gmail.trentech.mysteryblock.utils.CommandHelp;
import com.gmail.trentech.mysteryblock.utils.ConfigManager;
import com.gmail.trentech.mysteryblock.utils.Resource;
import com.google.inject.Inject;

import me.flibio.updatifier.Updatifier;

@Updatifier(repoName = Resource.NAME, repoOwner = Resource.AUTHOR, version = Resource.VERSION)
@Plugin(id = Resource.ID, name = Resource.NAME, version = Resource.VERSION, description = Resource.DESCRIPTION, authors = Resource.AUTHOR, url = Resource.URL, dependencies = { @Dependency(id = "Updatifier", optional = true), @Dependency(id = "helpme", version = "0.2.1", optional = true) })
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
		
		CommandHelp.init();
	}

	@Listener
	public void onStartedServer(GameStartedServerEvent event) {
		ConfigManager.init("entities");
		ConfigManager.init("items");
		ConfigManager.init("blocks");
		ConfigManager.init("potions");
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