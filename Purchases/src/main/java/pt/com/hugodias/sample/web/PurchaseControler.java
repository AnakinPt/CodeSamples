package pt.com.hugodias.sample.web;

import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.com.hugodias.sample.config.PurchaseConfig;
import pt.com.hugodias.sample.entities.GenericResponse;
import pt.com.hugodias.sample.entities.Purchase;
import pt.com.hugodias.sample.services.PurchaseService;

@Controller
@EnableAutoConfiguration
@SpringBootApplication
@Import(PurchaseConfig.class)
public class PurchaseControler {

	private static final String SEMICOLON = ";";
	@Autowired
	private PurchaseService purchaseService;
	
	
	@RequestMapping("/")
	@ResponseBody
	GenericResponse<Collection<Purchase>> getPurchases() {
		 return purchaseService.getPurchases();
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(PurchaseControler.class, args);
	}
			
	@RequestMapping(path="/addPurchase", method=RequestMethod.POST)
	public GenericResponse<Long> addPurchase(@Valid String productType, @Valid Date expires){
		Purchase purchase = new Purchase(0, productType, expires);
		return purchaseService.addPurchase(purchase);
	}
	
}
