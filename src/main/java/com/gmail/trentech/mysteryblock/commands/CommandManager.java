package com.gmail.trentech.mysteryblock.commands;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import com.gmail.trentech.mysteryblock.Type;
import com.gmail.trentech.mysteryblock.utils.Help;

public class CommandManager {
	
	private CommandSpec cmdAdd = CommandSpec.builder()
			.permission("mysteryblock.cmd.mb.add")
			.arguments(GenericArguments.catalogedElement(Text.of("block"), BlockType.class), GenericArguments.string(Text.of("name")), 
					GenericArguments.doubleNum(Text.of("percentage")), GenericArguments.enumValue(Text.of("type"), Type.class), 
					GenericArguments.string(Text.of("data")), 
					GenericArguments.flags().valueFlag(GenericArguments.string(Text.of("amplifier")), "a").flag("c").buildWith(GenericArguments.none()))
			.executor(new CMDAdd())
			.build();
	
	private CommandSpec cmdRemove = CommandSpec.builder()
			.permission("mysteryblock.cmd.mb.remove")
			.arguments(GenericArguments.catalogedElement(Text.of("block"), BlockType.class), GenericArguments.optional(GenericArguments.string(Text.of("name"))))
			.executor(new CMDRemove())
			.build();
	
	private CommandSpec cmdList = CommandSpec.builder()
			.permission("mysteryblock.cmd.mb.list")
			.executor(new CMDList())
			.build();
	
	public CommandSpec cmdHelp = CommandSpec.builder()
		    .description(Text.of(" I need help with Mystery Block"))
		    .permission("mysteryblock.cmd.mb")
		    .arguments(GenericArguments.choices(Text.of("command"), Help.all()))
		    .executor(new CMDHelp())
		    .build();
	
	public CommandSpec cmdMysteryBlock = CommandSpec.builder()
			.permission("mysteryblock.cmd.mb")
			.child(cmdAdd, "add", "a")
			.child(cmdRemove, "remove", "r" )
			.child(cmdList, "list", "l" )
			.child(cmdHelp, "help", "h" )
			.executor(new CMDMysteryBlock())
			.build();
	
}