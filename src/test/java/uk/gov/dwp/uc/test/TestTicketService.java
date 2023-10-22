package uk.gov.dwp.uc.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TestTicketService {
	@Test
	public void testPurchasingMoreThanMaximumTickets() {
		try {
			TicketTypeRequest ticketTypeRequestAdult = new TicketTypeRequest(Type.ADULT, 12);
			TicketTypeRequest ticketTypeRequestChild = new TicketTypeRequest(Type.CHILD, 5);
			TicketTypeRequest ticketTypeRequestInfant = new TicketTypeRequest(Type.INFANT, 5);
			TicketServiceImpl ticketService = new TicketServiceImpl();
			long accountId = 1;
			ticketService.purchaseTickets(accountId, ticketTypeRequestAdult, ticketTypeRequestChild,
					ticketTypeRequestInfant);
		} catch (InvalidPurchaseException e) {
			assertTrue(e.getMessage().equals("Only a maximum of 20 tickets that can be purchased at a time."));
		}
	}

	@Test
	public void testPurchasingMoreThanMaximumTicketsForAdult() {
		try {
			TicketTypeRequest ticketTypeRequestAdult = new TicketTypeRequest(Type.ADULT, 22);
			TicketTypeRequest ticketTypeRequestChild = new TicketTypeRequest(Type.CHILD, 5);
			TicketTypeRequest ticketTypeRequestInfant = new TicketTypeRequest(Type.INFANT, 5);
			TicketServiceImpl ticketService = new TicketServiceImpl();
			long accountId = 1;
			ticketService.purchaseTickets(accountId, ticketTypeRequestAdult, ticketTypeRequestChild,
					ticketTypeRequestInfant);
		} catch (InvalidPurchaseException e) {
			assertTrue(e.getMessage().equals("Only a maximum of 20 tickets that can be purchased at a time."));
		}
	}

	@Test
	public void testPurchasingInfantTicketsWithoutAdult() {
		try {
			TicketTypeRequest ticketTypeRequestInfant = new TicketTypeRequest(Type.INFANT, 5);
			TicketServiceImpl ticketService = new TicketServiceImpl();
			long accountId = 1;
			ticketService.purchaseTickets(accountId, ticketTypeRequestInfant);
		} catch (InvalidPurchaseException e) {
			assertTrue(e.getMessage()
					.equals("Child and Infant tickets cannot be purchased without purchasing an Adult ticket."));
		}
	}

	@Test
	public void testPurchasingChildTicketsWithoutAdult() {
		try {
			TicketTypeRequest ticketTypeRequestChild = new TicketTypeRequest(Type.CHILD, 5);
			TicketServiceImpl ticketService = new TicketServiceImpl();
			long accountId = 1;
			ticketService.purchaseTickets(accountId, ticketTypeRequestChild);
		} catch (InvalidPurchaseException e) {
			assertTrue(e.getMessage()
					.equals("Child and Infant tickets cannot be purchased without purchasing an Adult ticket."));
		}
	}

	@Test
	public void testPurchasingAdultInfantChildTickets() {
		TicketTypeRequest ticketTypeRequestAdult = new TicketTypeRequest(Type.ADULT, 2);
		TicketTypeRequest ticketTypeRequestChild = new TicketTypeRequest(Type.CHILD, 5);
		TicketTypeRequest ticketTypeRequestInfant = new TicketTypeRequest(Type.INFANT, 5);
		TicketServiceImpl ticketService = new TicketServiceImpl();
		long accountId = 1;
		ticketService.purchaseTickets(accountId, ticketTypeRequestAdult, ticketTypeRequestChild,
				ticketTypeRequestInfant);
	}

	@Test
	public void testPurchasingOnlyAdultTickets() {
		TicketTypeRequest ticketTypeRequestAdult = new TicketTypeRequest(Type.ADULT, 2);
		TicketTypeRequest ticketTypeRequestChild = new TicketTypeRequest(Type.ADULT, 5);
		TicketTypeRequest ticketTypeRequestInfant = new TicketTypeRequest(Type.ADULT, 6);
		TicketServiceImpl ticketService = new TicketServiceImpl();
		long accountId = 1;
		ticketService.purchaseTickets(accountId, ticketTypeRequestAdult, ticketTypeRequestChild,
				ticketTypeRequestInfant);
	}
}
