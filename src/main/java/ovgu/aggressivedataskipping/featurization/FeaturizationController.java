package ovgu.aggressivedataskipping.featurization;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/featurization")
@Api(value = "Responsible with reading JSON representations of the workload and creating features")
public class FeaturizationController {

    @Autowired
    private FeaturizationService service;

    @GetMapping("/read")
    public String readQueries(@RequestParam String queriesPath) {
        try {
            return service.readQueries(queriesPath);
        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Queries file not found");
        }
    }


}
