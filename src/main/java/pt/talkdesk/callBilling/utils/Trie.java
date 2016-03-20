package pt.talkdesk.callBilling.utils;

import java.math.BigDecimal;

/**
 * Implementation of a trie to store cost/prefix number information
 * @author Ricardo
 *
 */
public class Trie {

	private TrieNode root;

	public Trie() {
		root = new TrieNode();
	}

	/**
	 * Method to insert a prefix into the trie
	 * @param phoneNumberPrefix
	 * @param cost (per minute)
	 */
	public void insert(String phoneNumberPrefix, BigDecimal cost) {
		String phoneNumberPrefixToInsert = phoneNumberPrefix.replace("+", "");
		TrieNode[] children = root.getChildren();
		for (int i = 0; i < phoneNumberPrefixToInsert.length(); i++) {
			int number = new Integer(phoneNumberPrefixToInsert.charAt(i));

			TrieNode node;
			if (children[number] != null) {
				node = children[number];
			} else {
				node = new TrieNode();
				children[number] = node;
			}

			children = node.getChildren();

			if (i == phoneNumberPrefixToInsert.length() - 1) {
				node.setCost(cost);
			}
		}
	}

	public TrieNode searchNode(String str) {
		str = str.toUpperCase();
		TrieNode[] children = root.getChildren();
		TrieNode node = null;
		for (int i = 0; i < str.length(); i++) {
			int number = new Integer(str.charAt(i));
			if (children[number] != null) {
				node = children[number];
				children = node.getChildren();
			} else {
				return node;
			}
		}

		return node;
	}
}
