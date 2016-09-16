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

import com.gmail.trentech.mysteryblock.Type;
import com.gmail.trentech.mysteryblock.utils.ConfigManager;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDAdd implements CommandExecutor {
	
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		BlockType block = args.<BlockType>getOne("block").get();

		String name = args.<String>getOne("name").get();

		float percentage = (float) args.<Double>getOne("percentage").get().doubleValue();

		Type type = args.<Type>getOne("type").get();

		String data = args.<String>getOne("data").get();

		int amplifier = 1;
		
		if (args.hasAny("amplifier")) {
			amplifier = args.<Integer>getOne("amplifier").get();
		}

		boolean cancel = false;
		if (args.hasAny("c")) {
			cancel = true;
		}

		ConfigManager configManager = ConfigManager.get();
		ConfigurationNode node = configManager.getConfig().getNode("blocks", block.getId(), name);

		if (!node.isVirtual()) {
			throw new CommandException(Text.of(TextColors.RED, "<name> ", name, " already exists"));
		}
		
		if (type.equals(Type.ITEM)) {
			Optional<ItemType> optionalItemType = Sponge.getRegistry().getType(ItemType.class, data);

			if (!optionalItemType.isPresent()) {
				throw new CommandException(Text.of(TextColors.RED, "<data> ", data, " not a valid ItemType"));
			}
		} else if (type.equals(Type.ENTITY)) {
			Optional<EntityType> optionalEntityType = Sponge.getRegistry().getType(EntityType.class, data);

			if (!optionalEntityType.isPresent()) {
				throw new CommandException(Text.of(TextColors.RED, "<data> ", data, " not a valid EntityType"));
			}
		} else if (type.equals(Type.POTION)) {
			Optional<PotionEffectType> optionalPotionEffectType = Sponge.getRegistry().getType(PotionEffectType.class, data);

			if (!optionalPotionEffectType.isPresent()) {
				throw new CommandException(Text.of(TextColors.RED, "<data> ", data, " not a valid PotionEffectType"));
			}
		}

		node.getNode("percentage").setValue(percentage);
		node.getNode("type").setValue(type.getName());
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