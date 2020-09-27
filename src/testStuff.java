import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class testStuff {

	public static void main(String[] args) {
		double a = 0.3;
		double b = 0.1;
		double c = a - b;
		
		System.out.println(c);
		
		BigDecimal _a = new BigDecimal("0.03");
		BigDecimal _b = new BigDecimal("0.01");
		BigDecimal _c = _a.subtract(_b);
		
		System.out.println(_c);
	}
}

