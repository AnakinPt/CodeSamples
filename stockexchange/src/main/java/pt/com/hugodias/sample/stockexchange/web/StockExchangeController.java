package pt.com.hugodias.sample.stockexchange.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.com.hugodias.sample.stockexchange.config.StockExchangeConfig;
import pt.com.hugodias.sample.stockexchange.entities.orders.BuyOrder;
import pt.com.hugodias.sample.stockexchange.entities.orders.Order;
import pt.com.hugodias.sample.stockexchange.entities.orders.SellOrder;
import pt.com.hugodias.sample.stockexchange.entities.stock.CommonStock;
import pt.com.hugodias.sample.stockexchange.entities.stock.PreferredStock;
import pt.com.hugodias.sample.stockexchange.entities.stock.Stock;
import pt.com.hugodias.sample.stockexchange.exceptions.DuplicateStockException;
import pt.com.hugodias.sample.stockexchange.exceptions.IllegalOrderException;
import pt.com.hugodias.sample.stockexchange.exceptions.IllegalStockException;
import pt.com.hugodias.sample.stockexchange.exceptions.StockException;
import pt.com.hugodias.sample.stockexchange.services.OrdersService;
import pt.com.hugodias.sample.stockexchange.services.StockService;

@Controller
@EnableAutoConfiguration
@Import(StockExchangeConfig.class)
public class StockExchangeController {

	private static boolean init = false;

	@Autowired
	private StockService stockService;

	@Autowired
	private OrdersService orderService;

	private void init() {
		try {
			Stock stock = new CommonStock("TEA", 0.0, 100);
			stockService.addStock(stock);
			stock = new CommonStock("POP", 8.0, 100);
			stockService.addStock(stock);
			stock = new CommonStock("ALE", 23, 60);
			stockService.addStock(stock);
			stock = new CommonStock("JOE", 13.0, 250);
			stockService.addStock(stock);
			stock = new PreferredStock("GIN", 8, 100, 2);
			stockService.addStock(stock);
		} catch (Exception e) {
			// eat all exceptions
		}
	}

	@RequestMapping("/")
	@ResponseBody
	Collection<Stock> getStocks() {
		if (!init) {
			init();
		}
		return stockService.getAllStock();
	}

	@RequestMapping("/stocks/getStock")
	@ResponseBody
	Stock getStock(String code) {
		if (!init) {
			init();
		}
		return stockService.getStock(code);
	}

	@RequestMapping("/stocks/getDividendYeld")
	@ResponseBody
	double getDividendYeld(String code) throws IllegalStockException, StockException {
		if (!init) {
			init();
		}
		
		return stockService.getDividendYeld(code);
	}

	@RequestMapping("/stocks/getProfitEarningsRatio")
	@ResponseBody
	double getProfitEarningsRatio(String code) throws IllegalStockException, StockException {
		if (!init) {
			init();
		}
		
		return stockService.getProfitEarningsRatio(code);
	}

	@RequestMapping(path="/stocks/addStock", method=RequestMethod.POST)
	@ResponseBody
	String addStock(String code, double lastDividend, double parValue ) {
		if (!init) {
			init();
		}
		Stock stock = new CommonStock(code, lastDividend, parValue);
		try {
			stockService.addStock(stock);
		} catch (DuplicateStockException e) {
			return String.format("Stock %s not added to the system due to error: %s", code, e.getMessage());
		}
		return String.format("Stock %s added to the system", code);
	}
	
	@RequestMapping(path="/order/addOrder", method=RequestMethod.POST)
	@ResponseBody
	String addOrder(String type, String code, double price, int quantity) {
		if (!init) {
			init();
		}
		Stock stock = stockService.getStock(code);
		if (stock == null)
			return String.format("Stock %s not found in the system", code);
		Order order = null; 
			
		try {
			if ("B".equals(type)){
				order = new BuyOrder(stock, quantity, price);
			}else if ("S".equals(type)){
				order = new SellOrder(stock, quantity, price);
			}
			orderService.addOrder(order);
		} catch (IllegalOrderException e) {
			return String.format("Error creating order: %s", e.getMessage());
		}
		return String.format("Order registered");
	}
	
	@RequestMapping("/order/getGeometricMean")
	@ResponseBody
	double getGeometricMean(String code) throws IllegalStockException, StockException {
		if (!init) {
			init();
		}
		Stock stock = stockService.getStock(code);
		return orderService.getGeometricMean(stock);
	}

	@RequestMapping("/order/getStockPrice")
	@ResponseBody
	double getStockPrice(String code) throws IllegalStockException, StockException {
		if (!init) {
			init();
		}
		Stock stock = stockService.getStock(code);
		return orderService.getStockPrice(stock);
	}

	@RequestMapping("/order/getAllShareIndex")
	@ResponseBody
	double getAllShareIndex() throws IllegalStockException, StockException {
		if (!init) {
			init();
		}
		return orderService.getAllShareIndex();
	}

	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(StockExchangeController.class, args);
	}
}
