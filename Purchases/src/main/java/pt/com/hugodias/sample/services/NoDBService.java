package pt.com.hugodias.sample.services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IdGenerator;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;

import pt.com.hugodias.sample.entities.Purchase;

public class NoDBService implements DatabaseService {

	private IMap<Long,Purchase> purchasesMap;
	
	private IdGenerator idGenerator;
	
	@Autowired
	private HazelcastInstance hazelcastInstance;

	@Autowired
	public NoDBService(HazelcastInstance instance) {
		this.hazelcastInstance = instance;
		purchasesMap = hazelcastInstance.getMap("purchases");
		idGenerator = hazelcastInstance.getIdGenerator("purchasesId");
		idGenerator.init(1l);
		init();
	}

	
	
	
	private void init() {
		Calendar cal = Calendar.getInstance();
		long id = idGenerator.newId();
		cal.set(2016, 10, 01, 16, 00, 00);
		Purchase purchase = new Purchase(id, "Alpha", cal.getTime());
		purchasesMap.put(id, purchase);
		id = idGenerator.newId();
		purchase = new Purchase(id, "Beta", cal.getTime());
		purchasesMap.put(id, purchase);
		id = idGenerator.newId();
		cal = Calendar.getInstance();
		cal.set(2016, 01, 01, 16, 00, 00);
		purchase = new Purchase(id, "Charlie", cal.getTime());
		purchasesMap.put(id, purchase);
		
	}




	@Override
	public long addPurchase(Purchase purchase) {
		long id = idGenerator.newId();
		purchase.setId(id);
		purchasesMap.put(id, purchase);
		return id;
	}

	@Override
	public Purchase getPurchase(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Purchase> getPurchases() {
		Calendar cal = Calendar.getInstance();
		Date time = cal.getTime();
		Predicate notExpired = Predicates.greaterThan("expires", time);
		return purchasesMap.values(notExpired);
	}

	@Override
	public Collection<Purchase> getPurchases(List<Long> ids) {
		// TODO Auto-generated method stub
		return purchasesMap.getAll(new HashSet(ids)).values();
	}

}
