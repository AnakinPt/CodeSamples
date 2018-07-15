package pt.com.hugodias.cardservice.service;

import pt.com.hugodias.cardservice.entities.CardServiceResponse;

public interface ICardService {
	CardServiceResponse verify(String cardNumber);
}
