package ovgu.aggressivedataskipping.featurization;

import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public class FeaturizationService {

    public String readQueries(String queriesFile) throws FileNotFoundException {
        WorkloadReader reader = new WorkloadReader(queriesFile);
        return reader.readQueries();
    }

}
