import java.util.ArrayList;

public abstract class testStuff {

	public static void main(String[] args) {
		int[] structure = {3,5,7,4,2};
		Network N = new Network(structure);
		System.out.println(N.toString());
		Network K = new Network(structure);
		System.out.println(K.toString());
		Network P = N.morph();
		System.out.println(P.toString());
		
		
	}
}
