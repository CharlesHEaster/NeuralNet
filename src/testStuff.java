import java.util.ArrayList;

public abstract class testStuff {

	public static void main(String[] args) {

		ArrayList<Integer> I = new ArrayList<Integer>();
		for (Integer i = 0; i < 100; i++) {
			I.add(i);
		}
		ArrayList<Integer> J = new ArrayList<Integer>();
		J.addAll(I.subList(0, 100));
		System.out.println(J);
		
		
		String[] B = {"b1", "b2", "b3"};
		ArrayList<String> BB = new ArrayList<String>();
		int numStrings = 100;
		for(int i = 0; i < numStrings; i++) {
			while(i < numStrings * .08) {
				BB.add(B[0]);
				i++;
			}
			for(int j = 1; j < B.length; j++) {
				while(i < (numStrings * .08) + (j * (numStrings * .04))) {
					BB.add(B[j]);
					i++;
				}
			}
			BB.add("b4");
			
			
			
		}
		for(int i = 1; i <= BB.size(); i++) {
			System.out.println(i + " " + BB.get(i - 1));
		}
		
	}
}

