package pt.talkdesk.callBilling.initialization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VoicePriceImporter {
	// Path to Twilio prices file
	private static final String TWILIO_FILE_NAME = "Twilio - Voice Prices.csv";

	// String Pattern to retrieve price information of each line 
	private static final String STRING_PATTERN = "(.*),([\\d\\.]*),(.*)";

	// Pattern to use
	private static final Pattern PATTERN = Pattern.compile(STRING_PATTERN);
	
	public void loadPrices() throws IOException {
		InputStream voicePriceFile = ClassLoader.getSystemResourceAsStream(TWILIO_FILE_NAME);
		InputStreamReader voicePriceFileReader = new InputStreamReader(voicePriceFile);
		BufferedReader br = new BufferedReader(voicePriceFileReader);
		//Reading header file - No relevant data to import
		String line = br.readLine();
		while ((line = br.readLine()) != null) {
			Matcher m = PATTERN.matcher(line);
			if (m.matches()) {
				String country = m.group(1);
				String rate = m.group(2);
				String[] splitedPreffixes = m.group(3).split(",");
				for (int i = 0 ; i < splitedPreffixes.length; i++) {
					String prefix = splitedPreffixes[i].replaceFirst("\"", "").trim();
//					System.out.println("Country: " + country + "\tPrefix: " + prefix + "\tRate: " + rate);
				}
			} else {
				System.err.println("Problem parsing line: " + line);
			}
		}
		br.close();
		voicePriceFileReader.close();
	}

}
