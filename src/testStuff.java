import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class testStuff {

  public static void main(String[] args) {
	  int[] netStrut = new int[]{3, 9, 9, 3} ;
	  int[] inputHistoryStrut = new int[] {10, 20, 30, 50, 60};
	  
	  
	  
	  
	  ColorTrial C = new ColorTrial(100, 100, netStrut, 100, 3, inputHistoryStrut);

	  
	 
	 C.run();
	 
	 for (int i = 0; i < 100; i++) {
		 System.out.println(((InputNode) C.getNetwork(0).getNode(0, i)).getHist().toString());
	 }
	  
	  
	  
	
	  }
}

