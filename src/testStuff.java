import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class testStuff {

	public static void main(String[] args) {
		ArrayList<ArrayList<Double>> Arr = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i < 10; i++) {
			ArrayList<Double> newStuff = new ArrayList<Double>();
			for (Double j = 0.0; j < 10; j++) {
				newStuff.add(j);
			}
			Arr.add(newStuff);
		}
		
		
		System.out.println(Arr.toString());
		
		
		Arr = Trial.flipDoubleArrayList(Arr);
		
		System.out.println(Arr.toString());
	}
}

