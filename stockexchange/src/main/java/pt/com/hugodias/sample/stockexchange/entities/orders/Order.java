package pt.com.hugodias.sample.stockexchange.entities.orders;

import java.util.Calendar;

import pt.com.hugodias.sample.stockexchange.entities.stock.Stock;
import pt.com.hugodias.sample.stockexchange.exceptions.IllegalOrderException;

public abstract class Order {
	private Stock stock;
	private Calendar operationTime;
	private int shares;
	private double price;
	
	public Order(Stock stock, int shares, double price) throws IllegalOrderException {
		super();
		if (shares <= 0){
			throw new IllegalOrderException("Ammount of shares must be greater than 0");
		}
		if (price <= 0.0){
			throw new IllegalOrderException("Price must be greater than 0");
		}
		this.stock = stock;
		this.shares = shares;
		this.price = price;
		this.operationTime = Calendar.getInstance();
	}
	
	public Stock getStock() {
		return stock;
	}
	public Calendar getOperationTime() {
		return operationTime;
	}
	
	public int getShares() {
		return shares;
	}
	public double getPrice() {
		return price;
	}

	public void setOperationTime(Calendar operationTime) {
		this.operationTime = operationTime;
	}
	
	
}
