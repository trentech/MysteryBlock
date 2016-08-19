package com.gmail.trentech.mysteryblock;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class Trigger {
	
	public static void spawnItem(Location<World> location, String itemId, int quantity) {
		Optional<ItemType> optionalItemType = Sponge.getRegistry().getType(ItemType.class, itemId);

		if (!optionalItemType.isPresent()) {
			Main.getLog().warn("ItemType " + itemId + " not found");
			return;
		}
		ItemStack itemStack = ItemStack.of(optionalItemType.get(), quantity);

		Item entity = (Item) location.getExtent().createEntity(EntityTypes.ITEM, location.getPosition());
		entity.offer(Keys.REPRESENTED_ITEM, itemStack.createSnapshot());

		location.getExtent().spawnEntity(entity, Cause.of(NamedCause.source(EntitySpawnCause.builder().entity(entity).type(SpawnTypes.PLUGIN).build())));
	}

	public static void spawnEntity(Location<World> location, String entityId, int quantity) {
		Optional<EntityType> optionalEntityType = Sponge.getRegistry().getType(EntityType.class, entityId);

		if (!optionalEntityType.isPresent()) {
			Main.getLog().warn(EntityTypes.PRIMED_TNT.getId());
			Main.getLog().warn("EntityType " + entityId + " not found");
			return;
		}
		EntityType entityType = (EntityType) optionalEntityType.get();

		if ((entityType.equals(EntityTypes.HUMAN)) || (entityType.equals(EntityTypes.PLAYER)) || (entityType.equals(EntityTypes.ITEM))) {
			Main.getLog().warn("EntityType " + entityId + " not found");
			return;
		}

		for (int i = 0; i < quantity; i++) {
			Entity entity = location.getExtent().createEntity(entityType, location.getPosition());

			location.getExtent().spawnEntity(entity, Cause.of(NamedCause.source(EntitySpawnCause.builder().entity(entity).type(SpawnTypes.PLUGIN).build())));
		}
	}

	public static void potionEffect(Player player, String potionEffectId, int duration) {
		Optional<PotionEffectType> optionalItemType = Sponge.getRegistry().getType(PotionEffectType.class, potionEffectId);

		if (!optionalItemType.isPresent()) {
			Main.getLog().warn("PotionEffectType " + potionEffectId + " not found");
			return;
		}
		PotionEffect potionEffect = PotionEffect.of(optionalItemType.get(), 1, duration * 20);

		PotionEffectData effects = player.getOrCreate(PotionEffectData.class).get();
		effects.addElement(potionEffect);

		player.offer(effects).isSuccessful();
	}
}