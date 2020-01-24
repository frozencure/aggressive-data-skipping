package ovgu.aggressivedataskipping.augmentation;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/augmentation")
public class AugmentationController {

    final AugmentationService augmentationService;

    public AugmentationController(AugmentationService augmentationService) {
        this.augmentationService = augmentationService;
    }

    @RequestMapping("/test")
    public long augmentationTest() throws ExecutionException, InterruptedException {
        return augmentationService.testAugmentation();
    }


}
