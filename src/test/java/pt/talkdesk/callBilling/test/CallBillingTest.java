package pt.talkdesk.callBilling.test;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import pt.talkdesk.callBilling.bean.CallDetail;
import pt.talkdesk.callBilling.bean.CallEvent;
import pt.talkdesk.callBilling.bean.CallType;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CallBillingTest.class)
@WebAppConfiguration
public class CallBillingTest {

	private RestTemplate restTemplate;

	@Before
	public void setup() {
		this.restTemplate = new TestRestTemplate();
	}
   
    @Test
    public void testWithoutForward() {
		CallDetail callDetail = createCallDetail("AccountId1",
	    		"callId1", "+351210000000", 1,
	    		"+351210000000", null);
		
		ResponseEntity<Double> result = restTemplate.postForEntity("/billing", callDetail, Double.class);
    	Double cost = result.getBody();
		assertEquals(applyFormula(0.01700, null, 0.05, 1), cost);
    }

    @Test
    public void testWithForward() {
		CallDetail callDetail = createCallDetail("AccountId1",
	    		"callId1", "+351210000000", 1,
	    		"+351210000000", "+351210000000");
		
		ResponseEntity<Double> result = restTemplate.postForEntity("http://localhost:8080/billing", callDetail, Double.class);
		Double cost = result.getBody();
		assertEquals(applyFormula(0.01700, 0.01700, 0.05, 1), cost);
    }
    
    private CallDetail createCallDetail(String accountId,
    		String callId, String customerPhoneNumber,
    		Integer duration, String talkdeskPhoneNumber,
    		String forwardedPhoneNumber) {
		CallDetail callDetail = new CallDetail();

		callDetail.setEvent(CallEvent.call_finished);
		callDetail.setTimestamp(new Date());
		callDetail.setType(CallType.in);

		callDetail.setAccountId(accountId);
		callDetail.setCallId(callId);
		callDetail.setCustomerPhoneNumber(customerPhoneNumber);
		callDetail.setDuration(duration);
		callDetail.setTalkdeskPhoneNumber(talkdeskPhoneNumber);
		callDetail.setForwardedPhoneNumber(forwardedPhoneNumber);
		
		return callDetail;
    }

    private Double applyFormula(Double talkDeskNumberCost, Double externalNumberCost, Double profitMargin, Integer duration) {
    	Double externalCost = 0.0;
    	if (externalNumberCost != null) {
    		externalCost = externalNumberCost * duration;
    	}
    	return (talkDeskNumberCost * duration) + externalCost + profitMargin;
    }
}
