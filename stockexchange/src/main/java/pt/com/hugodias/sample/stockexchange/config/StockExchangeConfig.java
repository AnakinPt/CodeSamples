package pt.com.hugodias.sample.stockexchange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pt.com.hugodias.sample.stockexchange.services.NoDBOrdersService;
import pt.com.hugodias.sample.stockexchange.services.NoDBStockService;
import pt.com.hugodias.sample.stockexchange.services.OrdersService;
import pt.com.hugodias.sample.stockexchange.services.StockService;

@Configuration
public class StockExchangeConfig {

	@Bean
	public StockService stockService(){
		return new NoDBStockService();
	}
	
	@Bean
	public int minutesForStockPrice(){
		return 15;
	}
	
	@Bean
	public OrdersService ordersService(){
		return new NoDBOrdersService();
	}
}
