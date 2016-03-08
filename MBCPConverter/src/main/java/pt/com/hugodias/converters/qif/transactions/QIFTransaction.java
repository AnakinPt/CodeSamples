package pt.com.hugodias.converters.qif.transactions;

public class QIFTransaction {
	private String date;
	private double ammount;
	private String payee;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getAmmount() {
		return ammount;
	}
	public void setAmmount(double ammount) {
		this.ammount = ammount;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(String.format("D%1$s\r\n", date));
		sb.append(String.format("T%1$-15.2f\r\n", ammount));
		sb.append(String.format("P%1$s\r\nM%1$s\r\n", payee));
		sb.append("^");
		return sb.toString();
	}
}
