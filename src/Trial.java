import java.util.ArrayList;
import java.util.Collections;

public class Trial {


	ArrayList<Network> networks, theBest, theMorgue;
	int cycles;
	int[] structure;
	double[][] inputs;
	boolean fillTheMorgue$;
	
	public void setMorgue(boolean fill$) {
		this.fillTheMorgue$ = fill$;
	}

	//constructors
	public Trial(int numNetworks, int numCycles, int[] netStructure, double[][] inputs){
		//setup
		for (int i = 0; i < numNetworks; i++) {
			networks.add(new Network(netStructure));
		}
	}

	//Set = A set of inputs
	//Cycle = all sets run once, then compare and cull
	//Trial = all cycles


	public void runSet(double[] inputs){
		// run a set of inputs
		for (int i = 0; i < this.networks.size(); i++){
			this.networks.get(i).resetOutputs();
			this.networks.get(i).run(inputs);
		}
	}										


	public void runCycle() {
		for (int setNum = 0; setNum < inputs.length; setNum++) {
			this.runSet(inputs[setNum]);			
		}
		//something to score each network
		this.compare();
//		this.cullAndCreate();

	}

	public void compare(){
		this.networks.addAll(this.theBest);
		Collections.sort(this.networks, Collections.reverseOrder()); // reverse order because high score first
	}


//	public void cullAndCreate(){
//		this.theBest.clear();
//		this.theBest.addAll(this.networks.subList(0, (int) (this.networks.size() * .1))); //grab the best 10%
//		if (fillTheMorgue$){
//			this.theMorgue.addAll(this.networks.subList((int) (this.networks.size() * .1), this.networks.size()));
//		}
//		this.networks.clear();
//		for (int i = 0, i < this.theBest.size(), i++){
//			if(i < this.theBest.size() * .2){
//				// double children for the top 20% of the top 10%
//				for (int j = 0, j < 10, j++){
//					this.networks.add(
//							this.theBest[i].morph());
//				}
//			} else {
//				for (int j = 0, j < 5, j++){
//					this.networks.add(
//							this.theBest[i].morph());
//				}
//			}
//			while(this.networks.size() < numNetworks){
//				this.networks.add(new Network(this.structure));
//
//			}
//		}
//	}
	//
	//	public void run(){
	//	   for (int i = 0, i < this.cycles, i++) {
	//	      this.runCycle()
	//
	//	} 

}
