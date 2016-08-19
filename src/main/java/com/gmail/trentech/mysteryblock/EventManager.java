package com.gmail.trentech.mysteryblock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.filter.cause.First;

import com.gmail.trentech.mysteryblock.utils.ConfigManager;

import ninja.leaping.configurate.ConfigurationNode;

public class EventManager {
	
	private static ThreadLocalRandom random = ThreadLocalRandom.current();
	private static List<UUID> players = new ArrayList<UUID>();

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
					Main.getLog().warn("Effect Type " + type + " not found");
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
