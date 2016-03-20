package pt.talkdesk.callBilling.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class should be used to store a Call Detail
 * 
 * @author Ricardo
 *
 */
@IdClass(CallDetailId.class)
@Entity
public class CallDetail implements Serializable {
	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 5833731185209512577L;

	@Id
	@JsonProperty("call_id")
	private String callId;

	@Id
	private CallEvent event;

	private CallType type;

	private Integer duration;

	@JsonProperty("account_id")
	private String accountId;

	@JsonProperty("talkdesk_phone_number")
	private String talkdeskPhoneNumber;

	@JsonProperty("customer_phone_number")
	private String customerPhoneNumber;

	@Column(nullable = true)
	@JsonProperty("forwarded_phone_number")
//	@JsonProperty(required=false)
	private String forwardedPhoneNumber = null;

	private Date timestamp;

	private BigDecimal cost;

	public CallEvent getEvent() {
		return event;
	}

	public void setEvent(CallEvent event) {
		this.event = event;
	}

	public CallType getType() {
		return type;
	}

	public void setType(CallType type) {
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

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CallDetail) {
			CallDetail callDetail = (CallDetail) obj;
			return this.callId.equals(callDetail.getCallId()) && this.event.equals(callDetail.getEvent());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.callId.hashCode() + this.event.hashCode();
	}

	@Override
	public String toString() {
		return "Call Id: " + callId + "\tEvent: " + event + "\tType: " + type + "\tDuration: " + duration
				+ "\tAccount Id: " + accountId + "\tTalkdesk PhoneNumber: " + talkdeskPhoneNumber
				+ "\tCustomer PhoneNumber: " + customerPhoneNumber + "\tForwarded PhoneNumber: " + forwardedPhoneNumber
				+ "\tTimestamp: " + timestamp;
	}
}
