import java.util.ArrayList;
import java.util.Arrays;

public abstract class testStuff {

	public static void main(String[] args) {
		int[] strut = new int[5];
		strut[0] = 3;
		strut[1] = 5;
		strut[2] = 7;
		strut[3] = 5;
		strut[4] = 3;
		Network N = new Network(strut);
		System.out.println(N.toString());
		System.out.println(N.toString());
//		ColorTrial C = new ColorTrial(1000, 100, strut, 1000);
//		C.run();
//		Network theBest = C.getTheBest(0);
//		System.out.println(theBest.toString());
	}
}

