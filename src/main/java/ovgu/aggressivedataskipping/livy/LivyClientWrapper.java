package ovgu.aggressivedataskipping.livy;

import org.apache.livy.LivyClient;

import javax.annotation.PreDestroy;

public class LivyClientWrapper {

    private LivyClient livyClient;

    public LivyClientWrapper(LivyClient livyClient) {
        this.livyClient = livyClient;
    }

    public LivyClient getLivyClient() {
        return livyClient;
    }

    @PreDestroy
    public void onExit() {
        livyClient.stop(true);
    }
}
