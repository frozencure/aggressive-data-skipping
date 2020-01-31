package ovgu.aggressivedataskipping.evaluation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/convert")
public class QueryConversionController {

    final QueryConversionService service;

    public QueryConversionController(QueryConversionService service) {
        this.service = service;
    }


    @GetMapping("/augmented")
    public void createAugmentedQueries(@RequestParam(value = "table") String tableName,
                                       @RequestParam(value = "column") String columnName,
                                       @RequestParam(value = "features file") String featuresPath,
                                       @RequestParam(value = "queries to convert") String oldQueriesPath,
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
