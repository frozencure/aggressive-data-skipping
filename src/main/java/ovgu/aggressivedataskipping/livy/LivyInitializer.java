package ovgu.aggressivedataskipping.livy;

import org.apache.livy.LivyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.File;
import java.util.concurrent.ExecutionException;

@Service
public class LivyInitializer {

    private static final Logger LOGGER= LoggerFactory.getLogger(LivyInitializer.class);

    private final JarCreator jarCreator;

    private final LivyClientWrapper livyClient;

    public LivyInitializer(JarCreator jarCreator, LivyClientWrapper livyClient) {
        this.jarCreator = jarCreator;
        this.livyClient = livyClient;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void uploadJars() throws ExecutionException, InterruptedException {
        jarCreator.createJar();
        LOGGER.info("Uploading %s to the Spark context...\n", jarCreator.getJarPath());
        livyClient.getLivyClient().uploadJar(new File(jarCreator.getJarPath())).get();
    }
}
