
package pt.com.hugodias.sample.stockexchange.services;

import java.util.Collection;

import pt.com.hugodias.sample.stockexchange.entities.stock.Stock;
import pt.com.hugodias.sample.stockexchange.exceptions.DuplicateStockException;
import pt.com.hugodias.sample.stockexchange.exceptions.IllegalStockException;
import pt.com.hugodias.sample.stockexchange.exceptions.StockException;

public interface StockService {
	public Collection<Stock> getAllStock();
	
	public void addStock(Stock stock) throws DuplicateStockException;
	
	public Stock getStock(String code);

	public double getDividendYeld(Stock stock) throws IllegalStockException, StockException;
	
	public double getProfitEarningsRatio(Stock stock) throws IllegalStockException, StockException;

	double getDividendYeld(String code) throws IllegalStockException, StockException;

	double getProfitEarningsRatio(String code) throws IllegalStockException, StockException;
}
