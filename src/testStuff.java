import java.util.ArrayList;

public abstract class testStuff {

	public static void main(String[] args) {

		ArrayList<Integer> theBest = new ArrayList<Integer>();
		ArrayList<Integer> networks = new ArrayList<Integer>();

		int numNetworks = 100;
				int top1 = (int)(numNetworks * 0.01);
		int top10 = (int)(numNetworks * 0.10);
		int top20 = (int)(numNetworks * 0.20);
		
		while (theBest.size() < top20) {
			theBest.add(5);
		}

		for (int i = 0; i < theBest.size(); i++) { // roll through those top 20%
			if (i < top1) {								
				for (int j = 0; j < 10; j++) {			// top 1% get 10 children
					networks.add(1);
				}
			} else if (i < top10) {
				for (int j = 0; j < 5; j++) {			// top 1% - 10% get 5 children
					networks.add(10);
				}
			} else {									// top 10% - 20% get 2 children
				networks.add(20);
				networks.add(20);
			}			
		}
		while (networks.size() < numNetworks) {	//fill in the rest of the networks with random 1stGen.  
			networks.add(100);
		}
		
		for (int i = 0; i < networks.size(); i++) {
			System.out.println(i + " is " + networks.get(i));
			
		}
		
		
	}
}

