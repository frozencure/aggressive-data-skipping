package ovgu.aggressivedataskipping.augmentation;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/augmentation")
@Api("Controller responsible with reading features and augmenting feature vectors to an existing table.")
public class AugmentationController {

    final AugmentationService augmentationService;

    public AugmentationController(AugmentationService augmentationService) {
        this.augmentationService = augmentationService;
    }

    @GetMapping("/augment-table")
    @ApiOperation("Creates a of the given table with an augmented column which contains the feature vectors.\n" +
            "Takes as input the features JSON file that was created during he featurization process")
    public long augmentTable(@ApiParam("The path and name of the features file.")
                             @RequestParam(value = "featuresPath") String featuresPath,
                             @ApiParam("The database where the input table is located")
                             @RequestParam(value = "database") String databaseName,
                             @ApiParam("The name of the new, agumented table")
                             @RequestParam(value = "new table") String newTableName,
                             @ApiParam("The name of the to-be-augmented table")
                             @RequestParam(value = "old table") String oldTableName,
                             @ApiParam("The name of the new feature vector column")
                             @RequestParam(value = "vector column name") String vectorColumn,
                             @ApiParam("The index of the first feature, whith which the process will be started")
                             @RequestParam(value = "First feature id") int firstFeatureId,
                             @ApiParam("The number of features that will be augmented")
                             @RequestParam(value = "Batch size") int batchSize,
                             @ApiParam("A boolean that specifies whether the old table already contains a feature vector column. " +
                                     "When true, it will create the column from scratch. Otherwise it will take the values from the old one.")
                             @RequestParam(value = "From old table") boolean isFromOld) throws ExecutionException,
            InterruptedException, FileNotFoundException {
        return augmentationService.augmentVectors(featuresPath, databaseName, oldTableName, newTableName, vectorColumn,
                firstFeatureId, batchSize, isFromOld);
    }


}
