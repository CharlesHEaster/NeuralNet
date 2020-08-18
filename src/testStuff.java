import java.util.ArrayList;

public abstract class testStuff {

	public static void main(String[] args) {

		int numNetworks = 5;
		int numCycles = 10;
		int[] netStructure = {3, 3, 3};
		RandColor[] trialInputs = new RandColor[100];
		for (int i = 0; i < trialInputs.length; i++) {
			trialInputs[i] = new RandColor();
		}
		Trial colorTrial = new Trial(numNetworks, numCycles, netStructure, trialInputs);
		ArrayList<Network> before = colorTrial.getNetworks();
		colorTrial.runCycle();
		ArrayList<Network> after = colorTrial.getNetworks();
		String str = "";
		for (int i = 0; i < after.size(); i++) {
			str += before.get(i).toString() + "-----" + after.get(i).toString() + "\n";
		}
		System.out.println(str);
		
		
		
	}
}

