package pt.com.hugodias.sample.stockexchange.entities.stock;

import pt.com.hugodias.sample.stockexchange.exceptions.StockException;

public abstract class Stock {
	private String code;
	private double lastDividend;
	private double parValue;
	
	private double lastPrice;
	

	@SuppressWarnings("unused")
	private Stock(){
		
	}
	
	protected Stock(String code, double lastDividend, double parValue){
		this.code = code;
		this.lastDividend = lastDividend;
		this.parValue = parValue;
		this.lastPrice = 0.0;
	}

	public String getCode() {
		return code;
	}
	
	public double getLastDividend() {
		return lastDividend;
	}

	public void setLastDividend(double lastDividend) {
		this.lastDividend = lastDividend;
	}

	public double getParValue() {
		return parValue;
	}

	public void setParValue(double parValue) {
		this.parValue = parValue;
	}

	public double getLastPrice() {
		return lastPrice;
	}

	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}
	
	public abstract double dividendYield() throws StockException;
	
	public double priceEarningsRatio() throws StockException{
		if (getLastDividend() == 0.0)
			throw new StockException(String.format("Stock %s has a ticker price of 0", this.getCode()));
		return getLastPrice() / getLastDividend();
	}
	
}
