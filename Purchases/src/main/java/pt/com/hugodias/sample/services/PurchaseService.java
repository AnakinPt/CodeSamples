package pt.com.hugodias.sample.services;

import java.util.Collection;

import pt.com.hugodias.sample.entities.GenericResponse;
import pt.com.hugodias.sample.entities.Purchase;

public interface PurchaseService {
	public static final String KEYS_RETRIEVAL = "KeysRetrieval";
	public static final String AGGREGATE_RESULTS = "AggregateResults";
	public static final String VALUES_RETRIEVAL = "ValuesRetrieval";
	public static final String ADD_PURCHASE = "AddPurchase";
	
	GenericResponse<Collection<Purchase>> getPurchases();
	
	GenericResponse<Long> addPurchase(Purchase purchase); 
}
