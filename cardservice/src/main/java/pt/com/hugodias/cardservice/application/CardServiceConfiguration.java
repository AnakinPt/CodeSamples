package pt.com.hugodias.cardservice.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pt.com.hugodias.cardservice.service.CardService;
import pt.com.hugodias.cardservice.service.ICardService;
import pt.com.hugodias.cardservice.thirdparty.BinListDetailsProviderService;
import pt.com.hugodias.cardservice.thirdparty.ICardDetailsProviderService;

@Configuration
public class CardServiceConfiguration {

	@Bean
	public ICardService getCardService() {
		return new CardService();
	}
	
	@Bean
	public ICardDetailsProviderService getCardDetailsProviderService() {
		BinListDetailsProviderService binListDetailsProviderService = new BinListDetailsProviderService();
		binListDetailsProviderService.init();
		return binListDetailsProviderService;
	}
}
