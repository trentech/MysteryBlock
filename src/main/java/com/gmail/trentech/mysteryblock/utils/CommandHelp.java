package com.gmail.trentech.mysteryblock.utils;

import com.gmail.trentech.helpme.help.Argument;
import com.gmail.trentech.helpme.help.Help;
import com.gmail.trentech.helpme.help.Usage;

public class CommandHelp {

	public static void init() {
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
}
