package com.gmail.trentech.mysteryblock.init;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import com.gmail.trentech.mysteryblock.Main;
import com.gmail.trentech.pjc.core.ConfigManager;
import com.gmail.trentech.pjc.help.Argument;
import com.gmail.trentech.pjc.help.Help;
import com.gmail.trentech.pjc.help.Usage;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class Common {

	public static void init() {
		initConfig(Main.getPlugin().getId());
		initHelp();
	}
	
	public static void initHelp() {
		Usage usageAdd = new Usage(Argument.of("<block>", "Specifies to id of the BlockType"))
				.addArgument(Argument.of("<name>", "Spcifies the new name of the event"))
				.addArgument(Argument.of("<percentage>", "The percent chance of triggering this event. Value should be 0-1"))
				.addArgument(Argument.of("<type>", "Specifies the event type. ITEM spawns item, ENTITY spawns entity,POTION adds potion effect to the triggerer"))
				.addArgument(Argument.of("<data>", "Arugment depends on <type>. ITEM = ItemType, ENTITY = EntityType, POTION = PotionEffectType"))
				.addArgument(Argument.of("[-a <amplifier>]", "Arugment depends on <type>. ITEM = quantity, ENTITY = quantity, POTION = duration"))
				.addArgument(Argument.of("[-c]", "Specifies to cancel any events related with this block"));
		
		Help helpAdd = new Help("mb add", "add", "Create a new Mystery Block event")
				.setPermission("mysteryblock.cmd.mb.add")
				.setUsage(usageAdd)
				.addExample("/mb add minecraft:IRON_ORE test 0.5 ITEM minecraft:golden_nugget -a 3")
				.addExample("/mb add minecraft:DIAMOND_ORE test 0.2 POTION minecraft:speed -a 60")
				.addExample("/mb add minecraft:IRON_ORE test 0.05 ENTITY minecraft:zombie -c");
		
		Usage usageRemove = new Usage(Argument.of("<block>", "Specifies to id of the BlockType"))
				.addArgument(Argument.of("[name]", "Specifies the new name of the event. If not specified all events will be removed under <block>"));
				
		Help helpRemove = new Help("mb remove", "remove", "Removes Mystery Block event")
				.setPermission("mysteryblock.cmd.mb.remove")
				.setUsage(usageRemove)
				.addExample("/mb remove minecraft:IRON_ORE")
				.addExample("/mb remove minecraft:IRON_ORE test");
		
		Help helpList = new Help("mb list", "list", "Lists all events")
				.setPermission("mysteryblock.cmd.mb.list");
		
		Help helpMb = new Help("mb", "mb", "Base command for Mystery Block")
				.setPermission("mysterblock.cmd.mb")
				.addChild(helpList)
				.addChild(helpRemove)
				.addChild(helpAdd);
		
		Help.register(helpMb);
	}
	
	public static void initConfig(String configName) {
		ConfigManager configManager = ConfigManager.init(Main.getPlugin(), configName);
		CommentedConfigurationNode config = configManager.getConfig();

		if(configName.equals(Main.getPlugin().getId())) {
			if (config.getNode("blocks").isVirtual()) {
				if (config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId()).isVirtual()) {
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example1", "percentage").setValue(Double.valueOf(0.2D)).setComment("0.0 to 1.0");
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example1", "type").setValue("ITEM").setComment("ENTITY, ITEM or POTION");
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example1", "data").setValue("minecraft:apple");
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example1", "amplifier").setValue(Integer.valueOf(1));
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example1", "cancel").setValue(Boolean.valueOf(false));
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example2", "percentage").setValue(Double.valueOf(0.2D)).setComment("0.0 to 1.0");
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example2", "type").setValue("ENTITY").setComment("ENTITY, ITEM or POTION");
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example2", "data").setValue("minecraft:zombie");
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example2", "amplifier").setValue(Integer.valueOf(1));
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example2", "cancel").setValue(Boolean.valueOf(false));
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example3", "percentage").setValue(Double.valueOf(0.2D)).setComment("0.0 to 1.0");
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example3", "type").setValue("POTION").setComment("ENTITY, ITEM or POTION");
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example3", "data").setValue("minecraft:speed");
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example3", "amplifier").setValue(Integer.valueOf(1));
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example3", "cancel").setValue(Boolean.valueOf(false));
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example4", "percentage").setValue(Double.valueOf(0.2D)).setComment("0.0 to 1.0");
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example4", "type").setValue("ITEM").setComment("ENTITY, ITEM or POTION");
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example4", "data").setValue("minecraft:pumpkin");
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example4", "amplifier").setValue(Integer.valueOf(1));
					config.getNode("blocks", BlockTypes.DIAMOND_ORE.getId(), "example4", "cancel").setValue(Boolean.valueOf(false));
				}
			}
		} else if(configName.equals("entities")) {
			List<String> list = new ArrayList<>();
			
			for (EntityType entityType : Sponge.getRegistry().getAllOf(EntityType.class)) {
				if ((!entityType.equals(EntityTypes.HUMAN)) && (!entityType.equals(EntityTypes.PLAYER)) && (!entityType.equals(EntityTypes.ITEM))) {
					list.add(entityType.getId());
				}
			}
			
			config.getNode("entities").setValue(list);
		} else if(configName.equals("items")) {
			List<String> list = new ArrayList<>();

			for (ItemType itemType : Sponge.getRegistry().getAllOf(ItemType.class)) {
				if (!itemType.equals(ItemTypes.NONE)) {
					list.add(itemType.getId());
				}
			}
			
			configManager.getConfig().getNode("items").setValue(list);
		} else if(configName.equals("blocks")) {
			List<String> list = new ArrayList<>();

			for (BlockType blockType : Sponge.getRegistry().getAllOf(BlockType.class)) {
				list.add(blockType.getId());
			}
			
			configManager.getConfig().getNode("blocks").setValue(list);
		} else if(configName.equals("potions")) {
			List<String> list = new ArrayList<>();

			for (PotionEffectType effectType : Sponge.getRegistry().getAllOf(PotionEffectType.class)) {
				list.add(effectType.getId());
			}

			configManager.getConfig().getNode("potions").setValue(list);
		}
		
		configManager.save();
	}
}
