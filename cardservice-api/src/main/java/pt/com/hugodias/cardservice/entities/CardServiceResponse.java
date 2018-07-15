package pt.com.hugodias.cardservice.entities;

/***
 * Represents the response from the CardService
 * 
 * @author Hugo
 *
 */
public class CardServiceResponse {
	private boolean success;
	private Payload payload;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Payload getPayload() {
		return payload;
	}
	public void setPayload(Payload payload) {
		this.payload = payload;
	}
	
}
