import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class testStuff {

	public static void main(String[] args) {
		
//		int[] struct = new int[5];
//		struct[0] = 3;
//		struct[1] = 5;
//		struct[2] = 5;
//		struct[3] = 5;
//		struct[3] = 3;
//		ColorTrial T = new ColorTrial(100, 100, struct, 100);
//		T.run();
		
		
		ColorTrial CT = ColorTrial.load("C:/Users/Arch1986gmail.com/eclipse-workspace/NeuralNet/ColorTrialResults/_Working.txt");
		CT.run();
		

		
	}
}

