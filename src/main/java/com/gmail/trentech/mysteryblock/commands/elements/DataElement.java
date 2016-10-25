package com.gmail.trentech.mysteryblock.commands.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.mysteryblock.utils.Type;

public class DataElement extends CommandElement {

	CommandContext context = new CommandContext();
	
    public DataElement(Text key) {
        super(key);
    }

    @Override
    public void parse(CommandSource source, CommandArgs args, CommandContext context) throws ArgumentParseException {
    	this.context = context;
    	
        Object val = parseValue(source, args);
        String key = getUntranslatedKey();
        if (key != null && val != null) {
            if (val instanceof Iterable<?>) {
                for (Object ent : ((Iterable<?>) val)) {
                    context.putArg(key, ent);
                }
            } else {
                context.putArg(key, val);
            }
        }
    }
    
    @Override
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
    	final String next = args.next();

    	if(context.hasAny(getKey())) {
    		Type type = context.<Type>getOne(getKey()).get();
    		
    		if(type.equals(Type.ENTITY)) {
        		for(EntityType entityType : Sponge.getRegistry().getAllOf(EntityType.class)) {
        			if(entityType.getId().equals(next)) {
        				return next;
        			}
        		}
        		
        		throw args.createError(Text.of(TextColors.RED, "EntityType not found")); 
    		} else if(type.equals(Type.ITEM)) {
        		for(ItemType itemType : Sponge.getRegistry().getAllOf(ItemType.class)) {
        			if(itemType.getId().equals(next)) {
        				return next;
        			}
        		}
        		
        		throw args.createError(Text.of(TextColors.RED, "ItemType not found")); 
    		} else if(type.equals(Type.POTION)) {
        		for(PotionEffectType effectType : Sponge.getRegistry().getAllOf(PotionEffectType.class)) {
        			if(effectType.getId().equals(next)) {
        				return next;
        			}
        		}
        		
        		throw args.createError(Text.of(TextColors.RED, "PotionEffectType not found")); 
    		}
    	}

		throw args.createError(Text.of(TextColors.RED, "Type was not provided."));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {  	
    	List<String> list = new ArrayList<>();

    	Optional<String> next = args.nextIfPresent();
    	
    	if(next.isPresent() && !next.get().equals("")) {
        	if(context.hasAny(getKey())) {
        		Type type = context.<Type>getOne(getKey()).get();
        		
        		if(type.equals(Type.ENTITY)) {
            		for(EntityType entityType : Sponge.getRegistry().getAllOf(EntityType.class)) {
            			if(entityType.getId().startsWith(next.get())) {
            				list.add(entityType.getId());
            			}
            		}
        		} else if(type.equals(Type.ITEM)) {
            		for(ItemType itemType : Sponge.getRegistry().getAllOf(ItemType.class)) {
            			if(itemType.getId().startsWith(next.get())) {
            				list.add(itemType.getId());
            			}
            		}
        		} else if(type.equals(Type.POTION)) {
            		for(PotionEffectType effectType : Sponge.getRegistry().getAllOf(PotionEffectType.class)) {
            			if(effectType.getId().startsWith(next.get())) {
            				list.add(effectType.getId());
            			}
            		}
        		}
        	}
    	} else {
        	if(context.hasAny(getKey())) {
        		Type type = context.<Type>getOne(getKey()).get();
        		
        		if(type.equals(Type.ENTITY)) {
            		for(EntityType entityType : Sponge.getRegistry().getAllOf(EntityType.class)) {
            			list.add(entityType.getId());
            		}
        		} else if(type.equals(Type.ITEM)) {
            		for(ItemType itemType : Sponge.getRegistry().getAllOf(ItemType.class)) {
            			list.add(itemType.getId());
            		}
        		} else if(type.equals(Type.POTION)) {
            		for(PotionEffectType effectType : Sponge.getRegistry().getAllOf(PotionEffectType.class)) {
            			list.add(effectType.getId());
            		}
        		}
        	}
    	}
		
		return list;
    }

    @Override
    public Text getUsage(CommandSource src) {
        return Text.of(getKey());
    }
}
