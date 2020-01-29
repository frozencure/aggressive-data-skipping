package ovgu.aggressivedataskipping.parsing;

import java.io.IOException;

public class ParsingMain {

    public static void main(String...args) throws IOException {
        QueryParser parser = new QueryParser("C:\\Users\\iverg\\Downloads\\test",
                "more-queries.json", "lineitem");
        parser.exportPredicatesToJson();
    }

}
