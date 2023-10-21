package uk.gov.dwp.uc.pairtest.dao;

import java.util.HashMap;
import java.util.Map;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;

public class TicketPriceDao {
	 private static Map<Type, Integer> ticketPrice = new HashMap<>();
	 
	 public static Map<Type, Integer> getTicketPrice(){
		 ticketPrice.put(Type.INFANT, 0);
		 ticketPrice.put(Type.CHILD, 10);
		 ticketPrice.put(Type.ADULT, 20);
		 return ticketPrice;
	 }
}
