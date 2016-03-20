package pt.talkdesk.callBilling;

import java.math.BigDecimal;

public interface CallBillingConstants {

	// Default Client Id to retrieve default margin
	final String DEFAULT_CLIENT_ID = "*";
	
	// Web Browser cost per minute
	final BigDecimal WEB_BROWSER_COST_PER_MINUTE = new BigDecimal(0.01);

	// Default TalkDesk cost per minute
	final BigDecimal TALK_DESK_DEFAULT_COST_PER_MINUTE = new BigDecimal(0.01);

	// US tool free cost per minute
	final BigDecimal TALK_DESK_US_TOOL_FREE_COST_PER_MINUTE = new BigDecimal(0.03);

	// UK tool free cost per minute
	final BigDecimal TALK_DESK_UK_TOOL_FREE_COST_PER_MINUTE = new BigDecimal(0.06);
}
