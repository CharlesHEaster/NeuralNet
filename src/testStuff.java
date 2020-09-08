import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class testStuff {

  public static void main(String[] args) {
	  int[] netStrut = new int[4];
	  netStrut[0] = 3;
	  for (int i = 1; i < 3; i++) {
		  netStrut[i] = i * 3;
	  }
	  netStrut[3] = 3;
	  int[] histStrut = new int[3];
	  histStrut[0] = 10;
	  histStrut[1] = 20;
	  histStrut[2] = 50;
	  ColorTrial C = new ColorTrial(100, 10, netStrut, 100, 3, 3, histStrut);
	  Network N = C.getNetwork(0);
	  InputNode inputNode = (InputNode) N.getNode(0, 3);
	  Node n = N.getNode(1, 1);
	  OutputNode outputNode = (OutputNode) N.getNode(3, 2);
	  C.run();
	  System.out.println(inputNode.getHistCapasity());
	  System.out.println(inputNode.getHist().toString());
	  System.out.println(inputNode.getInputIdentifier());
	  System.out.println(inputNode.toString());
	  System.out.println(n.toStringDetail());
	  System.out.println(n.getWeights());
	  System.out.println(outputNode.getBias());
	  System.out.println(outputNode.toStringDetail());
	  outputNode.setBias(18);
	  System.out.println(outputNode.getInputs().toString());
		  
	  
	  }
}

