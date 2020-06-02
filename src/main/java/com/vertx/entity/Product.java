package com.vertx.entity;

public class Product {

	private String number;
	private int id;
	private String description;
	
	public Product(int id,String number, String description) {
		this.id = id;
		this.number = number;
		this.description = description;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}



	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}

	
