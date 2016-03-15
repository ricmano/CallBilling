package pt.talkdesk.callBilling.bean;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ClientDetail implements Serializable {

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = -5547462413521873438L;

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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ClientDetail) {
			return this.accountId.equals(((ClientDetail) obj).getAccountId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.accountId.hashCode();
	}

	@Override
	public String toString() {
		return "Account Id: " + accountId + "\tAccount Name: " + accountName + "\tMargin: " + margin;
	}
}
