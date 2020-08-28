import java.util.ArrayList;
import java.util.Arrays;

public abstract class testStuff {

	public static void main(String[] args) {
		int[] strut = new int[3];
		strut[0] = 3;
		strut[1] = 8;
		strut[2] = 3;
		ColorTrial C = new ColorTrial(100, 100, strut, 100);
		C.run();
		Network theBest = C.getTheBest(0);
		System.out.println(theBest.toStringDetail());
	}
}

