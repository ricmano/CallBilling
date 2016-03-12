package pt.talkdesk.callBilling.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.talkdesk.callBilling.bean.CallDetail;

@RestController
public class CallBillingController {

	@RequestMapping(value = "/billing/{callDetail}")
	public Double calculateCallCost(@PathVariable CallDetail callDetail) {
		return null;
	}

	@RequestMapping(value = "/test")
	public String helloWorld() {
		return "Hello World";
	}
}
