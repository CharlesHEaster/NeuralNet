import java.util.ArrayList;

public abstract class testStuff {

	public static void main(String[] args) {
		int[] structure = {3,3,3};
		Network N = new Network(structure);
		System.out.println(N.toStringDetail());
		
		Network O = N.morph();
		System.out.println(O.toStringDetail());
		
		Network P = O.morph();
		System.out.println(P.toStringDetail());
		
		Network Q = N.morph();
		System.out.println(Q.toStringDetail());
		
		Network R = P.morph();
		System.out.println(R.toStringDetail());
		
	}
}
