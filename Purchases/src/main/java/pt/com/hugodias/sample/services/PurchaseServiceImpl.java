package pt.com.hugodias.sample.services;

import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.util.StopWatch.TaskInfo;

import pt.com.hugodias.sample.entities.Error;
import pt.com.hugodias.sample.entities.GenericResponse;
import pt.com.hugodias.sample.entities.Purchase;

@Service
public class PurchaseServiceImpl implements PurchaseService{

	private static Logger log;
	
	protected Logger getLog(){
		if (log == null)
			log = Logger.getLogger(PurchaseServiceImpl.class.getName());
		return log;
	}
	
	
	@Autowired
	private DatabaseService database;
	
	@Override
	public GenericResponse<Collection<Purchase>> getPurchases() {
		try {
			//The first operation retrieves purchase details related to valid company purchases.
			//1.	it fetches a collection of all existing purchases from the database;
			StopWatch sw = new StopWatch();
			sw.start(PurchaseService.KEYS_RETRIEVAL);
			Collection<Purchase> purchases = database.getPurchases();
			sw.stop();
			//2.	aggregates the results that are valid for the current time;
			sw.start(PurchaseService.AGGREGATE_RESULTS);
			List<Long> list = new LinkedList<Long>();
			Calendar cal = getNow();
			Calendar expires = Calendar.getInstance();
			for (Purchase purchase : purchases) {
				expires.setTime(purchase.getExpires());
				if(cal.before(expires)){
					list.add(purchase.getId());
				}
			}
			sw.stop();
			//3.	subsequently calls the database with a collection of the aggregated purchase ids;
			//4.	the database yields all queried purchase details;
			sw.start(PurchaseService.VALUES_RETRIEVAL);
			purchases = database.getPurchases(list);
			sw.stop();
			TaskInfo[] metrics = sw.getTaskInfo();
			return new GenericResponse<Collection<Purchase>>(purchases, Error.OK, metrics);
		} catch (Exception e) {
			getLog().fatal(e.getMessage(), e);
			return new GenericResponse<Collection<Purchase>>(null, Error.ERROR, null, e.getMessage());
		}
	}

	public Calendar getNow() {
		return Calendar.getInstance();
	}

	@Override
	public GenericResponse<Long> addPurchase(Purchase purchase) {
		try {
			StopWatch sw = new StopWatch();
			sw.start(PurchaseService.ADD_PURCHASE);
			long id = database.addPurchase(purchase);
			sw.stop();
			return new GenericResponse<Long>(id, Error.OK, sw.getTaskInfo());
		} catch (Exception e) {
			getLog().fatal(e.getMessage(), e);
			return new GenericResponse<Long>(null, Error.ERROR, null, e.getMessage());
		}
		
	}

}
