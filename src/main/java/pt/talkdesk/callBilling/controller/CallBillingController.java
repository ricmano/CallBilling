package pt.talkdesk.callBilling.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.talkdesk.callBilling.bean.CallDetail;
import pt.talkdesk.callBilling.bean.CallEvent;
import pt.talkdesk.callBilling.bean.CallType;
import pt.talkdesk.callBilling.repository.CallRepository;
import pt.talkdesk.callBilling.repository.ClientRepository;

@RestController
public class CallBillingController {
	@Autowired
	CallRepository callRepository;

	@Autowired
	ClientRepository clientRepository;

	@RequestMapping(value = "/billing/{callDetail}")
	public Double calculateCallCost(@PathVariable CallDetail callDetail) {
		Double cost = 0.001;

		callDetail.setCost(cost);
		callRepository.save(callDetail);

		return cost;
	}

	@RequestMapping(value = "/detailedBilling")
	public Double calculateCallCost(@RequestParam(value="accountId") String accountId,
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

	@RequestMapping("/searchCallsByAccount/{accountId}")
	public List<CallDetail> getCallsByAccount(@PathVariable String accountId) {
		return callRepository.findByAccountId(accountId);
	}

	@RequestMapping("/searchCallById/{callId}")
	public CallDetail getCallById(@PathVariable String callId) {
		return callRepository.findOne(callId);
	}
	
	private Double billingFormula() {
		return 0.001;
	}
}
