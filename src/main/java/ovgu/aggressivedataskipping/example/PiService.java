package ovgu.aggressivedataskipping.example;

import org.apache.livy.LivyClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class PiService {

    final LivyClient client;

    public PiService(LivyClient client) {
        this.client = client;
    }

    public double estimatePi(int samples) throws ExecutionException, InterruptedException {
        return client.submit(new PiJob(samples)).get();
    }

}
