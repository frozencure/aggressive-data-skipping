package ovgu.aggressivedataskipping.featurization;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ovgu.aggressivedataskipping.featurization.models.QuerySet;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/featurization")
@Api(value = "Responsible with reading JSON representations of the workload and creating features")
public class FeaturizationController {

    @Autowired
    private FeaturizationService service;

    @GetMapping("/read")
    public void readQueries(@RequestParam String queriesPath, @RequestParam String outputPath) {
        try {
            QuerySet queries = service.augmentQueries(queriesPath);
            service.writeQueriesToFile(queries, outputPath);
        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Queries file not found");
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Output path does not exist");
        }
    }


}
