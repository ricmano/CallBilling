package pt.talkdesk.callBilling.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ClientDetail {

	@Id
	private String accountId;
	
	private Double margin;
	
	private String accountName;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Double getMargin() {
		return margin;
	}

	public void setMargin(Double margin) {
		this.margin = margin;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
}
