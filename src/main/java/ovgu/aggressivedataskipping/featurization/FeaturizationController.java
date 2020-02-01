package ovgu.aggressivedataskipping.featurization;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ovgu.aggressivedataskipping.featurization.models.Feature;
import ovgu.aggressivedataskipping.featurization.models.FeatureSet;
import ovgu.aggressivedataskipping.featurization.models.QuerySet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/featurization")
@Api(value = "Responsible with reading JSON representations of the workload and creating features.\n" +
        "A feature is a representative set a of predicates that can help a lot of queries to skip data.\n" +
        "Such features will be used in the augmentation and clustering modules for the workload-driven partitioning.")
public class FeaturizationController {

    @Autowired
    private FeaturizationService service;

    @GetMapping("/create-features")
    @ApiOperation("This operation takes as input the queries JSON file created during the query-parsing process.\n" +
            "It will perform frequent-itemset mining operation and create representative features\n" +
            "The features will be saved in JSON format.")
    public void readQueries(@ApiParam("The path of the parsed-queries JSON file") @RequestParam String queriesPath,
                            @ApiParam("The path and name of output features file. Must be in JSON format.")
                            @RequestParam String outputPath,
                            @ApiParam ("The support value (minimum frequency) for the frequent-itemset mining algorithm.")
                                @RequestParam int support,
                            @ApiParam("A stop early limit for the frequent-itemset mining algorithm. This represents the maximum size" +
                                    "of the sets that will be generated")
                            @RequestParam int stopEarlyLimit,
                            @ApiParam("The maximum number of features to be created. When reached, the process will be stopped early.")
                            @RequestParam int maxNumberOfFeatures) {
        try {
            QuerySet queries = service.augmentQueries(queriesPath);
            FeatureSet featureSet = service.getFrequentItemSets(new HashSet<>(queries.getQueries()), support, stopEarlyLimit);
            RedundantPredicatesRemover remover = new RedundantPredicatesRemover(featureSet.getFeatures(),
                    new HashSet<>(queries.getQueries()), support, maxNumberOfFeatures);
            FeatureSet features = new FeatureSet(remover.removeRedundantFeatures());
            service.writeQueriesToFile(features, outputPath);
        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Queries file not found");
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Output path does not exist");
        }
    }
}
