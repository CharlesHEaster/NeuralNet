import java.util.ArrayList;

public class Trial {
//	boolean fillTheMorgue$ = true;  //would you like to keep the data of the failed networks?
//	if (fillTheMorgue$){
//	   Arraylist<Network> theMorgue
//	};
	ArrayList<Network> networks;
	int cycles;
	int[] structure;
	double[][] inputs;
	ArrayList<Network> theBest;

	//constructors
	public Trial(int numNetworks, int numCycles, int[] netStructure, double [][] inputs){
		//setup
		for (int i = 0; i < numNetworks; i++) {
			networks.add(new Network(netStructure));
		}
	}

	//Set = A set of inputs
	//Cycle = all sets run once, then compare and cull
	//Trial = all cycles
					

//	public void runSet(){
//		// run each set
//		for (int setNum = 0; setNum < inputs.length; setNum++) {
//			for (int i = 0; i < networks.size(); i++){
//				networks.get(i).run(inputs[setNum]);
//
//				//this part is specific to color trial.   Maybe make trial abstract class of some sort...
//				if (color.getAnswer(setNum).isEqual(networks[i].getOutput())){
//					networks[i].incScore();
//				}
//				network[i].resetOutputs();
//			}										
//		}
//	}
//	public void runCycle(){
//	---// run each cycle
//	For (int cycleNum = 0, cycleNum < cycles, cycleNum++) {
//	   this.runSet();
//	   this.compare();
//	   this.cullAndCreate();
//	}
//	}
//
//	public void compare(){
//	this.networks.addAll(this.theBest);
//	Collections.sort(this.networks, Collections.reverseOrder());
//	}
//
//	public void cullAndCreate(){
//	  this.theBest.clear();
//	this.theBest.addAll(this.networks.subList(0, (this.networks.size() * .1) + 1));
//	      //grab the best 10%
//	if (fillTheMorgue$){
//	this.theMorgue.addAll(this.networks.subList((this.networks.size() * .1) + 1, this.networks.size()))
//	}
//	this.networks.clear();
//	for (int i = 0, i < this.theBest.size(), i++){
//	   if(i < this.theBest.size() * .2){
//	      // double children for the top 20% of the top 10%
//	      for (int j = 0, j < 10, j++){
//	         this.networks.add(
//	         this.theBest[i].morph());
//	      }
//	   } else {
//	      for (int j = 0, j < 5, j++){
//	         this.networks.add(
//	         this.theBest[i].morph());
//	      }
//	   }
//	   while(this.networks.size() < numNetworks){
//	      this.networks.add(new Network(this.structure));
//
//	   }
//	}
//	}
//
//	public void run(){
//	   for (int i = 0, i < this.cycles, i++) {
//	      this.runCycle()
//
//	} 

}
