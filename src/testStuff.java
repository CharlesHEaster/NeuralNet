import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class testStuff {

	public static void main(String[] args) {
		
		int[] strut = {3,5,3};
		int firstGen = 11;
		String[][] ioLegend = {{"Stuff", "And", "other"},{"Stuff", "And", "other"}};
		Network N = new Network(strut, firstGen, ioLegend);
		String Nsave = N.toSave();
		System.out.println(Nsave);
		System.out.println("-------------------------------------------------------------");
		
		String[] strArr = Nsave.split(Network.marker());
		for (String a : strArr) {
			a = a.replace("\r\n",  "");
			System.out.println(a);
		}
		
		
		
		



		
	}
}

