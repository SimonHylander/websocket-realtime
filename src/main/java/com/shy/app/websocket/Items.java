package com.shy.app.websocket;

public class Items {
	int id;
	String name;
	String description;
	
	public Items() {}

	public Items(String name, String description)  {
		this.name = name;
		this.description = description;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
