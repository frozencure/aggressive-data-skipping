package ovgu.aggressivedataskipping;

import org.apache.livy.LivyClient;
import org.apache.livy.LivyClientBuilder;
import ovgu.aggressivedataskipping.test.PiJob;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Main {

    private static final String classPath = "ovgu/aggressivedataskipping/test";
    private static final String jarPath = "./temp/PiJob.jar";
    private static final String livyUrl = "http://127.0.0.1:8998";

    public static void main(String...args) throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        LivyClient client = new LivyClientBuilder()
                .setURI(new URI(livyUrl))
                .build();
        try {
            JarCreator jarCrerater = new JarCreator(classPath, jarPath);
            jarCrerater.createJar();
            System.err.printf("Uploading %s to the Spark context...\n", jarPath);
            client.uploadJar(new File(jarPath)).get();
            int input = -1;
            Scanner keyboard = new Scanner(System.in);
            while (input != 0) {
                System.out.println("Enter 0 to exit, other integers to run");
                input = keyboard.nextInt();
                System.err.printf("Running PiJob with %d samples...\n", input);
                double pi = client.submit(new PiJob(input)).get();

                System.out.println("Pi is roughly: " + pi);
            }
        } finally {
            client.stop(true);
        }
    }

}
