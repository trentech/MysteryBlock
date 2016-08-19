package com.gmail.trentech.mysteryblock.commands;

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
		if (!args.hasAny("block")) {
			src.sendMessage(Text.of(TextColors.RED, "<block> Not enough arguments"));
			src.sendMessage(Text.of(TextColors.YELLOW, "/mb remove <block> [name]"));
			return CommandResult.success();
		}
		String child = args.<String>getOne("block").get();

		ConfigManager configManager = ConfigManager.get();
		ConfigurationNode parent = configManager.getConfig().getNode("blocks");
		ConfigurationNode node = parent.getNode(child);

		if (node.isVirtual()) {
			src.sendMessage(Text.of(TextColors.RED, "<block> ", child, " node does not exist"));
			src.sendMessage(Text.of(TextColors.YELLOW, "/mb remove <block> [name]"));
			return CommandResult.success();
		}

		if (args.hasAny("name")) {
			String name = args.<String>getOne("name").get();

			parent = node;
			node = parent.getNode(name);

			if (node.isVirtual()) {
				src.sendMessage(Text.of(TextColors.RED, "<name> ", name, " node does not exist"));
				src.sendMessage(Text.of(TextColors.YELLOW, "/mb remove <block> [name]"));
				return CommandResult.success();
			}
			child = name;
		}

		parent.removeChild(child);

		configManager.save();

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Mystery block removed"));

		return CommandResult.success();
	}
}