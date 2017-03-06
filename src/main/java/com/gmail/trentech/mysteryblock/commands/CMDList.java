package com.gmail.trentech.mysteryblock.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.mysteryblock.Main;
import com.gmail.trentech.pjc.core.ConfigManager;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDList implements CommandExecutor {
	
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		List<Text> list = new ArrayList<>();

		ConfigurationNode node = ConfigManager.get(Main.getPlugin()).getConfig().getNode("blocks");

		int line = 0;
		for (Entry<Object, ? extends ConfigurationNode> child : node.getChildrenMap().entrySet()) {
			list.add(Text.of(TextColors.GREEN, "Block: ", TextColors.WHITE, child.getKey()));
			line++;

			for (Entry<Object, ? extends ConfigurationNode> child1 : child.getValue().getChildrenMap().entrySet()) {
				if (line + 6 > 18) {
					int spaces = 18 - line;
					while (spaces != 0) {
						if ((src instanceof Player)) {
							list.add(Text.of(TextColors.GREEN, "***************************************************************"));
						}
						spaces--;
					}
					line = 0;
				}

				list.add(Text.of(TextColors.GREEN, "  Name: ", TextColors.WHITE, child1.getKey()));
				list.add(Text.of(TextColors.GREEN, "    Percentage: ", TextColors.WHITE, Float.valueOf(((ConfigurationNode) child1.getValue()).getNode("percentage").getFloat())));
				list.add(Text.of(TextColors.GREEN, "    Type: ", TextColors.WHITE, ((ConfigurationNode) child1.getValue()).getNode("type").getString().toUpperCase()));
				list.add(Text.of(TextColors.GREEN, "    Data: ", TextColors.WHITE, ((ConfigurationNode) child1.getValue()).getNode("data").getString()));
				list.add(Text.of(TextColors.GREEN, "    Amplifier: ", TextColors.WHITE, Integer.valueOf(((ConfigurationNode) child1.getValue()).getNode("amplifier").getInt())));
				list.add(Text.of(TextColors.GREEN, "    Cancel: ", TextColors.WHITE, Boolean.valueOf(((ConfigurationNode) child1.getValue()).getNode("cancel").getBoolean())));

				line += 6;
			}

			while (line < 18) {
				if ((src instanceof Player)) {
					list.add(Text.of(TextColors.GREEN, "***************************************************************"));
				}
				line++;
			}
			line = 0;
		}

		if (src instanceof Player) {
			PaginationList.Builder pages = PaginationList.builder();

			pages.linesPerPage(20).title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Blocks")).build());

			pages.contents(list);

			pages.sendTo(src);
		} else {
			for (Text text : list) {
				src.sendMessage(text);
			}
		}

		return CommandResult.success();
	}
}