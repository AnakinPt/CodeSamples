package pt.com.hugodias.sample.stockexchange.services;

import pt.com.hugodias.sample.stockexchange.entities.orders.Order;
import pt.com.hugodias.sample.stockexchange.entities.stock.Stock;
import pt.com.hugodias.sample.stockexchange.exceptions.IllegalOrderException;
import pt.com.hugodias.sample.stockexchange.exceptions.IllegalStockException;

public interface OrdersService {
	public void addOrder(Order order) throws IllegalOrderException;
	
	public double getGeometricMean(Stock stock) throws IllegalStockException;
	
	public double getStockPrice(Stock stock) throws IllegalStockException;
	
	public double getAllShareIndex();
}
