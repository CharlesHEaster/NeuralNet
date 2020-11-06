import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class testStuff {

	public static void main(String[] args) {
		String str = "node{Weights [0.6566697751216966, -0.12559634343861809, -0.18144955666785506]}";
		Node N = Node.load(str);
		System.out.println(N.toString());
		



		
	}
}

