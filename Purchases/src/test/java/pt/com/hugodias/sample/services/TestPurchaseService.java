package pt.com.hugodias.sample.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pt.com.hugodias.sample.config.TestConfig;
import pt.com.hugodias.sample.entities.Error;
import pt.com.hugodias.sample.entities.GenericResponse;
import pt.com.hugodias.sample.entities.Purchase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfig.class)
public class TestPurchaseService {
	private static final String DB_ERROR = "DB Error";

	@Autowired
	private PurchaseService service;
	
	@Autowired
	private DatabaseService dbService;
	
	@Before
	public void setUp(){
		Mockito.reset(dbService);
	}
	
	
	@Test
	public void testGetPurchases(){
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 10, 01, 16, 00, 00);
		Collection<Purchase> list = new LinkedList<Purchase>();
		list.add(new Purchase(1, "Alpha",cal.getTime()));
		list.add(new Purchase(2, "Beta",cal.getTime()));
		cal = Calendar.getInstance();
		cal.set(2015, 10, 01, 16, 00, 00);
		list.add(new Purchase(3, "Charlie",cal.getTime()));
		when(dbService.getPurchases()).thenReturn(list);
		
		Collection<Purchase> result = new LinkedList<Purchase>();
		result.add(new Purchase(1, "Alpha",cal.getTime()));
		result.add(new Purchase(2, "Beta",cal.getTime()));
		
		when(dbService.getPurchases(anyList())).thenReturn(result);
		GenericResponse<Collection<Purchase>> purchases = service.getPurchases();
		assertEquals(Error.OK, purchases.getError());
		assertEquals(2, purchases.getResult().size());
	}

	
	@Test
	public void testGetPurchasesNoResults(){
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 10, 01, 16, 00, 00);
		Collection<Purchase> list = new LinkedList<Purchase>();
		cal = Calendar.getInstance();
		cal.set(2015, 10, 01, 16, 00, 00);
		when(dbService.getPurchases()).thenReturn(list);
		
		Collection<Purchase> result = new LinkedList<Purchase>();
		
		when(dbService.getPurchases(anyList())).thenReturn(result);
		GenericResponse<Collection<Purchase>> purchases = service.getPurchases();
		assertEquals(Error.OK, purchases.getError());
		assertEquals(0, purchases.getResult().size());
	}

	@Test
	public void testGetPurchasesDBError(){
		when(dbService.getPurchases()).thenThrow(new RuntimeException(DB_ERROR));
		GenericResponse<Collection<Purchase>> purchases = service.getPurchases();
		assertEquals(Error.ERROR, purchases.getError());
		assertEquals(DB_ERROR, purchases.getErrorMessage());
	}
	
	@Test
	public void testAddPurchase(){
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 10, 01, 16, 00, 00);
		Purchase purchase = new Purchase(0, "Charlie", cal.getTime());
		when(dbService.addPurchase(purchase)).thenReturn(4l);
		GenericResponse<Long> result = service.addPurchase(purchase);
		assertEquals(Error.OK, result.getError());
		assertEquals(new Long(4), result.getResult());
	}

	@Test
	public void testAddPurchaseException(){
		Calendar cal = Calendar.getInstance();
		cal.set(2016, 10, 01, 16, 00, 00);
		Purchase purchase = new Purchase(0, "Charlie", cal.getTime());
		when(dbService.addPurchase(purchase)).thenThrow(new RuntimeException(DB_ERROR));
		GenericResponse<Long> result = service.addPurchase(purchase);
		assertEquals(Error.ERROR, result.getError());
		assertEquals(DB_ERROR, result.getErrorMessage());
	}

}
