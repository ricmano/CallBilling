package pt.talkdesk.callBilling.bean;

import java.util.Date;

public class CallDetail {
	private CallEvent event;
	
	private String type;
	
	private Integer duration;
	
	private String accountId;
	
	private String callId;
	
	private String talkdeskPhoneNumber;
	
	private String customerPhoneNumber;
	
	private String forwardedPhoneNumber;
	
	private Date timestamp;

	public CallEvent getEvent() {
		return event;
	}

	public void setEvent(CallEvent event) {
		this.event = event;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public String getTalkdeskPhoneNumber() {
		return talkdeskPhoneNumber;
	}

	public void setTalkdeskPhoneNumber(String talkdeskPhoneNumber) {
		this.talkdeskPhoneNumber = talkdeskPhoneNumber;
	}

	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}

	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}

	public String getForwardedPhoneNumber() {
		return forwardedPhoneNumber;
	}

	public void setForwardedPhoneNumber(String forwardedPhoneNumber) {
		this.forwardedPhoneNumber = forwardedPhoneNumber;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}	
}