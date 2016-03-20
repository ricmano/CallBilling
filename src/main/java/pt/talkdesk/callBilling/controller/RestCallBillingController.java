package pt.talkdesk.callBilling.controller;

import java.math.BigDecimal;
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

@RestController
public class RestCallBillingController {
	@Autowired
	CallRepository callRepository;

	@Autowired
	ICallBillingController callBillingController;

	/**
	 * Rest WebService used to calculate cost from a CallDetail Object
	 * @param callDetail
	 * @return
	 */
	@RequestMapping(value = "/billing/{callDetail}")
	public BigDecimal calculateCallCost(@PathVariable CallDetail callDetail) {
		return callBillingController.calculateCallCost(callDetail);
	}

	/**
	 * Rest WebService used to calculate cost from detailed information passed as parameters
	 * A Call Detail object will be created from this information
	 * @param accountId
	 * @param callId
	 * @param customerPhoneNumber
	 * @param duration
	 * @param forwardedPhoneNumber
	 * @param talkdeskPhoneNumber
	 * @param timestamp
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/detailedBilling")
	public BigDecimal calculateCallCost(@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "callId") String callId,
			@RequestParam(value = "customerPhoneNumber") String customerPhoneNumber,
			@RequestParam(value = "duration") Integer duration,
			@RequestParam(value = "forwardedPhoneNumber", required = false) String forwardedPhoneNumber,
			@RequestParam(value = "talkdeskPhoneNumber") String talkdeskPhoneNumber,
			@RequestParam(value = "timestamp") Long timestamp, @RequestParam(value = "type") String type) {

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

	/**
	 * Rest WebService to search calls from a given account
	 * @param accountId
	 * @return
	 */
	@RequestMapping("/searchCallsByAccount/{accountId}")
	public List<CallDetail> getCallsByAccount(@PathVariable String accountId) {
		return callRepository.findByAccountId(accountId);
	}

	/**
	 * Rest WebService to search call detail by Call Id
	 * @param callId
	 * @return
	 */
	@RequestMapping("/searchCallById/{callId}")
	public List<CallDetail> getCallsById(@PathVariable String callId) {
		return callRepository.findByCallId(callId);
	}
}
