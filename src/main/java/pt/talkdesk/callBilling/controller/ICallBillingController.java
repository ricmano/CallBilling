package pt.talkdesk.callBilling.controller;

import java.math.BigDecimal;

import pt.talkdesk.callBilling.bean.CallDetail;

public interface ICallBillingController {

	/**
	 * Method to calculate the cost of a given call
	 * @param callDetail
	 * @return
	 */
	public BigDecimal calculateCallCost(CallDetail callDetail);
	
}
