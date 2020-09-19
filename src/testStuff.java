import java.io.FileWriter;
import java.io.IOException;

public class testStuff {

	public static void main(String[] args) {
		int[] netStrut = new int[]{3, 9, 9, 3} ;
		int[] inputHistoryStrut = new int[] {10, 20, 30, 50, 60};




		ColorTrial C = new ColorTrial(100, 5, netStrut, 1000, 3, inputHistoryStrut);
		C.setMorgue(true);

		C.run();
		C.compileTheMorgue();
		

	}
}

