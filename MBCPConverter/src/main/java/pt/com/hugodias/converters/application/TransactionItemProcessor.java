package pt.com.hugodias.converters.application;

import org.springframework.batch.item.ItemProcessor;

import pt.com.hugodias.converters.qif.transactions.QIFTransaction;
import pt.com.hugodias.converters.qif.transactions.Transaction;

public class TransactionItemProcessor implements ItemProcessor<Transaction, QIFTransaction> {

	public QIFTransaction process(Transaction item) throws Exception {
		QIFTransaction transaction = new QIFTransaction();
		transaction.setAmmount(item.getAmmount());
		transaction.setDate(item.getTransactionDate());
		transaction.setPayee(item.getPayee());
		return transaction;
	}

}
