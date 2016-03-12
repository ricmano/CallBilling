package pt.talkdesk.callBilling.bean;

public enum CallType {
	in("in"), out("out");
	
	private final String type;

	CallType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}
}
