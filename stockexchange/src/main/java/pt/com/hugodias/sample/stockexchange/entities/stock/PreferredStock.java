package pt.com.hugodias.sample.stockexchange.entities.stock;

import pt.com.hugodias.sample.stockexchange.exceptions.StockException;

public class PreferredStock extends Stock{

	private double fixedDividend;
	
	public PreferredStock(String code, double lastDividend, double parValue) {
		super(code, lastDividend, parValue);
		this.fixedDividend = 0.0;
	}
	
	public PreferredStock(String code, double lastDividend, double parValue, double fixedDividend) {
		super(code, lastDividend, parValue);
		this.fixedDividend = fixedDividend;
	}

	public double getFixedDividend() {
		return fixedDividend;
	}

	public void setFixedDividend(double fixedDividend) {
		this.fixedDividend = fixedDividend;
	}
	
	public double dividendYield() throws StockException{
		if (getLastPrice() == 0.0)
			throw new StockException(String.format("Stock %s has a ticker price of 0", this.getCode()));
		return (getFixedDividend() / 100) * getParValue() / getLastPrice();
	}


}
