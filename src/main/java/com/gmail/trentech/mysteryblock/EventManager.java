package com.gmail.trentech.mysteryblock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.command.TabCompleteEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

import com.gmail.trentech.mysteryblock.utils.ConfigManager;

import ninja.leaping.configurate.ConfigurationNode;

public class EventManager {
	
	private static ThreadLocalRandom random = ThreadLocalRandom.current();
	private static List<UUID> players = new ArrayList<UUID>();

	@Listener
	public void onTabCompleteEvent(TabCompleteEvent event, @First Player player) {
		String rawMessage = event.getRawMessage();
		
		String[] args = rawMessage.split(" ");
		
		if(!args[0].equalsIgnoreCase("mb") && !args[0].equalsIgnoreCase("mysteryblock") || args.length == 1) {
			return;
		}
		
		List<String> list = event.getTabCompletions();

		if(args.length == 2 || args.length == 3) {
			if(args[1].equalsIgnoreCase("add")) {
				for(BlockType blockType : Sponge.getRegistry().getAllOf(BlockType.class)) {
					String id = blockType.getId();
					
					if(args.length == 3) {
						if(id.contains(args[2].toLowerCase()) && !id.equalsIgnoreCase(args[2])) {
							list.add(id);
						}
					} else if(rawMessage.substring(rawMessage.length() - 1).equalsIgnoreCase(" ")){
						list.add(id);
					}
				}
			}
		} else if(args.length == 5 && rawMessage.substring(rawMessage.length() - 1).equalsIgnoreCase(" ")) {
			list.add("ENTITY");
			list.add("ITEM");
			list.add("POTION");
		}else if(args.length == 6 || args.length == 7) {
			if (args[5].equals("ITEM")) {
				for(ItemType itemType : Sponge.getRegistry().getAllOf(ItemType.class)) {
					if (itemType.equals(ItemTypes.NONE)) {
						continue;
					}
					
					String id = itemType.getId();
					
					if(args.length == 7) {
						if(id.contains(args[6].toLowerCase()) && !id.equalsIgnoreCase(args[6])) {
							list.add(id);
						}
					} else if(rawMessage.substring(rawMessage.length() - 1).equalsIgnoreCase(" ")){
						list.add(id);
					}
				}
			} else if (args[5].equals("ENTITY")) {
				for(EntityType entityType : Sponge.getRegistry().getAllOf(EntityType.class)) {					
					if (entityType.equals(EntityTypes.HUMAN) || entityType.equals(EntityTypes.PLAYER) || entityType.equals(EntityTypes.ITEM)) {
						continue;
					}
					
					String id = entityType.getId();
					
					if(args.length == 7) {
						if(id.contains(args[6].toLowerCase()) && !id.equalsIgnoreCase(args[6])) {
							list.add(id);
						}
					} else if(rawMessage.substring(rawMessage.length() - 1).equalsIgnoreCase(" ")){
						list.add(id);
					}
				}
			} else if (args[5].equals("POTION")) {
				for(PotionEffectType potionType : Sponge.getRegistry().getAllOf(PotionEffectType.class)) {
					String id = potionType.getId();
					
					if(args.length == 7) {
						if(id.contains(args[6].toLowerCase()) && !id.equalsIgnoreCase(args[6])) {
							list.add(id);
						}
					} else if(rawMessage.substring(rawMessage.length() - 1).equalsIgnoreCase(" ")){
						list.add(id);
					}
				}
			}
		}
	}
	
	@Listener
	public void onChangeBlockEvent(ChangeBlockEvent.Break event, @First Player player) {
		if (players.contains(player.getUniqueId())) {
			return;
		}
		
		if (player.gameMode().get().equals(GameModes.CREATIVE)) {
			return;
		}
		
		for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
			if (players.contains(player.getUniqueId())) {
				continue;
			}
			BlockSnapshot snapshot = (BlockSnapshot) transaction.getOriginal();
			
			if (snapshot.getCreator().isPresent()) {
				continue;
			}
			BlockState blockState = snapshot.getState();
			
			ConfigurationNode node = ConfigManager.get().getConfig().getNode("blocks", blockState.getType().getId());
			
			if (node.isVirtual()) {
				continue;
			}
			
			players.add(player.getUniqueId());
			
			Map<Object, ? extends ConfigurationNode> children = node.getChildrenMap();
			
			for (final Map.Entry<Object, ? extends ConfigurationNode> child : children.entrySet()) {
				float percentage = ((ConfigurationNode) child.getValue()).getNode(new Object[] { "percentage" }).getFloat();
				if (random.nextDouble() > percentage) {
					continue;
				}
				
				String type = child.getValue().getNode("type").getString().toUpperCase();
				String data = child.getValue().getNode("data").getString();
				int amplifier = child.getValue().getNode("amplifier").getInt();
				boolean cancel = child.getValue().getNode("cancel").getBoolean();
				
				if (type.equals("ITEM")) {
					Trigger.spawnItem(snapshot.getLocation().get(), data, amplifier);
				} else if (type.equals("ENTITY")) {
					Trigger.spawnEntity(snapshot.getLocation().get(), data, amplifier);
				} else if (type.equals("POTION")) {
					Trigger.potionEffect(player, data, amplifier);
				} else {
					Main.instance().getLog().warn("Effect Type " + type + " not found");
				}
				
				if (!cancel) {
					continue;
				}
				
				Sponge.getScheduler().createTaskBuilder().delayTicks(5).execute(e -> snapshot.getLocation().get().setBlock(blockState, Cause.of(NamedCause.source(Main.getPlugin())))).submit(Main.getPlugin());
			}
			
			Sponge.getScheduler().createTaskBuilder().delayTicks(20).execute(e -> players.remove(player.getUniqueId())).submit(Main.getPlugin());
		}
	}
}
