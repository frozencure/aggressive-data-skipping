package ovgu.aggressivedataskipping;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionCalculatorService {

    @Autowired
    private JavaSparkContext sparkContext;

    public CollectionCalculatorService(JavaSparkContext sparkContext) {
        this.sparkContext = sparkContext;
    }

    public Integer addAll(List<Integer> numbers) {
        JavaRDD<Integer> rdd = sparkContext.parallelize(numbers);
        return rdd.reduce(Integer::sum);
    }

    public Integer substractAll(List<Integer> numbers) {
        JavaRDD<Integer> rdd = sparkContext.parallelize(numbers);
        return rdd.reduce((a,b) -> a - b);
    }

    public Integer multiplyAll(List<Integer> numbers) {
        JavaRDD<Integer> rdd = sparkContext.parallelize(numbers);
        return rdd.reduce((a,b) -> a * b);
    }

    public Integer divideAll(List<Integer> numbers) {
        JavaRDD<Integer> rdd = sparkContext.parallelize(numbers);
        return rdd.reduce((a,b) -> a/b);
    }

}
