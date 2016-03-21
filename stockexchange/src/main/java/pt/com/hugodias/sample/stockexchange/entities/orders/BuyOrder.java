package pt.com.hugodias.sample.stockexchange.entities.orders;

import pt.com.hugodias.sample.stockexchange.entities.stock.Stock;
import pt.com.hugodias.sample.stockexchange.exceptions.IllegalOrderException;

public class BuyOrder extends Order {

	public BuyOrder(Stock stock, int shares, double price) throws IllegalOrderException {
		super(stock, shares, price);
	}

}
