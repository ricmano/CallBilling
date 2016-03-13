package pt.talkdesk.callBilling.initialization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.talkdesk.callBilling.bean.ClientDetail;
import pt.talkdesk.callBilling.repository.ClientRepository;

@Service
public class ClientDetailImporter {
	// Path to ClientDetail file
	private static final String CLIENT_DETAILS_FILE_NAME = "ClientDetails.csv";

	@Autowired
	ClientRepository clientRepository;
	
	public void loadClientInformation() throws IOException {
		InputStream clientDetailFile = ClassLoader.getSystemResourceAsStream(CLIENT_DETAILS_FILE_NAME);
		InputStreamReader clientDetailFileReader = new InputStreamReader(clientDetailFile);
		BufferedReader br = new BufferedReader(clientDetailFileReader);

		List<ClientDetail> clientList = new ArrayList<>();
		//Reading header file - No relevant data to import
		String line = br.readLine();
		while ((line = br.readLine()) != null) {
			String[] splitedLine = line.split(",");

			ClientDetail client = new ClientDetail();
			client.setAccountId(splitedLine[0]);
			client.setAccountName(splitedLine[1]);
			client.setMargin(Double.parseDouble(splitedLine[2]));
			
			clientList.add(client);
		}
		clientRepository.save(clientList);
		br.close();
		clientDetailFileReader.close();
	}

}
