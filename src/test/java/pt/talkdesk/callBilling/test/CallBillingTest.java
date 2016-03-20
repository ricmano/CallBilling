package pt.talkdesk.callBilling.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import pt.talkdesk.callBilling.Application;
import pt.talkdesk.callBilling.CallBillingConstants;
import pt.talkdesk.callBilling.bean.CallDetail;
import pt.talkdesk.callBilling.bean.CallEvent;
import pt.talkdesk.callBilling.bean.CallType;
import pt.talkdesk.callBilling.controller.CallBillingController;
import pt.talkdesk.callBilling.controller.RestCallBillingController;
import pt.talkdesk.callBilling.utils.Trie;
import pt.talkdesk.callBilling.utils.TrieNode;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class CallBillingTest {

	// Path to ClientDetail file
	private static final String TEST_SCENARIOS_FILE_NAME = "TestScenarios.csv";

	private static final Logger LOGGER = LoggerFactory.getLogger(CallBillingTest.class);

	@Autowired
	CallBillingController callBillingController;

	@Autowired
	RestCallBillingController restCallBillingController;

	/**
	 * Run several test according to excel file TEST_SCENARIOS_FILE_NAME
	 * This will create a CallDetail Object and test it's cost
	 * @throws IOException
	 */
	@Test
	public void runTestScenarios() throws IOException {
		InputStream testScenarioFile = ClassLoader.getSystemResourceAsStream(TEST_SCENARIOS_FILE_NAME);
		InputStreamReader testScenarioFileReader = new InputStreamReader(testScenarioFile);
		BufferedReader br = new BufferedReader(testScenarioFileReader);

		int failedResults = 0;
		// Reading header file - No relevant data to import
		String line = br.readLine();
		while ((line = br.readLine()) != null) {
			String[] splitedLine = line.split(",");

			CallDetail callDetail = createCallDetail(splitedLine[0], splitedLine[1], splitedLine[2],
					Integer.parseInt(splitedLine[3]), splitedLine[4], splitedLine[5]);

			BigDecimal expected = new BigDecimal(splitedLine[6]);
			BigDecimal actual = restCallBillingController.calculateCallCost(callDetail);
			if (expected.doubleValue() != actual.doubleValue()) {
				failedResults++;
				LOGGER.error("Expected (" + expected.toString() + ") != Actual(" + actual.toString() + ") for call \t"
						+ callDetail.toString());
			}
		}

		assertEquals("More than one result failed, check log", 0, failedResults);
		br.close();
		testScenarioFileReader.close();
	}

	@Test
	public void billingFormulaTest() {
		assertEquals(new BigDecimal(1*(0.02*0.01*0.03)).setScale(5, RoundingMode.HALF_UP),
				callBillingController.billingFormula(new BigDecimal(0.02),
						new BigDecimal(0.01),
						new BigDecimal(0.03),
						40));

		assertEquals(new BigDecimal(3*(0.12*0.05*0.01)).setScale(5, RoundingMode.HALF_UP),
				callBillingController.billingFormula(new BigDecimal(0.12),
						new BigDecimal(0.05),
						new BigDecimal(0.01),
						140));

		assertEquals(new BigDecimal(2*(0.2*0.11*0.02)).setScale(5, RoundingMode.HALF_UP),
				callBillingController.billingFormula(new BigDecimal(0.2),
						new BigDecimal(0.11),
						new BigDecimal(0.02),
						100));

		assertEquals(new BigDecimal(5*(0.17*0.07*0.02)).setScale(5, RoundingMode.HALF_UP),
				callBillingController.billingFormula(new BigDecimal(0.17),
						new BigDecimal(0.07),
						new BigDecimal(0.02),
						300));
	}
	
	@Test
	public void clientMarginTest() {
		assertEquals(new BigDecimal(0.05), callBillingController.getClientMargin("*"));
		assertEquals(new BigDecimal(0.05), callBillingController.getClientMargin("Not Existing account"));
		assertEquals(new BigDecimal(0.03), callBillingController.getClientMargin("account-1"));
		assertEquals(new BigDecimal(0.02), callBillingController.getClientMargin("account-2"));
		assertEquals(new BigDecimal(0.01), callBillingController.getClientMargin("account-3"));
	}

	@Test
	public void talkDeskNumberCostTest() {
		//Default TalskDesk Cost
		assertEquals(CallBillingConstants.TALK_DESK_DEFAULT_COST_PER_MINUTE, callBillingController.getTalkDeskNumberCost("+123456789"));
		//US Tool Free Cost
		assertEquals(CallBillingConstants.TALK_DESK_US_TOOL_FREE_COST_PER_MINUTE, callBillingController.getTalkDeskNumberCost("+123456789"));
		//UK Tool Free Cost
		assertEquals(CallBillingConstants.TALK_DESK_UK_TOOL_FREE_COST_PER_MINUTE, callBillingController.getTalkDeskNumberCost("+123456789"));
	}

	@Test
	public void externalNumberCostTest() {
		// Web Browser
		assertEquals(CallBillingConstants.WEB_BROWSER_COST_PER_MINUTE, callBillingController.getExternalNumberCost(null));
		/**
		 * Get cost information from twilio
		 */
		// Afghanistan
		assertEquals(new BigDecimal(0.29000), callBillingController.getExternalNumberCost("+9312346565"));

		// Bhutan
		assertEquals(new BigDecimal(0.13260), callBillingController.getExternalNumberCost("+97598731987"));
		// French Polynesia
		assertEquals(new BigDecimal(0.37500), callBillingController.getExternalNumberCost("+68954513546"));
		// Gambia
		assertEquals(new BigDecimal(0.64500), callBillingController.getExternalNumberCost("+22098798742"));
	}	

	@Test
	public void searchCallsByAccountTest() {
		
		CallDetail firstCallDetail = createCallDetail("test-account1", "test-callId-1", "+654354687",
				40, "+779442467", null);
		CallDetail secondCallDetail = createCallDetail("test-account2", "test-callId-2", "+654354687",
				40, "+779442467", null);
		CallDetail thirdCallDetail = createCallDetail("test-account2", "test-callId-3", "+654354687",
				40, "+779442467", null);
		
		restCallBillingController.calculateCallCost(firstCallDetail);
		restCallBillingController.calculateCallCost(secondCallDetail);
		restCallBillingController.calculateCallCost(thirdCallDetail);
		
		List<CallDetail> firstClient = restCallBillingController.getCallsByAccount(firstCallDetail.getAccountId());
		List<CallDetail> secondClient = restCallBillingController.getCallsByAccount(secondCallDetail.getAccountId());
		List<CallDetail> emptyClient = restCallBillingController.getCallsByAccount("test-empty-account");
		
		assertEquals(1, firstClient.size());
		assertEquals(2, secondClient.size());
		assertEquals(0, emptyClient.size());
	}

	@Test
	public void searchCallsByIdTest() {
		CallDetail firstCallDetail = createCallDetail("test-account3", "test-callId-4", "+654354687",
				40, "+779442467", null);
		CallDetail secondCallDetail = createCallDetail("test-account4", "test-callId-5", "+654354687",
				40, "+779442467", null);
		CallDetail thirdCallDetail = createCallDetail("test-account4", "test-callId-5", "+654354687",
				40, "+779442467", null);
		thirdCallDetail.setEvent(CallEvent.call_voicemail);
		
		restCallBillingController.calculateCallCost(firstCallDetail);
		restCallBillingController.calculateCallCost(secondCallDetail);
		restCallBillingController.calculateCallCost(thirdCallDetail);
		
		List<CallDetail> firstCall = restCallBillingController.getCallsById(firstCallDetail.getCallId());
		List<CallDetail> secondCall = restCallBillingController.getCallsById(secondCallDetail.getCallId());
		List<CallDetail> emptyCall = restCallBillingController.getCallsById("test-callId-6");
		
		assertEquals(1, firstCall.size());
		assertEquals(2, secondCall.size());
		assertEquals(0, emptyCall.size());
	}

	@Test
	public void trieTest() {
		Trie root = new Trie();
		BigDecimal firstCost = new BigDecimal(0.01);
		BigDecimal secondCost = new BigDecimal(0.02);
		
		root.insert("+321", firstCost);
		root.insert("+32156", secondCost);
		
		TrieNode emptyCostNode = root.searchNode("+31111312");
		assertNull(emptyCostNode.getCost());
		
		TrieNode firstCostNode = root.searchNode("+321167788");
		assertEquals(firstCost, firstCostNode.getCost());
		
		TrieNode secondCostNode = root.searchNode("+321567788");
		assertEquals(secondCost, secondCostNode.getCost());
	}


	/**
	 * Method used to create a Call Detail Object
	 * @param accountId
	 * @param callId
	 * @param customerPhoneNumber
	 * @param duration
	 * @param talkdeskPhoneNumber
	 * @param forwardedPhoneNumber
	 * @return
	 */
	private CallDetail createCallDetail(String accountId, String callId, String customerPhoneNumber, Integer duration,
			String talkdeskPhoneNumber, String forwardedPhoneNumber) {
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

}
