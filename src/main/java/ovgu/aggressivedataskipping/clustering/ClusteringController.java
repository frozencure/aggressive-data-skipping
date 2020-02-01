package ovgu.aggressivedataskipping.clustering;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.checkerframework.checker.units.qual.A;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/clustering")
@Api("This controller contains operations that can apply HAC using a vector-augmented table.\n" +
        "After doing the clustering the table will partitioned according to the block vectors created.")
public class ClusteringController {

    final ClusteringService clusteringService;

    public ClusteringController(ClusteringService clusteringService) {
        this.clusteringService = clusteringService;
    }

    @GetMapping("/partition-table")
    @ApiOperation("This operation performs HAC given a vector-augmented table and creates a new, partitioned copy of the table")
    public void clusterAndPartition(@ApiParam("Path to the features JSON file obtained after the featurization process")
                                    @RequestParam(value = "featuresPath") String featuresPath,
                                    @ApiParam("The name of the database where the to-be partitioned table is located")
                                    @RequestParam(value = "database") String databaseName,
                                    @ApiParam("The name of the to-be partitioned table")
                                    @RequestParam(value = "table") String tableName,
                                    @ApiParam("The column where the feature vectors are stored")
                                    @RequestParam(value = "column") String columnName,
                                    @ApiParam("The maximum size for a partition. Will be used for stopping early in HAC.")
                                    @RequestParam(value = "maxPartitionSize") int maximumPartitionSize,
                                    @ApiParam("The name of the new, partitioned table")
                                    @RequestParam(value = "new table name") String newTableName,
                                    @ApiParam("The name of the blocking vector column inside the paritioned table")
                                    @RequestParam(value = "block vector column") String blockVectorColumn) {
        try {
            clusteringService.partitionTable(featuresPath, databaseName,
                    tableName, columnName, maximumPartitionSize, newTableName, blockVectorColumn);
        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Features file path does not exist.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occured when comunicating with Livy");
        }
    }


}
