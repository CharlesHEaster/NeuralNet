import java.util.ArrayList;

public class otherTest {

	public static void main(String[] args) {
//		String str = Trial.load("C:\\Users\\Arch1986gmail.com\\eclipse-workspace\\NeuralNet\\ColorTrialResults\\_Working.txt");
//		System.out.println(str);

		
		String str = "";
		for (int i = 0; i < 10; i++) {
			str += "[0.0";
			for (Double j = 1.0; j < 10; j++) {
				str += ", " + j;
			}
			str += "]";
		}
		System.out.println(str);

		
		ArrayList<ArrayList<Double>> D = Trial.unstringArrayList2d(str);
		System.out.println(D.toString());
		String string = D.toString();
		System.out.println(string);
		ArrayList<ArrayList<Double>> Dou = Trial.unstringArrayList2d(string);
		System.out.println(Dou.toString());
		
		
	}

}
