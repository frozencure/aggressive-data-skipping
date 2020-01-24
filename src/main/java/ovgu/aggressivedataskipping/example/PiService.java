package ovgu.aggressivedataskipping.example;

import org.apache.livy.LivyClient;
import org.springframework.stereotype.Service;
import ovgu.aggressivedataskipping.livy.LivyClientWrapper;

import java.util.concurrent.ExecutionException;

@Service
public class PiService {

    final LivyClientWrapper client;

    public PiService(LivyClientWrapper client) {
        this.client = client;
    }

    public double estimatePi(int samples) throws ExecutionException, InterruptedException {
        return client.getLivyClient().submit(new PiJob(samples)).get();
    }

}
