package pt.com.hugodias.sample.entities;

import java.io.Serializable;

public class Details implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String description;
	private int quantity;
	private double value;
	
	
	public Details(long id, String description, int quantity, double value) {
		super();
		this.id = id;
		this.description = description;
		this.quantity = quantity;
		this.value = value;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public double getValue() {
		return value;
	}


	public void setValue(double value) {
		this.value = value;
	}
	
	
}
