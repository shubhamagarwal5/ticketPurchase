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

	@Override
	public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests)
			throws InvalidPurchaseException {
		Map<Type, Integer> ticketPrices = TicketPriceDao.getTicketPrice();
		int totalSeatsToAllocate = 0;
		int totalAmountToPay = 0;
		int adult = 0;
		int child = 0;
		int infant = 0;
		for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {

			if (ticketTypeRequest.getNoOfTickets() > 20) {
				throw new InvalidPurchaseException("Only a maximum of 20 tickets that can be purchased at a time.");
			}
			if (ticketTypeRequest.getTicketType() == Type.ADULT) {
				totalAmountToPay += ticketPrices.get(Type.ADULT) * ticketTypeRequest.getNoOfTickets();
				adult += ticketTypeRequest.getNoOfTickets();
			}
			if (ticketTypeRequest.getTicketType() == Type.CHILD) {
				totalAmountToPay += ticketPrices.get(Type.CHILD) * ticketTypeRequest.getNoOfTickets();
				child += ticketTypeRequest.getNoOfTickets();
			}
			if (ticketTypeRequest.getTicketType() == Type.INFANT) {
				infant += ticketTypeRequest.getNoOfTickets();
			}
		}
		if(infant+child+adult>20) {
			throw new InvalidPurchaseException("Only a maximum of 20 tickets that can be purchased at a time.");
		}
		if ((infant > 0 || child > 0) && (adult == 0)) {
			throw new InvalidPurchaseException("Child and Infant tickets cannot be purchased without purchasing an Adult ticket.");
		}
		totalSeatsToAllocate = adult + child;
		TicketPaymentServiceImpl ticketPaymentServiceImpl = new TicketPaymentServiceImpl();
		ticketPaymentServiceImpl.makePayment(accountId, totalAmountToPay);
		SeatReservationServiceImpl seatReservationServiceImpl = new SeatReservationServiceImpl();
		seatReservationServiceImpl.reserveSeat(accountId, totalSeatsToAllocate);
	}

}
