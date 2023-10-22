package uk.gov.dwp.uc.pairtest;

import java.util.Map;

import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.dao.TicketPriceDao;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {
	/**
	 * Should only have private methods other than the one below.
	 */
	private int calculateTotalSeatsToAllocate(TicketTypeRequest... ticketTypeRequests) {
		int totalSeatsToAllocate = 0;
		int infantTicket = 0;
		int childTicket = 0;
		int adultTicket = 0;
		for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {
			if (ticketTypeRequest.getTicketType() == Type.ADULT) {
				adultTicket += ticketTypeRequest.getNoOfTickets();
			}
			else if (ticketTypeRequest.getTicketType() == Type.CHILD) {
				childTicket += ticketTypeRequest.getNoOfTickets();
			}
			else if (ticketTypeRequest.getTicketType() == Type.INFANT) {
				infantTicket += ticketTypeRequest.getNoOfTickets();
			}
		}
		totalSeatsToAllocate  = childTicket+ adultTicket;
		if(infantTicket + childTicket+ adultTicket>20) {
			throw new InvalidPurchaseException("Only a maximum of 20 tickets that can be purchased at a time.");
		}
		if ((infantTicket > 0 || childTicket > 0) && (adultTicket == 0)) {
			throw new InvalidPurchaseException("Child and Infant tickets cannot be purchased without purchasing an Adult ticket.");
		}
		return totalSeatsToAllocate;
	}
	
	private int calculateTotalAmountToPay(TicketTypeRequest... ticketTypeRequests) {
		int totalAmountToPay = 0;
		Map<Type, Integer> ticketPrices = TicketPriceDao.getTicketPrice();
		for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {
			if (ticketTypeRequest.getTicketType() == Type.ADULT) {
				totalAmountToPay += ticketPrices.get(Type.ADULT) * ticketTypeRequest.getNoOfTickets();
			}
			if (ticketTypeRequest.getTicketType() == Type.CHILD) {
				totalAmountToPay += ticketPrices.get(Type.CHILD) * ticketTypeRequest.getNoOfTickets();
			}
		}
		return totalAmountToPay;
	}
	@Override
	public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests)
			throws InvalidPurchaseException {
		int totalSeatsToAllocate = calculateTotalSeatsToAllocate(ticketTypeRequests);
		int totalAmountToPay = calculateTotalAmountToPay(ticketTypeRequests);
		TicketPaymentServiceImpl ticketPaymentServiceImpl = new TicketPaymentServiceImpl();
		ticketPaymentServiceImpl.makePayment(accountId, totalAmountToPay);
		SeatReservationServiceImpl seatReservationServiceImpl = new SeatReservationServiceImpl();
		seatReservationServiceImpl.reserveSeat(accountId, totalSeatsToAllocate);
	}

}
