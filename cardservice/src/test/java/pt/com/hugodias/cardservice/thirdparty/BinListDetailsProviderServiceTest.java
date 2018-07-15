package pt.com.hugodias.cardservice.thirdparty;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pt.com.hugodias.cardservice.application.Application;
import pt.com.hugodias.cardservice.entities.CardServiceResponse;
import pt.com.hugodias.cardservice.entities.Payload;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class BinListDetailsProviderServiceTest {

	@Autowired
	private BinListDetailsProviderService cardService;
	
	@Test
	public void testVisa() throws CardDetailsException {
		Payload payload = cardService.getDetails("4545687966849299");
		assertThat(payload.getScheme(), is("visa"));
		assertThat(payload.getType(), is("debit"));
		assertThat(payload.getBank(), nullValue());
	}
	
	@Test
	public void testMastercard() throws CardDetailsException {
		Payload payload = cardService.getDetails("5599786963143540");
		assertThat(payload.getScheme(), is("mastercard"));
		assertThat(payload.getType(), is("credit"));
		assertThat(payload.getBank(), nullValue());
	}

	@Test
	public void testAmEx() throws CardDetailsException {
		Payload payload = cardService.getDetails("378843427701724");
		assertThat(payload.getScheme(), is("amex"));
		assertThat(payload.getType(), is("credit"));
		assertThat(payload.getBank(), is("AMERICAN EXPRESS COMPANY"));
	}

	@Test(expected=CardDetailsException.class)
	public void testNonExistant() throws CardDetailsException {
		cardService.getDetails("9999999999999");
	}

}
