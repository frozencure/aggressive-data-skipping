package ovgu.aggressivedataskipping.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/pi")
public class PiController {

    final PiService piService;

    public PiController(PiService piService) {
        this.piService = piService;
    }

    @RequestMapping("/estimate")
    public double add(@RequestParam(value = "samples") int samples) throws ExecutionException, InterruptedException {
        return piService.estimatePi(samples);
    }
}
