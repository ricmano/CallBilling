package pt.talkdesk.callBilling.controller;

import java.math.BigDecimal;

public interface IPhoneNumberCostController {

	public BigDecimal getCostPerMinute(String phoneNumber);
	
}
