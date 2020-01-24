package ovgu.aggressivedataskipping.augmentation;

import org.apache.livy.LivyClient;
import org.springframework.stereotype.Service;
import ovgu.aggressivedataskipping.livy.LivyClientWrapper;

import java.util.concurrent.ExecutionException;

@Service
public class AugmentationService {

    final LivyClientWrapper client;

    public AugmentationService(LivyClientWrapper client) {
        this.client = client;
    }

    public long testAugmentation() throws ExecutionException, InterruptedException {
        return client.getLivyClient().submit(new AugmentationJob()).get();
    }

}
