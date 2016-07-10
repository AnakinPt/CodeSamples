package pt.com.hugodias.sample.services;

import java.util.Calendar;

public class TestPurchaseServiceImpl extends PurchaseServiceImpl {

	@Override
	public Calendar getNow() {
		Calendar now = Calendar.getInstance();
		now.set(2016, 10, 01, 15, 00, 00);
		return now;
	}

}
