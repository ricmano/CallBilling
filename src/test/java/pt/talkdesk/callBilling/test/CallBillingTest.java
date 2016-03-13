package pt.talkdesk.callBilling.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import pt.talkdesk.callBilling.Application;
import pt.talkdesk.callBilling.bean.CallDetail;
import pt.talkdesk.callBilling.bean.CallEvent;
import pt.talkdesk.callBilling.bean.CallType;
import pt.talkdesk.callBilling.controller.CallBillingController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class CallBillingTest {

	private static final Double TWILIO_COST = 0.01700;
	
	private static final Double DEFAULT_MARGIN = 0.05;
	
	private static final Double ACCOUNT_1_MARGIN = 0.03;
	
	private static final Double ACCOUNT_2_MARGIN = 0.02;
	
	private static final Double ACCOUNT_3_MARGIN = 0.01;

	// Path to ClientDetail file
	private static final String TEST_SCENARIOS_FILE_NAME = "TestScenarios.csv";
	
	@Autowired
	CallBillingController callBillingController;
	
    @Test
    public void defaultMarginTestWithoutForward() {
		CallDetail callDetail = createCallDetail("Default Account",
	    		"callId1", "+351210000000", 1,
	    		"+351210000000", null);
		
    	Double cost = callBillingController.calculateCallCost(callDetail);
		assertEquals(applyFormula(TWILIO_COST, null, DEFAULT_MARGIN, 1), cost);
    }

    @Test
    public void defaultMarginTestWithForward() {
		CallDetail callDetail = createCallDetail("Default Account",
	    		"callId1", "+351210000000", 1,
	    		"+351210000000", "+351210000000");
		
		Double cost = callBillingController.calculateCallCost(callDetail);
		assertEquals(applyFormula(TWILIO_COST, TWILIO_COST, DEFAULT_MARGIN, 1), cost);
    }
    
    @Test
    public void account1MarginTestWithoutForward() {
		CallDetail callDetail = createCallDetail("account-1",
	    		"callId1", "+351210000000", 1,
	    		"+351210000000", null);
		
    	Double cost = callBillingController.calculateCallCost(callDetail);
		assertEquals(applyFormula(TWILIO_COST, null, ACCOUNT_1_MARGIN, 1), cost);
    }

    @Test
    public void account1MarginTestWithForward() {
		CallDetail callDetail = createCallDetail("account-1",
	    		"callId1", "+351210000000", 1,
	    		"+351210000000", "+351210000000");
		
		Double cost = callBillingController.calculateCallCost(callDetail);
		assertEquals(applyFormula(TWILIO_COST, TWILIO_COST, ACCOUNT_1_MARGIN, 1), cost);
    }
    
    @Test
    public void account2MarginTestWithoutForward() {
		CallDetail callDetail = createCallDetail("account-2",
	    		"callId1", "+351210000000", 1,
	    		"+351210000000", null);
		
    	Double cost = callBillingController.calculateCallCost(callDetail);
		assertEquals(applyFormula(TWILIO_COST, null, ACCOUNT_2_MARGIN, 1), cost);
    }

    @Test
    public void account2MarginTestWithForward() {
		CallDetail callDetail = createCallDetail("account-2",
	    		"callId1", "+351210000000", 1,
	    		"+351210000000", "+351210000000");
		
		Double cost = callBillingController.calculateCallCost(callDetail);
		assertEquals(applyFormula(TWILIO_COST, TWILIO_COST, ACCOUNT_2_MARGIN, 1), cost);
    }
    
    @Test
    public void account3MarginTestWithoutForward() {
		CallDetail callDetail = createCallDetail("account-3",
	    		"callId1", "+351210000000", 1,
	    		"+351210000000", null);
		
    	Double cost = callBillingController.calculateCallCost(callDetail);
		assertEquals(applyFormula(TWILIO_COST, null, ACCOUNT_3_MARGIN, 1), cost);
    }

    @Test
    public void account3MarginTestWithForward() {
		CallDetail callDetail = createCallDetail("account-3",
	    		"callId1", "+351210000000", 1,
	    		"+351210000000", "+351210000000");
		
		Double cost = callBillingController.calculateCallCost(callDetail);
		assertEquals(applyFormula(TWILIO_COST, TWILIO_COST, ACCOUNT_3_MARGIN, 1), cost);
    }
    
    @Test
    public void runTestScenarios() throws IOException {
		InputStream testScenarioFile = ClassLoader.getSystemResourceAsStream(TEST_SCENARIOS_FILE_NAME);
		InputStreamReader testScenarioFileReader = new InputStreamReader(testScenarioFile);
		BufferedReader br = new BufferedReader(testScenarioFileReader);

		List<Double> expecteds = new ArrayList<>();
		List<Double> actuals = new ArrayList<>();
		//Reading header file - No relevant data to import
		String line = br.readLine();
		while ((line = br.readLine()) != null) {
			String[] splitedLine = line.split(",");

			CallDetail callDetail = createCallDetail(splitedLine[0], splitedLine[1],
					splitedLine[2], Integer.parseInt(splitedLine[3]),
					splitedLine[4], splitedLine[5]);

			expecteds.add(new Double(splitedLine[6]));
			actuals.add(callBillingController.calculateCallCost(callDetail));
		}

		assertArrayEquals(expecteds.toArray(new Double[0]), actuals.toArray(new Double[0]));
		br.close();
		testScenarioFileReader.close();
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
