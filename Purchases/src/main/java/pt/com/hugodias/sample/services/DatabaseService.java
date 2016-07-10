package pt.com.hugodias.sample.services;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import pt.com.hugodias.sample.entities.Purchase;

public interface DatabaseService {
	long addPurchase(Purchase purchase);
	
	Purchase getPurchase(long id);
	
	Collection<Purchase> getPurchases();
	
	Collection<Purchase> getPurchases(List<Long> ids);
}
