import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class testStuff {

  public static void main(String[] args) {

	  InputNode I = new InputNode(10, 6.3);
	  for (int i = 0; i < 100; i++) {
		  I.setInput(Math.floor(Math.random() * 10000) / 100);
		  System.out.println(i);
		  System.out.println(I.getHist().toString());
		  System.out.println("Min " + I.getMin());
		  System.out.println("Max " + I.getMax());
		  System.out.println("-----------------");
	  }
	  
	  

  
}

}