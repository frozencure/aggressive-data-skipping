package ovgu.aggressivedataskipping.clustering;

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
public class ClusteringController {

    final ClusteringService clusteringService;

    public ClusteringController(ClusteringService clusteringService) {
        this.clusteringService = clusteringService;
    }

    @GetMapping("/partition-table")
    public void clusteringTest(@RequestParam(value = "featuresPath") String featuresPath,
                                 @RequestParam(value = "database") String databaseName,
                                 @RequestParam(value = "table") String tableName,
                                 @RequestParam(value = "column") String columnName,
                                 @RequestParam(value = "maxPartitionSize") int maximumPartitionSize) {
        try {
            clusteringService.partitionTable(featuresPath, databaseName, tableName, columnName, maximumPartitionSize);
        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Features file path does not exist.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occured when comunicating with Livy");
        }
    }


}
