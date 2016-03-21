package pt.com.hugodias.sample.stockexchange.services;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pt.com.hugodias.sample.stockexchange.entities.stock.Stock;
import pt.com.hugodias.sample.stockexchange.exceptions.DuplicateStockException;
import pt.com.hugodias.sample.stockexchange.exceptions.IllegalStockException;
import pt.com.hugodias.sample.stockexchange.exceptions.StockException;

public class NoDBStockService implements StockService {

	private Map<String, Stock> stocks = new TreeMap<String, Stock>();
	
	void reset(){
		stocks = new TreeMap<String, Stock>();
	}
	
	@Override
	public void addStock(Stock stock) throws DuplicateStockException {
		if(stocks.get(stock.getCode()) != null){
			throw new DuplicateStockException(String.format("Stock %s already exists", stock.getCode()));
		}
		stocks.put(stock.getCode(), stock);
	}

	@Override
	public Stock getStock(String code) {
		return stocks.get(code);
	}

	@Override
	public double getDividendYeld(Stock stock) throws IllegalStockException, StockException {
		Stock dbStock = stocks.get(stock.getCode());
		if(dbStock == null){
			throw new IllegalStockException(String.format("Stock %s don't exists", stock.getCode()));
		}
		return dbStock.dividendYield();
	}

	@Override
	public double getDividendYeld(String code) throws IllegalStockException, StockException {
		Stock dbStock = stocks.get(code);
		if(dbStock == null){
			throw new IllegalStockException(String.format("Stock %s don't exists", code));
		}
		return dbStock.dividendYield();
	}


	@Override
	public double getProfitEarningsRatio(Stock stock) throws IllegalStockException, StockException {
		Stock dbStock = stocks.get(stock.getCode());
		if(dbStock == null){
			throw new IllegalStockException(String.format("Stock %s don't exists", stock.getCode()));
		}
		return dbStock.priceEarningsRatio();
	}

	@Override
	public double getProfitEarningsRatio(String code) throws IllegalStockException, StockException {
		Stock dbStock = stocks.get(code);
		if(dbStock == null){
			throw new IllegalStockException(String.format("Stock %s don't exists", code));
		}
		return dbStock.priceEarningsRatio();
	}

	@Override
	public Collection<Stock> getAllStock() {
		return stocks.values();
	}

}
