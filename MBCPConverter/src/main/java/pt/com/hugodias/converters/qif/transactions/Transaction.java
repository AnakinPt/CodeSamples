package pt.com.hugodias.converters.qif.transactions;

public class Transaction {

	private String transactionDate;
	private String valueDate;
	private String payee;
	private String type;
	private double ammount;
	private double balance;
	private String empty;
	
	public Transaction(){
		
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getValueDate() {
		return valueDate;
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmmount() {
		return ammount;
	}

	public void setAmmount(double ammount) {
		this.ammount = ammount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public void setEmpty(String empty){
		this.empty = empty;
	}
}
