package ovgu.aggressivedataskipping.example;

import java.util.*;
import org.apache.livy.*;

public class PiJob implements Job<Double> {

    private final int samples;

    public PiJob(int samples) {
        this.samples = samples;
    }

    @Override
    public Double call(JobContext ctx) throws Exception {
        List<Integer> sampleList = new ArrayList<Integer>();
        for (int i = 0; i < samples; i++) {
            sampleList.add(i + 1);
        }
        return 4.0d * ctx.sc().parallelize(sampleList).map(this::randomInt).reduce(Integer::sum) / samples;
    }

    private Integer randomInt(Integer v1) {
        double x = Math.random();
        double y = Math.random();
        return (x*x + y*y < 1) ? 1 : 0;
    }
}
