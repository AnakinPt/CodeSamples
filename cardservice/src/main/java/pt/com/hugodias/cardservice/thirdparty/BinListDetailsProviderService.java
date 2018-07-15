package pt.com.hugodias.cardservice.thirdparty;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import pt.com.hugodias.cardservice.entities.Payload;

public class BinListDetailsProviderService implements ICardDetailsProviderService {

	public void init() {
		Unirest.setObjectMapper(new ObjectMapper() {
		    private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
		                = new com.fasterxml.jackson.databind.ObjectMapper();

		    public <T> T readValue(String value, Class<T> valueType) {
		        try {
		            return jacksonObjectMapper.readValue(value, valueType);
		        } catch (IOException e) {
		            throw new RuntimeException(e);
		        }
		    }

		    public String writeValue(Object value) {
		        try {
		            return jacksonObjectMapper.writeValueAsString(value);
		        } catch (JsonProcessingException e) {
		            throw new RuntimeException(e);
		        }
		    }
		});
	}
	
	@Override
	public Payload getDetails(String cardNumber) throws CardDetailsException {
		
		try {
			HttpResponse<BinListResponse> response = Unirest.get("https://lookup.binlist.net/{cardNumber}")
					.header("Accept-Version", "3" )
					.routeParam("cardNumber", cardNumber)
					.asObject(BinListResponse.class);
			int status = response.getStatus();
			if (status != 200) {
				throw new CardDetailsException("Error returned: "+status);
			}
			return mapBinListResponse2Payload(response.getBody());
		} catch (UnirestException e) {
			throw new CardDetailsException(e);
		}
	}
	
	private Payload mapBinListResponse2Payload(BinListResponse response) {
		Payload payload = new Payload();
		payload.setBank(response.getBank().getName());
		payload.setScheme(response.getScheme());
		payload.setType(response.getType());
		return payload;
	}

}
