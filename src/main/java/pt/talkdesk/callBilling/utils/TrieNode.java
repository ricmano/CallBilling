package pt.talkdesk.callBilling.utils;

import java.math.BigDecimal;

public class TrieNode {

	private BigDecimal cost;
	
	private TrieNode[] children;

	public TrieNode() {
		children = new TrieNode[10];
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public TrieNode[] getChildren() {
		return children;
	}
}
