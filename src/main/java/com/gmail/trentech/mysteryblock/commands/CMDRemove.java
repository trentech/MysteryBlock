package com.gmail.trentech.mysteryblock.commands;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.mysteryblock.utils.ConfigManager;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDRemove implements CommandExecutor {
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String child = args.<BlockType>getOne("block").get().getId();

		ConfigManager configManager = ConfigManager.get();
		ConfigurationNode parent = configManager.getConfig().getNode("blocks");
		ConfigurationNode node = parent.getNode(child);

		if (node.isVirtual()) {
			throw new CommandException(Text.of(TextColors.RED, "<block> ", child, " node does not exist"));
		}

		if (args.hasAny("name")) {
			String name = args.<String>getOne("name").get();

			parent = node;
			node = parent.getNode(name);

			if (node.isVirtual()) {
				throw new CommandException(Text.of(TextColors.RED, "<name> ", name, " node does not exist"));
			}
			child = name;
		}

		parent.removeChild(child);

		configManager.save();

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Mystery block removed"));

		return CommandResult.success();
	}
}