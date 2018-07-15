package pt.com.hugodias.cardservice.thirdparty;

import pt.com.hugodias.cardservice.entities.Payload;

public interface ICardDetailsProviderService {
	Payload getDetails(String cardNumber) throws CardDetailsException;
}
