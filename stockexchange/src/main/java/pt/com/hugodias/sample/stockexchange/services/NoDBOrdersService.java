package pt.com.hugodias.sample.stockexchange.services;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import pt.com.hugodias.sample.stockexchange.entities.orders.Order;
import pt.com.hugodias.sample.stockexchange.entities.stock.Stock;
import pt.com.hugodias.sample.stockexchange.exceptions.IllegalOrderException;
import pt.com.hugodias.sample.stockexchange.exceptions.IllegalStockException;

public class NoDBOrdersService implements OrdersService {

	private Map<String, List<Order>> orders = new TreeMap<String, List<Order>>();

	@Autowired
	private StockService stockService;

	@Resource
	private int minutesForStockPrice;

	@Override
	public void addOrder(Order order) throws IllegalOrderException {
		Stock stockOrder = order.getStock();
		if (stockOrder == null)
			throw new IllegalOrderException("Cannot get Stock");
		String stockCode = stockOrder.getCode();
		Stock stock = stockService.getStock(stockCode);
		if (stock == null) {
			throw new IllegalOrderException(String.format("Stock %s doesn't exists", stockCode));
		}

		List<Order> orderList = orders.get(stockCode);
		if (orderList == null) {
			orderList = new LinkedList<Order>();
			orders.put(stockCode, orderList);
		}
		orderList.add(0, order);
		stock.setLastPrice(order.getPrice());

	}

	@Override
	public double getGeometricMean(Stock stock) throws IllegalStockException {
		List<Order> orderList = getStock(stock);
		if (orderList == null || orderList.size() == 0)
			return 0.0;
		double product = 1.0;
		for (Iterator<Order> iterator = orderList.iterator(); iterator.hasNext();) {
			Order order = iterator.next();
			product *= order.getPrice();
		}

		return Math.pow(product, 1.0 / orderList.size());
	}

	private List<Order> getStock(Stock stock) throws IllegalStockException {
		Stock dbstock = stockService.getStock(stock.getCode());
		if (dbstock == null) {
			throw new IllegalStockException(String.format("Stock %s doesn't exists", stock.getCode()));
		}
		List<Order> orderList = orders.get(stock.getCode());
		return orderList;
	}

	@Override
	public double getStockPrice(Stock stock) throws IllegalStockException {
		List<Order> orderList = getStock(stock);
		if (orderList == null || orderList.size() == 0)
			return 0.0;

		long quantity = 0;
		double ammount = 0.0;

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -minutesForStockPrice);

		for (Iterator<Order> iterator = orderList.iterator(); iterator.hasNext();) {
			Order order = iterator.next();

			if (order.getOperationTime().after(cal)) {
				quantity += order.getShares();
				ammount += order.getShares() * order.getPrice();
			}
		}
		return ammount / quantity;
	}

	@Override
	public double getAllShareIndex() {
		double product = 1.0;
		int elements = 0;
		for (Iterator<List<Order>> iterator = orders.values().iterator(); iterator.hasNext();) {
			List<Order> ordersList = iterator.next();
			for (Iterator<Order> iteratorList = ordersList.iterator(); iteratorList.hasNext();) {
				Order order = iteratorList.next();
				product *= order.getPrice();
				elements++;
			}
		}
		return Math.pow(product, 1.0 / elements);
	}

	void reset() {
		orders = new TreeMap<String, List<Order>>();
	}

}
