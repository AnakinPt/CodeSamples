package pt.com.hugodias.sample.stockexchange.entities.stock;

import pt.com.hugodias.sample.stockexchange.exceptions.StockException;

public class CommonStock extends Stock {

	public CommonStock(String code, double lastDividend, double parValue) {
		super(code, lastDividend, parValue);
	}

	public double dividendYield() throws StockException{
		if (getLastPrice() == 0.0)
			throw new StockException(String.format("Stock %s has a ticker price of 0", this.getCode()));
		return getLastDividend() / getLastPrice();
	}
}
