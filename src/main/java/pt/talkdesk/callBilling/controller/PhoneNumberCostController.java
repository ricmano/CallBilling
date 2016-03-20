package pt.talkdesk.callBilling.controller;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;

import pt.talkdesk.callBilling.utils.Trie;
import pt.talkdesk.callBilling.utils.TrieNode;

@Controller
public class PhoneNumberCostController implements IPhoneNumberCostController {

	private Trie root;
	
	private static PhoneNumberCostController instance = null;
	
	private PhoneNumberCostController() {
		root = new Trie();
	}

	public static PhoneNumberCostController getInstance() {
		if (instance == null) {
			instance = new PhoneNumberCostController();
		}
		return instance;
	}
	
	@Override
	public BigDecimal getCostPerMinute(String phoneNumber) {
		TrieNode node = root.searchNode(phoneNumber);
		if (node != null) {
			return node.getCost();
		}
		return null;
	}

}
