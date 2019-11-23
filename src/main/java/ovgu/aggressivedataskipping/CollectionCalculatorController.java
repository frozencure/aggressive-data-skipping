package ovgu.aggressivedataskipping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calculator")
public class CollectionCalculatorController {

    @Autowired
    CollectionCalculatorService service;

    @RequestMapping("/add")
    public Integer add(@RequestParam(value = "n") List<Integer> numbers) {
        return service.addAll(numbers);
    }

    @RequestMapping("/substract")
    public Integer substract(@RequestParam(value = "n") List<Integer> numbers) {
        return service.substractAll(numbers);
    }

    @RequestMapping("/multiply")
    public Integer multiply(@RequestParam(value = "n") List<Integer> numbers) {
        return service.multiplyAll(numbers);
    }

    @RequestMapping("/divide")
    public Integer divide(@RequestParam(value = "n") List<Integer> numbers) {
        return service.divideAll(numbers);
    }


}
