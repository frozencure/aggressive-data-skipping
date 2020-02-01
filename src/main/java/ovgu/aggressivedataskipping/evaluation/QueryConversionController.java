package ovgu.aggressivedataskipping.evaluation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/convert")
@Api("Controller that can be used to convert normal sql-queries to augmented queries, " +
        "that can be executed on a SOP partitioned table")
public class QueryConversionController {

    final QueryConversionService service;

    public QueryConversionController(QueryConversionService service) {
        this.service = service;
    }


    @GetMapping("/augmented")
    @ApiOperation("Converts a set of given queries to SOP executable queries by adding a new filter to their where conditions.\n" +
            "The new filter will look at the block vector column and skip the non-relevant partitions.\n" +
            "Takes as input a folder of SQL SELECT scripts and outputs the new queries to a given folder.")
    public void createAugmentedQueries(@ApiParam("The name of the table that queries are supposed to target")
                                       @RequestParam(value = "table") String tableName,
                                       @ApiParam("The column of the blocking vectors inside the SOP partitioned table")
                                       @RequestParam(value = "column") String columnName,
                                       @ApiParam("The path to the features JSON file created during the featurization process")
                                       @RequestParam(value = "features file") String featuresPath,
                                       @ApiParam("The path to the folder where nomral SQL SELECT scripts are located. " +
                                               "Queries to be converted.")
                                       @RequestParam(value = "queries to convert") String oldQueriesPath,
                                       @ApiParam("The path of the folder where the modified queries will be saved.")
                                       @RequestParam(value = "output path") String newQueriesPath) {
        try {
            service.convertQueries(tableName, columnName, featuresPath, oldQueriesPath, newQueriesPath);
        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One of the given file paths was not found");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "A communication errow with Livy occured");
        }
    }
}
