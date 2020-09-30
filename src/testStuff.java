import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class testStuff {

	public static void main(String[] args) {
		int[] strut = new int[2];
		strut[0] = 3;

		strut[1] = 11;

		ColorTrial C = new ColorTrial(200, 200, strut, 100);
		
		C.run();
	}
}

