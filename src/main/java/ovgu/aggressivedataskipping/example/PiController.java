package ovgu.aggressivedataskipping.example;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/pi")
@Api(value = "Pi estimation example")
public class PiController {

    final PiService piService;

    public PiController(PiService piService) {
        this.piService = piService;
    }

    @ApiOperation(value = "Estimate the value of Pi using a set of random samples", response = Double.class)
    @GetMapping("/estimate")
    public double add(@ApiParam(value = "The number of samples", required = true) @RequestParam(value = "samples") int samples) throws ExecutionException, InterruptedException {
        return piService.estimatePi(samples);
    }
}
