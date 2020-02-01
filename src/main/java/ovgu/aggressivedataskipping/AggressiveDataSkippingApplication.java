package ovgu.aggressivedataskipping;

import org.apache.livy.LivyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/***
 * Main application to start the REST server
 */
@SpringBootApplication
public class AggressiveDataSkippingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AggressiveDataSkippingApplication.class, args);
    }

}
