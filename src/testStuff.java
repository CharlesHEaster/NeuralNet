import java.util.ArrayList;

public abstract class testStuff {

	public static void main(String[] args) {
		int[] structure = new int[3];
		structure[0] = 3;
		structure[1] = 3;
		structure[2] = 3;
		Network N = new Network(structure);
		System.out.println(N.toString());
		
	}
}
