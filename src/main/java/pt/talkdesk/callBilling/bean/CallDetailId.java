package pt.talkdesk.callBilling.bean;

import java.io.Serializable;

public class CallDetailId implements Serializable {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 62497485383039221L;

	private String callId;

	private CallEvent event;

	public CallDetailId() {
	}

	public CallDetailId(String callId, CallEvent event) {
		this.callId = callId;
		this.event = event;
	}

	public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public CallEvent getEvent() {
		return event;
	}

	public void setEvent(CallEvent event) {
		this.event = event;
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
}
