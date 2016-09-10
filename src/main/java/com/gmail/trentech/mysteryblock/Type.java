package com.gmail.trentech.mysteryblock;

public enum Type {
	
	ITEM("POTION"), ENTITY("POTION"), POTION("POTION");
	
	private final String name;
	
	private Type(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
