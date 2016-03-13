package pt.talkdesk.callBilling;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import pt.talkdesk.callBilling.initialization.ClientDetailImporter;
import pt.talkdesk.callBilling.initialization.VoicePriceImporter;

@SpringBootApplication
public class Application implements CommandLineRunner {
	@Autowired
	ClientDetailImporter clientDetailImporter;

    public static void main(String[] args) throws IOException {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

    @Override
    public void run(String... args) throws IOException {
        VoicePriceImporter vpi = new VoicePriceImporter();
        vpi.loadPrices();

        clientDetailImporter.loadClientInformation();
    }
}
