package pt.com.hugodias.sample.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Purchase implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String productType;
	private Date expires;
	private List<Details> purchaseDetails;
	
	public Purchase(long id, String productType, Date expires) {
		super();
		this.id = id;
		this.productType = productType;
		this.expires = expires;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	public List<Details> getPurchaseDetails() {
		return purchaseDetails;
	}

	public void setPurchaseDetails(List<Details> purchaseDetails) {
		this.purchaseDetails = purchaseDetails;
	}

	@Override
	public String toString() {
		return "Purchase [id=" + id + ", productType=" + productType + ", expires=" + expires + ", purchaseDetails="
				+ purchaseDetails + "]";
	}
	
	
	
}
