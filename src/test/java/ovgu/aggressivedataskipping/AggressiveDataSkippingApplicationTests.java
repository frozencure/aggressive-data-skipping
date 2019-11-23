package ovgu.aggressivedataskipping;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AggressiveDataSkippingApplicationTests {

    @Autowired
    private CollectionCalculatorService service;

    @Test
    void contextLoads() {
    }

    @Test
    void calculatorAddAllTest() {
        int result = service.addAll(Arrays.asList(30, 3, 2));
        assertEquals(result, 35, "Addition failed");
    }


	@Test
	void calculatorSubstractAllTest() {
		int result = service.substractAll(Arrays.asList(30, 3, 2));
		assertEquals(result, 25, "Substraction failed");
	}


	@Test
	void calculatorMultiplyAllTest() {
		int result = service.multiplyAll(Arrays.asList(30, 3, 2));
		assertEquals(result, 180, "Multiplication failed");
	}


	@Test
	void calculatorDivideAllTest() {
		int result = service.divideAll(Arrays.asList(30, 3, 2));
		assertEquals(result, 5, "Division failed");
	}

}
