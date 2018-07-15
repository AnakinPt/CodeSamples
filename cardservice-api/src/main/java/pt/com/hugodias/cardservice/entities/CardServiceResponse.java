package pt.com.hugodias.cardservice.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/***
 * Represents the response from the CardService
 * 
 * @author Hugo
 *
 */
@ApiModel(description="Class representing the Card Service response")
public class CardServiceResponse {
	@ApiModelProperty(notes="Indicates if the service was called in success", example="true", required=true, position=0)
	private boolean success;
	
	@ApiModelProperty(notes="The details of the card", required=false, position=1)
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
