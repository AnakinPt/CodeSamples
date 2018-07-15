package pt.com.hugodias.cardservice.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import pt.com.hugodias.cardservice.application.Application;
import pt.com.hugodias.cardservice.entities.CardServiceResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@WebAppConfiguration
public class CardServiceIntegrationTest {

    private MockMvc mockMvc;
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    private HttpMessageConverter<?> mappingJackson2HttpMessageConverter;
   
	@Autowired
	private ICardService cardService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

    }
    
	@Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }
	
	@Test
	public void testVisa() {
		CardServiceResponse cardServiceResponse = cardService.verify("4545687966849299");
		assertThat(cardServiceResponse.isSuccess(), is(true));
		assertThat(cardServiceResponse.getPayload().getScheme(), is("visa"));
		assertThat(cardServiceResponse.getPayload().getType(), is("debit"));
		assertThat(cardServiceResponse.getPayload().getBank(), nullValue());
	}
	
	@Test
	public void testMastercard() {
		CardServiceResponse cardServiceResponse = cardService.verify("5599786963143540");
		assertThat(cardServiceResponse.isSuccess(), is(true));
		assertThat(cardServiceResponse.getPayload().getScheme(), is("mastercard"));
		assertThat(cardServiceResponse.getPayload().getType(), is("credit"));
		assertThat(cardServiceResponse.getPayload().getBank(), nullValue());
	}

	@Test
	public void testAmEx() {
		CardServiceResponse cardServiceResponse = cardService.verify("378843427701724");
		assertThat(cardServiceResponse.isSuccess(), is(true));
		assertThat(cardServiceResponse.getPayload().getScheme(), is("amex"));
		assertThat(cardServiceResponse.getPayload().getType(), is("credit"));
		assertThat(cardServiceResponse.getPayload().getBank(), is("AMERICAN EXPRESS COMPANY"));
	}

	@Test
	public void testNonExistant() {
		CardServiceResponse cardServiceResponse = cardService.verify("x");
		assertThat(cardServiceResponse.isSuccess(), is(false));
	}
	
	@Test
	public void testRestNonExistant() throws Exception {
        mockMvc.perform(get("/card-scheme/verify/x")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)));
	}

	@Test
	public void testRestVisa() throws Exception {
		mockMvc.perform(get("/card-scheme/verify/4545687966849299")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.payload.scheme", is("visa")))
                .andExpect(jsonPath("$.payload.type", is("debit")))
                .andExpect(jsonPath("$.payload.bank", nullValue()));

		mockMvc.perform(get("/card-scheme/verify/4319320012345678")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.payload.scheme", is("visa")))
                .andExpect(jsonPath("$.payload.type", is("debit")))
                .andExpect(jsonPath("$.payload.bank", is("ULSTER BANK")));
	}
	

}
