import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class testStuff {

  public static void main(String[] args) {
	  int[] netStrut = new int[5];
	  netStrut[0] = 3;
	  for (int i = 1; i < 4; i++) {
		  netStrut[i] = i * 3;
	  }
	  netStrut[4] = 3;
	  System.out.println(netStrut.toString());
	  int[] histStrut = new int[3];
	  histStrut[0] = 10;
	  histStrut[1] = 20;
	  histStrut[2] = 50;
	  ColorTrial C = new ColorTrial(100, 10, netStrut, 100);
	  System.out.println(C.getNetwork(0).toString());
	  
	  C.run();
	  }
}

