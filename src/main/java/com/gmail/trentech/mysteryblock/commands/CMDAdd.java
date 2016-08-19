package com.gmail.trentech.mysteryblock.commands;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.mysteryblock.utils.ConfigManager;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDAdd implements CommandExecutor {
	
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!args.hasAny("block")) {
			src.sendMessage(Text.of(TextColors.RED, "<block> Not enough arguments"));
			src.sendMessage(Text.of(TextColors.YELLOW, "/mb add <block> <name> <percentage> <type> <data> [-a <amplifier>] [-c]"));
			return CommandResult.success();
		}
		String block = args.<String>getOne("block").get();

		if (!args.hasAny("name")) {
			src.sendMessage(Text.of(TextColors.RED, "<name> Not enough arguments"));
			src.sendMessage(Text.of(TextColors.YELLOW, "/mb add <block> <name> <percentage> <type> <data> [-a <amplifier>] [-c]"));
			return CommandResult.success();
		}
		String name = args.<String>getOne("name").get();

		if (!args.hasAny("percentage")) {
			src.sendMessage(Text.of(TextColors.RED, "<percentage> Not enough arguments"));
			src.sendMessage(Text.of(TextColors.YELLOW, "/mb add <block> <name> <percentage> <type> <data> [-a <amplifier>] [-c]"));
			return CommandResult.success();
		}
		String argPercentage = args.<String>getOne("percentage").get();

		if (!args.hasAny("type")) {
			src.sendMessage(Text.of(TextColors.RED, "<type> Not enough arguments"));
			src.sendMessage(Text.of(TextColors.YELLOW, "/mb add <block> <name> <percentage> <type> <data> [-a <amplifier>] [-c]"));
			return CommandResult.success();
		}
		String type = args.<String>getOne("type").get().toUpperCase();

		if (!args.hasAny("data")) {
			src.sendMessage(Text.of(TextColors.RED, "<data> Not enough arguments"));
			src.sendMessage(Text.of(TextColors.YELLOW, "/mb add <block> <name> <percentage> <type> <data> [-a <amplifier>] [-c]"));
			return CommandResult.success();
		}
		String data = args.<String>getOne("data").get();

		int amplifier = 1;
		if (args.hasAny("amplifier")) {
			String argAmplifier = args.<String>getOne("amplifier").get();
			try {
				amplifier = Integer.parseInt(argAmplifier);
			} catch (Exception e) {
				src.sendMessage(Text.of(TextColors.RED, "<amplifier> ", argAmplifier, " not a valid integer"));
				src.sendMessage(Text.of(TextColors.YELLOW, "/mb add <block> <name> <percentage> <type> <data> [-a <amplifier>] [-c]"));
				return CommandResult.success();
			}
		}

		boolean cancel = false;
		if (args.hasAny("c")) {
			cancel = true;
		}

		Optional<BlockType> optionalBlockType = Sponge.getRegistry().getType(BlockType.class, block);

		if (!optionalBlockType.isPresent()) {
			src.sendMessage(Text.of(TextColors.RED, "<block> ", block, " not a valid BlockType"));
			src.sendMessage(Text.of(TextColors.YELLOW, "/mb add <block> <name> <percentage> <type> <data> [-a <amplifier>] [-c]"));
			return CommandResult.success();
		}

		ConfigManager configManager = ConfigManager.get();
		ConfigurationNode node = configManager.getConfig().getNode("blocks", block, name);

		if (!node.isVirtual()) {
			src.sendMessage(Text.of(TextColors.RED, "<name> ", name, " already exists"));
			src.sendMessage(Text.of(TextColors.YELLOW, "/mb add <block> <name> <percentage> <type> <data> [-a <amplifier>] [-c]"));
			return CommandResult.success();
		}
		
		float percentage;
		try {
			percentage = Float.parseFloat(argPercentage);
		} catch (Exception e) {
			src.sendMessage(Text.of(TextColors.RED, "<percentage> ", argPercentage, " not a valid float"));
			src.sendMessage(Text.of(TextColors.YELLOW, "/mb add <block> <name> <percentage> <type> <data> [-a <amplifier>] [-c]"));
			return CommandResult.success();
		}
		
		if (type.equals("ITEM")) {
			Optional<ItemType> optionalItemType = Sponge.getRegistry().getType(ItemType.class, data);

			if (!optionalItemType.isPresent()) {
				src.sendMessage(Text.of(TextColors.RED, "<data> ", data, " not a valid ItemType"));
				src.sendMessage(Text.of(TextColors.YELLOW, "/mb add <block> <name> <percentage> <type> <data> [-a <amplifier>] [-c]"));
				return CommandResult.success();
			}
		} else if (type.equals("ENTITY")) {
			Optional<EntityType> optionalEntityType = Sponge.getRegistry().getType(EntityType.class, data);

			if (!optionalEntityType.isPresent()) {
				src.sendMessage(Text.of(TextColors.RED, "<data> ", data, " not a valid EntityType"));
				src.sendMessage(Text.of(TextColors.YELLOW, "/mb add <block> <name> <percentage> <type> <data> [-a <amplifier>] [-c]"));
				return CommandResult.success();
			}
		} else if (type.equals("POTION")) {
			Optional<PotionEffectType> optionalPotionEffectType = Sponge.getRegistry().getType(PotionEffectType.class, data);

			if (!optionalPotionEffectType.isPresent()) {
				src.sendMessage(Text.of(TextColors.RED, "<data> ", data, " not a valid PotionEffectType"));
				src.sendMessage(Text.of(TextColors.YELLOW, "/mb add <block> <name> <percentage> <type> <data> [-a <amplifier>] [-c]"));
				return CommandResult.success();
			}
		}

		node.getNode("percentage").setValue(Float.valueOf(percentage));
		node.getNode("type").setValue(type);
		node.getNode("data").setValue(data);
		node.getNode("amplifier").setValue(Integer.valueOf(amplifier));
		node.getNode("cancel").setValue(Boolean.valueOf(cancel));

		configManager.save();

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Mystery block created"));

		src.sendMessage(Text.of(TextColors.GREEN, "Block: ", TextColors.WHITE, block));
		src.sendMessage(Text.of(TextColors.GREEN, "  Name: ", TextColors.WHITE, name));
		src.sendMessage(Text.of(TextColors.GREEN, "    Percentage: ", TextColors.WHITE, Float.valueOf(percentage)));
		src.sendMessage(Text.of(TextColors.GREEN, "    Type: ", TextColors.WHITE, type));
		src.sendMessage(Text.of(TextColors.GREEN, "    Data: ", TextColors.WHITE, data));
		src.sendMessage(Text.of(TextColors.GREEN, "    Amplifier: ", TextColors.WHITE, Integer.valueOf(amplifier)));
		src.sendMessage(Text.of(TextColors.GREEN, "    Cancel: ", TextColors.WHITE, Boolean.valueOf(cancel)));

		return CommandResult.success();
	}
}