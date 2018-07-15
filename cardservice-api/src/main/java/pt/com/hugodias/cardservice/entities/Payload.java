package pt.com.hugodias.cardservice.entities;

/***
 * The {@link Payload} object will hold the details of the credit card, namelly
 * <ul> 
 *   <li>scheme: the network of the card, like visa or mastercard</li>
 *   <li>type: the type of the card, like debit or credit</li>
 *   <li>bank: the name of the bank who issued the card</li>
 * </ul>
 * @author Hugo
 *
 */
public class Payload {
	private String scheme;
	private String type;
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
