package pt.com.hugodias.cardservice.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/***
 * The {@link Payload} object will hold the details of the credit card:
 * <ul> 
 *   <li>scheme: the network of the card, like visa or mastercard</li>
 *   <li>type: the type of the card, like debit or credit</li>
 *   <li>bank: the name of the bank who issued the card</li>
 * </ul>
 * @author Hugo
 *
 */
@ApiModel(description="Class to represent the details of the card")
public class Payload {
	@ApiModelProperty(notes="The scheme aka network of the card", required=true, example="visa", position=0)
	private String scheme;
	@ApiModelProperty(notes="The type of the card", required=true, example="debit", position=1)
	private String type;
	@ApiModelProperty(notes="The bank who issued the card", required=true, example="ULSTER BANK", position=2)
	private String bank;
	
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	
	
}
