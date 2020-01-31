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

    @GetMapping("/augment-table")
    public long augmentTable(@RequestParam(value = "featuresPath") String featuresPath,
                             @RequestParam(value = "database") String databaseName,
                             @RequestParam(value = "new table") String newTableName,
                             @RequestParam(value = "old table") String oldTableName,
                             @RequestParam(value = "vector column name") String vectorColumn,
                             @RequestParam(value = "First feature id") int firstFeatureId,
                             @RequestParam(value = "Batch size") int batchSize,
                             @RequestParam(value = "From old table") boolean isFromOld) throws ExecutionException,
            InterruptedException, FileNotFoundException {
        return augmentationService.augmentVectors(featuresPath, databaseName, oldTableName, newTableName, vectorColumn,
                firstFeatureId, batchSize, isFromOld);
    }


}
