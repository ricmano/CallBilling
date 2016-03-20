package pt.talkdesk.callBilling.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import pt.talkdesk.callBilling.CallBillingConstants;
import pt.talkdesk.callBilling.bean.CallDetail;
import pt.talkdesk.callBilling.bean.ClientDetail;
import pt.talkdesk.callBilling.repository.CallRepository;
import pt.talkdesk.callBilling.repository.ClientRepository;

@Controller
public class CallBillingController implements ICallBillingController {
	@Autowired
	CallRepository callRepository;

	@Autowired
	ClientRepository clientRepository;

	/**
	 * Method to calculate the cost of a given call
	 * @param callDetail
	 * @return
	 */
	public BigDecimal calculateCallCost(CallDetail callDetail) {
		BigDecimal talkDeskNumberCost = getTalkDeskNumberCost(callDetail.getTalkdeskPhoneNumber());
		BigDecimal externalNumberCost = getExternalNumberCost(callDetail.getForwardedPhoneNumber());

		BigDecimal cost = billingFormula(talkDeskNumberCost, externalNumberCost,
				getClientMargin(callDetail.getAccountId()), callDetail.getDuration());

		callDetail.setCost(cost);
		callRepository.save(callDetail);

		return cost;
	}

	/**
	 * Billing formula
	 * 
	 * @param talkDeskNumberCost
	 *            cost per minute
	 * @param externalNumberCost
	 *            cost per minute
	 * @param profitMargin
	 *            margin per minute
	 * @param callDuration
	 *            call duration in seconds
	 * @return call cost
	 */
	public BigDecimal billingFormula(BigDecimal talkDeskNumberCost, BigDecimal externalNumberCost,
			BigDecimal profitMargin, Integer callDuration) {
		Integer minutesToCharge = (callDuration / 60) + (callDuration % 60 > 0 ? 1 : 0);
		BigDecimal costPerMinute = talkDeskNumberCost.add(profitMargin).add(externalNumberCost).setScale(5, RoundingMode.HALF_UP);

		return costPerMinute.multiply(new BigDecimal(minutesToCharge));
	}
	
	/**
	 * Method to retrieve Client Margin
	 * @param accountId
	 * @return
	 */
	public BigDecimal getClientMargin(String accountId) {
		ClientDetail clientDetail = clientRepository.findOne(accountId);
		if (clientDetail == null) {
			clientDetail = clientRepository.findOne(CallBillingConstants.DEFAULT_CLIENT_ID);
		}
		return new BigDecimal(clientDetail.getMargin());
	}

	/**
	 * Method to calculate TalkDesk Number cost (per minute)
	 * @param talkDeskNumber
	 * @return
	 */
	public BigDecimal getTalkDeskNumberCost(String talkDeskNumber) {
		boolean usToolFree = false;
		boolean ukToolFree = false;
		if (usToolFree) {
			return CallBillingConstants.TALK_DESK_US_TOOL_FREE_COST_PER_MINUTE;
		} else if (ukToolFree) {
			return CallBillingConstants.TALK_DESK_UK_TOOL_FREE_COST_PER_MINUTE;
		} else {
			return CallBillingConstants.TALK_DESK_DEFAULT_COST_PER_MINUTE;
		}
	}

	/**
	 * Method to calculate External Number cost (per minute)
	 * @param externalNumber
	 * @return
	 */
	public BigDecimal getExternalNumberCost(String externalNumber) {
		if (externalNumber == null) {
			return CallBillingConstants.WEB_BROWSER_COST_PER_MINUTE;
		} else {
			// Get cost from twilio
			return new BigDecimal(0.01);
		}
	}
}
