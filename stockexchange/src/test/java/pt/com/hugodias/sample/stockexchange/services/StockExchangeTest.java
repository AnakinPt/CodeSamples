package pt.com.hugodias.sample.stockexchange.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
import pt.com.hugodias.sample.stockexchange.services.NoDBOrdersService;
import pt.com.hugodias.sample.stockexchange.services.NoDBStockService;
import pt.com.hugodias.sample.stockexchange.services.OrdersService;
import pt.com.hugodias.sample.stockexchange.services.StockService;

@ContextConfiguration(classes={StockExchangeConfig.class})
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class StockExchangeTest {

	@Autowired
	private StockService stockService;
	
	@Autowired
	private OrdersService orderService;
	
	@Before
	public void setUp() throws DuplicateStockException{
		((NoDBStockService) stockService).reset();
		((NoDBOrdersService) orderService).reset();
		
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
	}
	
	@Test
	public void testAddStock() throws DuplicateStockException{
		Stock stock = new CommonStock("COF", 0.0, 50);
		stockService.addStock(stock);
		assertNotNull(stockService.getStock("COF"));
		stock = new PreferredStock("SAX", 0.0, 100, 3);
		stockService.addStock(stock);
		assertNotNull(stockService.getStock("SAX"));
		stock = new PreferredStock("SLB", 0.0, 100);
		((PreferredStock)stock).setFixedDividend(2.5);
		stockService.addStock(stock);
		assertNotNull(stockService.getStock("SAX"));
		stock = new CommonStock("MAN", 0.0, 0);
		stock.setLastDividend(2);
		stock.setParValue(200);
		stockService.addStock(stock);
	}
	
	@Test(expected=DuplicateStockException.class)
	public void testAddStockDuplicate1() throws DuplicateStockException{
		Stock stock = new CommonStock("GIN", 0.0, 50);
		stockService.addStock(stock);
	}
	
	@Test(expected=DuplicateStockException.class)
	public void testAddStockDuplicate2() throws DuplicateStockException{
		Stock stock = new PreferredStock("TEA", 0.0, 100, 2);
		stockService.addStock(stock);
	}

	@Test
	public void testGetStock(){
		Stock stock = stockService.getStock("TEA");
		assertTrue(stock instanceof CommonStock);
		assertEquals(0.0, stock.getLastDividend(), 0.005);
		assertEquals(100.0, stock.getParValue(),0.005);
		stock = stockService.getStock("GIN");
		assertTrue(stock instanceof PreferredStock);
		assertEquals(8.0, stock.getLastDividend(), 0.005);
		assertEquals(100.0, stock.getParValue(),0.005);
		assertEquals(2.0, ((PreferredStock)stock).getFixedDividend(), 0.005);
		stock = stockService.getStock("SAX");
		assertNull(stock);
	}

	@Test
	public void testGetDividendYeld() throws IllegalStockException, IllegalOrderException, StockException{
		Stock stock = stockService.getStock("TEA");
		Order order = new BuyOrder(stock, 100, 1);
		orderService.addOrder(order);
		assertEquals(0.0, stockService.getDividendYeld(stock), 0.005);
		assertEquals(0.0, stockService.getDividendYeld("TEA"), 0.005);
		stock = stockService.getStock("POP");
		order = new BuyOrder(stock, 100, 2);
		orderService.addOrder(order);
		assertEquals(4.0, stockService.getDividendYeld(stock), 0.005);
		assertEquals(4.0, stockService.getDividendYeld("POP"), 0.005);
		stock = stockService.getStock("GIN");
		order = new BuyOrder(stock, 100, 5);
		orderService.addOrder(order);
		assertEquals(0.4, stockService.getDividendYeld(stock), 0.005);
		assertEquals(0.4, stockService.getDividendYeld("GIN"), 0.005);
	}

	@Test(expected=StockException.class)
	public void testGetDividendYeldStockException1() throws IllegalStockException, IllegalOrderException, StockException{
		Stock stock = stockService.getStock("TEA");
		stockService.getDividendYeld(stock);
	}

	@Test(expected=StockException.class)
	public void testGetDividendYeldStockException2() throws IllegalStockException, IllegalOrderException, StockException{
		Stock stock = stockService.getStock("GIN");
		stockService.getDividendYeld(stock);
	}

	@Test(expected=IllegalStockException.class)
	public void testGetDividendYeldIllegalStockException() throws IllegalStockException, IllegalOrderException, StockException{
		Stock stock = new PreferredStock("SAX", 0.0, 100, 3);;
		stockService.getDividendYeld(stock);
	}

	@Test(expected=IllegalStockException.class)
	public void testGetDividendYeldIllegalStockException2() throws IllegalStockException, IllegalOrderException, StockException{
		stockService.getDividendYeld("SAX");
	}
	
	@Test
	public void testProfitEarningsRatio() throws IllegalStockException, IllegalOrderException, StockException{
		Stock stock = stockService.getStock("ALE");
		assertEquals(0.0, stockService.getProfitEarningsRatio(stock), 0.005);
		stock = stockService.getStock("POP");
		Order order = new BuyOrder(stock, 100, 2);
		orderService.addOrder(order);
		assertEquals(0.25, stockService.getProfitEarningsRatio(stock), 0.005);
		stock = stockService.getStock("GIN");
		order = new BuyOrder(stock, 100, 5);
		orderService.addOrder(order);
		assertEquals(0.625, stockService.getProfitEarningsRatio(stock), 0.005);
	}

	@Test(expected=StockException.class)
	public void testProfitEarningsRatioStockException1() throws IllegalStockException, IllegalOrderException, StockException{
		Stock stock = stockService.getStock("TEA");
		stockService.getProfitEarningsRatio(stock);
	}

	@Test(expected=IllegalStockException.class)
	public void testProfitEarningsRatioIllegalStockException() throws IllegalStockException, IllegalOrderException, StockException{
		Stock stock = new PreferredStock("SAX", 0.0, 100, 3);;
		stockService.getProfitEarningsRatio(stock);
	}

	@Test(expected=IllegalStockException.class)
	public void testProfitEarningsRatioIllegalStockException2() throws IllegalStockException, IllegalOrderException, StockException{
		stockService.getProfitEarningsRatio("SAX");
	}

	@Test
	public void testAddOrder() throws IllegalOrderException{
		Stock stock = stockService.getStock("TEA");
		Order order = new BuyOrder(stock, 100, 1);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1);
		orderService.addOrder(order);
	}
	
	@Test(expected=IllegalOrderException.class)
	public void testAddOrderIllegalOrderException() throws IllegalOrderException{
		Stock stock = new PreferredStock("SAX", 0.0, 100, 3);
		Order order = new BuyOrder(stock, 100, 1);
		orderService.addOrder(order);
	}

	@Test(expected=IllegalOrderException.class)
	public void testAddOrderIllegalOrderException2() throws IllegalOrderException{
		Stock stock = stockService.getStock("SAX");
		Order order = new BuyOrder(stock, 100, 1);
		orderService.addOrder(order);
	}

	@Test(expected=IllegalOrderException.class)
	public void testAddOrderIllegalOrderException3() throws IllegalOrderException{
		Stock stock = stockService.getStock("TEA");
		Order order = new BuyOrder(stock, 100, 0);
		orderService.addOrder(order);
	}

	@Test(expected=IllegalOrderException.class)
	public void testAddOrderIllegalOrderException4() throws IllegalOrderException{
		Stock stock = stockService.getStock("TEA");
		Order order = new BuyOrder(stock, 0, 1);
		orderService.addOrder(order);
	}

	@Test
	public void testGetGeometricMean() throws IllegalOrderException, IllegalStockException{
		Stock stock = stockService.getStock("TEA");
		Order order = new BuyOrder(stock, 100, 1);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.02);
		orderService.addOrder(order);
		order = new BuyOrder(stock, 100, 1.04);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.06);
		orderService.addOrder(order);
		order = new BuyOrder(stock, 100, 1.08);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.10);
		orderService.addOrder(order);
		order = new BuyOrder(stock, 100, 1.12);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.14);
		orderService.addOrder(order);
		order = new BuyOrder(stock, 100, 1.16);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.18);
		orderService.addOrder(order);
		order = new BuyOrder(stock, 100, 1.20);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.22);
		orderService.addOrder(order);
		
		double geometricMean = orderService.getGeometricMean(stock);
		assertEquals(1.107847496, geometricMean, 0.0000000005);
		
		stock = stockService.getStock("POP");
		geometricMean = orderService.getGeometricMean(stock);
		assertEquals(0, geometricMean, 0.0001);
	}

	@Test
	public void testGetStockPrice() throws IllegalOrderException, IllegalStockException{
		Stock stock = stockService.getStock("TEA");
		Order order = new BuyOrder(stock, 100, 1);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -90);
		((BuyOrder) order).setOperationTime(cal);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.02);
		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -90);
		((SellOrder) order).setOperationTime(cal);
		orderService.addOrder(order);
		
		order = new BuyOrder(stock, 100, 1.04);
		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -70);
		((BuyOrder) order).setOperationTime(cal);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.06);
		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -70);
		((SellOrder) order).setOperationTime(cal);
		orderService.addOrder(order);

		order = new BuyOrder(stock, 100, 1.08);
		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -50);
		((BuyOrder) order).setOperationTime(cal);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.10);
		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -50);
		((SellOrder) order).setOperationTime(cal);
		orderService.addOrder(order);
		
		order = new BuyOrder(stock, 100, 1.12);
		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -15);
		((BuyOrder) order).setOperationTime(cal);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.14);
		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -15);
		((SellOrder) order).setOperationTime(cal);
		orderService.addOrder(order);

		order = new BuyOrder(stock, 100, 1.16);
		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -10);
		((BuyOrder) order).setOperationTime(cal);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.18);
		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -10);
		((SellOrder) order).setOperationTime(cal);
		orderService.addOrder(order);

		order = new BuyOrder(stock, 100, 1.20);
		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -5);
		((BuyOrder) order).setOperationTime(cal);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.22);
		cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -5);
		((SellOrder) order).setOperationTime(cal);
		orderService.addOrder(order);
		
		double stockPrice = orderService.getStockPrice(stock);
		assertEquals(1.19, stockPrice, 0.03);
		
		stock = stockService.getStock("POP");
		stockPrice = orderService.getStockPrice(stock);
		assertEquals(0, stockPrice, 0.0001);

	}

	
	@Test(expected=IllegalStockException.class)
	public void testGetStockPriceIllegalStockException() throws IllegalOrderException, IllegalStockException{
		Stock stock = new PreferredStock("SAX", 0.0, 100, 3);
		orderService.getStockPrice(stock);
	}

	@Test
	public void testAllShareIndex() throws IllegalOrderException, IllegalStockException{
		Stock stock = stockService.getStock("TEA");
		Order order = new BuyOrder(stock, 100, 1);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.02);
		orderService.addOrder(order);
		order = new BuyOrder(stock, 100, 1.04);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.06);
		orderService.addOrder(order);
		order = new BuyOrder(stock, 100, 1.08);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.10);
		orderService.addOrder(order);
		order = new BuyOrder(stock, 100, 1.12);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.14);
		orderService.addOrder(order);
		order = new BuyOrder(stock, 100, 1.16);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.18);
		orderService.addOrder(order);
		order = new BuyOrder(stock, 100, 1.20);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 1.22);
		orderService.addOrder(order);
		
		stock = stockService.getStock("POP");
		order = new BuyOrder(stock, 100, 2);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 2.02);
		orderService.addOrder(order);
		
		stock = stockService.getStock("ALE");
		order = new BuyOrder(stock, 100, 3);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 3.02);
		orderService.addOrder(order);
		
		stock = stockService.getStock("GIN");
		order = new BuyOrder(stock, 100, 4);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 4.02);
		orderService.addOrder(order);
		
		stock = stockService.getStock("JOE");
		order = new BuyOrder(stock, 100, 5);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 5.02);
		orderService.addOrder(order);
		
		double allShareIndex = orderService.getAllShareIndex();
		assertEquals(1.718547161, allShareIndex, 0.000000001);

		order = new BuyOrder(stock, 100, 5.04);
		orderService.addOrder(order);
		order = new SellOrder(stock, 100, 5.06);
		orderService.addOrder(order);

		allShareIndex = orderService.getAllShareIndex();
		assertEquals(1.895477541, allShareIndex, 0.000000001);

	}


}
