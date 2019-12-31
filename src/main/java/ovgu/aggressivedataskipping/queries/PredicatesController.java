package ovgu.aggressivedataskipping.queries;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/queries")
public class PredicatesController {

    @RequestMapping("/parse")
    public String parseQueries(@RequestParam String input, @RequestParam String output, @RequestParam String table) {
        QueryParser parser = new QueryParser(input, output, table);
        return parser.exportPredicatesToJson();
    }

}
