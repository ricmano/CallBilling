package pt.talkdesk.callBilling.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.talkdesk.callBilling.bean.CallDetail;
import pt.talkdesk.callBilling.bean.CallEvent;
import pt.talkdesk.callBilling.bean.CallType;

@RestController
public class CallBillingController {

	@RequestMapping(value = "/billing/{callDetail}")
	public Double calculateCallCost(@PathVariable CallDetail callDetail) {
		return new Double(0.001);
	}

	@RequestMapping(value = "/detailedBilling")
	public Double calculateCallCosta(@RequestParam(value="accountId") String accountId,
			@RequestParam(value="callId") String callId,
			@RequestParam(value="customerPhoneNumber") String customerPhoneNumber,
			@RequestParam(value="duration") Integer duration,
			@RequestParam(value="forwardedPhoneNumber", required=false) String forwardedPhoneNumber,
			@RequestParam(value="talkdeskPhoneNumber") String talkdeskPhoneNumber,
			@RequestParam(value="timestamp") Long timestamp,
			@RequestParam(value="type") String type) {
		CallDetail callDetail = new CallDetail();
		callDetail.setAccountId(accountId);
		callDetail.setCallId(callId);
		callDetail.setCustomerPhoneNumber(customerPhoneNumber);
		callDetail.setDuration(duration);
		callDetail.setEvent(CallEvent.call_finished);
		callDetail.setForwardedPhoneNumber(forwardedPhoneNumber);
		callDetail.setTalkdeskPhoneNumber(talkdeskPhoneNumber);
		callDetail.setTimestamp(new Date(timestamp));
		callDetail.setType(CallType.valueOf(type));
		return calculateCallCost(callDetail);
	}

	@RequestMapping(value = "/test")
	public String helloWorld() {
		return "Hello World";
	}
}
