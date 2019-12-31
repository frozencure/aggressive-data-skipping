package ovgu.aggressivedataskipping.queries;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/queries")
@Api(value = "Responsible with parsing SQL query scripts to JSON predicates")
public class PredicatesController {

    @GetMapping("/parse")
    @ApiOperation(value = "Parse multiple SQL SELECT scripts given the path to the folder and a table restriction",
            response = String.class)
    public String parseQueries(@ApiParam(value = "Path to the SQL scripts folder") @RequestParam String input,
                               @ApiParam(value = "Path and name of the output file") @RequestParam String output,
                               @ApiParam(value = "The table restriction for the queries") @RequestParam String table) {
        QueryParser parser = new QueryParser(input, output, table);
        return parser.exportPredicatesToJson();
    }

}
