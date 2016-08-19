package com.gmail.trentech.mysteryblock.commands;

import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandManager {
	
	private CommandSpec cmdAdd = CommandSpec.builder()
			.permission("mysteryblock.cmd.mb.add")
			.arguments(GenericArguments.optional(GenericArguments.string(Text.of("block"))), GenericArguments.optional(GenericArguments.string(Text.of("name"))), 
					GenericArguments.optional(GenericArguments.string(Text.of("percentage"))), GenericArguments.optional(GenericArguments.string(Text.of("type"))), 
					GenericArguments.optional(GenericArguments.string(Text.of("data"))), 
					GenericArguments.flags().valueFlag(GenericArguments.string(Text.of("amplifier")), "a").flag("c").buildWith(GenericArguments.none()))
			.executor(new CMDAdd())
			.build();
	
	private CommandSpec cmdRemove = CommandSpec.builder()
			.permission("mysteryblock.cmd.mb.remove")
			.arguments(GenericArguments.optional(GenericArguments.string(Text.of("block"))), GenericArguments.optional(GenericArguments.string(Text.of("name"))))
			.executor(new CMDRemove())
			.build();
	
	private CommandSpec cmdList = CommandSpec.builder()
			.permission("mysteryblock.cmd.mb.list")
			.executor(new CMDList())
			.build();
	
	public CommandSpec cmdMysteryBlock = CommandSpec.builder()
			.permission("mysteryblock.cmd.mb")
			.child(cmdAdd, "add", "a")
			.child(cmdRemove, "remove", "r" )
			.child(cmdList, "list", "l" )
			.executor(new CMDMysteryBlock())
			.build();
	
}