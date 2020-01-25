package ovgu.aggressivedataskipping.augmentation;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/augmentation")
public class AugmentationController {

    final AugmentationService augmentationService;

    public AugmentationController(AugmentationService augmentationService) {
        this.augmentationService = augmentationService;
    }

    @GetMapping("/test")
    public long augmentationTest(@RequestParam(value = "featuresPath") String featuresPath) throws ExecutionException,
            InterruptedException, FileNotFoundException {
        return augmentationService.testAugmentation(featuresPath);
    }


}
