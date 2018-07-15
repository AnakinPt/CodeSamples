package pt.com.hugodias.cardservice.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import pt.com.hugodias.cardservice.entities.CardServiceResponse;
import pt.com.hugodias.cardservice.entities.Payload;
import pt.com.hugodias.cardservice.thirdparty.CardDetailsException;
import pt.com.hugodias.cardservice.thirdparty.ICardDetailsProviderService;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceTest {

	@InjectMocks
	private CardService cardService;
	
	@Mock
	private ICardDetailsProviderService detailsServiceMock;
	
	@Mock
	private Payload payloadMock;

	@Test
	public void verify_returns_payload() throws CardDetailsException {
		when(detailsServiceMock.getDetails("378843427701724")).thenReturn(payloadMock);
		CardServiceResponse cardServiceResponse = cardService.verify("378843427701724");
		assertThat(cardServiceResponse.getPayload(), is(payloadMock));
		assertThat(cardServiceResponse.isSuccess(), is(true));
	}

	@Test
	public void verify_returnsFalse() throws CardDetailsException {
		when(detailsServiceMock.getDetails("x")).thenThrow(new CardDetailsException());
		CardServiceResponse cardServiceResponse = cardService.verify("x");
		assertThat(cardServiceResponse.isSuccess(), is(false));
	}

}
