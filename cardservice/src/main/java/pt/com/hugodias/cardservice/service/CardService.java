package pt.com.hugodias.cardservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.com.hugodias.cardservice.entities.CardServiceResponse;
import pt.com.hugodias.cardservice.entities.Payload;
import pt.com.hugodias.cardservice.thirdparty.CardDetailsException;
import pt.com.hugodias.cardservice.thirdparty.ICardDetailsProviderService;

@RestController
public class CardService implements ICardService {

	@Autowired
	private ICardDetailsProviderService cardDetailsProviderService; 
	
	
	@Override
	@RequestMapping(value="/card-scheme/verify/{cardNumber}", method=RequestMethod.GET)
	public CardServiceResponse verify(@PathVariable("cardNumber") String cardNumber) {
		CardServiceResponse response = new CardServiceResponse();
		try {
			Payload payload = cardDetailsProviderService.getDetails(cardNumber);
			response.setSuccess(true);
			response.setPayload(payload);
		} catch (CardDetailsException e) {
			response.setSuccess(false);
		}
		return response;
	}

}
