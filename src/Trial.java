import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public abstract class Trial {


	private ArrayList<Network> networks, theBest, theMorgue;
	private int cycles, numNetworks;
	private int[] structure;
	private Double[][] TrialInputs;
	private boolean fillTheMorgue$;
	private String[] InputLegend;
	private long Start, Elapsed;
	private string workingMorgueFile = "";
	private abstract string dir;

	public void setMorgue(boolean fill$) {
		this.fillTheMorgue$ = fill$;
	}
	
	public boolean getMorgue() {
		return this.fillTheMorgue$;
	}
	
	public int getNumNetworks() {
		return this.numNetworks;
	}
	
	public int getNumCycles() {
		return this.cycles;
	}
	
	public ArrayList<Network> getNetworks(){
		return this.networks;
	}
	
	public int[] getStructure() {
		return this.structure;
	}

	public void setTrialInputs(Double[][] trialInputs) {
		this.TrialInputs = trialInputs;
	}
	
	public Double[][] getTrialInputs(){
		return this.TrialInputs;
	}
	
	public void setInputLegend(String[] inputLegend) {
		this.InputLegend = inputLegend;
	}
	
	public String[] getInputLegend() {
		return this.InputLegend;
	}
	
	public ArrayList<Network> getTheBest(){
		return this.theBest;
	}
	
	public Network getTheBest(int i){
		return this.theBest.get(i);
	}
	
	public long getElapsed() {
		return this.Elapsed;
	}
	
	public Double[] getInputSet(int i){
		return this.TrialInputs[i];
	} 
	
	public String stringTrialInputs() {
		String str = "";
		System.out.println(this.getTrialInputs().length);
		for (int i = 0; i < this.getTrialInputs().length; i++) {
			str += Arrays.toString(this.getInputSet(i));
		}
		return str;
	}
	
	
	//constructors
	public Trial(int numNetworks, int numCycles, int[] netStructure, Double[][] trialInputs, String[] inputLegend){
		//setup
		this.networks = new ArrayList<Network>();
		this.numNetworks = numNetworks;
		for (int i = 0; i < numNetworks; i++) {
			this.networks.add(new Network(netStructure));
		}
		this.theBest = new ArrayList<Network>();
		this.cycles = numCycles;
		this.structure = netStructure;
		this.TrialInputs = trialInputs;
		this.fillTheMorgue$ = false;
		this.InputLegend = inputLegend;
	}

	//Set = A set of inputs
	//Cycle = all sets run once, then compare and cull
	//Trial = all cycles


	public void run() {
		this.Start = System.nanoTime();
		for(int i = 0; i < this.cycles; i++) {
			System.out.print("Cycle: " + i + "/" + this.cycles + " :: ");
			this.runCycle();
		}
		this.Elapsed = System.nanoTime() - this.Start;
		printBestToFile(this.getTheBest().size());
		if (this.fillTheMorgue$){
			Trial.closeTheMorgue();
		}
	}
	
	public void runCycle() {
		for (int setNum = 0; setNum < TrialInputs.length; setNum++) {
			this.runSet(TrialInputs[setNum]);			
		}
		this.compare();
		this.cullAndCreate();
	}
	
	public void runSet(Double[] inputs){
		// run a set of inputs
		for (int i = 0; i < this.networks.size(); i++){
			this.networks.get(i).resetOutputs();			
			this.networks.get(i).run(inputs);
			this.evaluateAndUpdate(networks.get(i), inputs);
		}
	}										

	public void compare(){
		this.networks.addAll(this.theBest);
		Collections.sort(this.networks, Collections.reverseOrder());
	}

	public void cullAndCreate() {
		this.theBest.clear();
		int top1 = (int)(this.numNetworks * 0.01);
		int top10 = (int)(this.numNetworks * 0.10);
		int top20 = (int)(this.numNetworks * 0.20);
		this.theBest.addAll(this.networks.subList(0,  top20)); // grab the top 20% and put them into 'theBest'
		if (this.fillTheMorgue$) {
			Trial.sendToTheMorgue(this.networks.subList(top20,  this.networks.size() - 1));
		}
		this.networks.clear();
		for (int i = 0; i < this.theBest.size(); i++) { // roll through those top 20%
			if (i < top1) {	
				for (int j = 0; j < 10; j++) {			// top 0% - 1% get 10 children
					this.networks.add(this.theBest.get(i).morph());
				}
			} else if (i < top10) {
				for (int j = 0; j < 5; j++) {			// top 1% - 10% get 5 children
					this.networks.add(this.theBest.get(i).morph());
				}
			} else {									// top 10% - 20% get 2 children
				this.networks.add(this.theBest.get(i).morph());
				this.networks.add(this.theBest.get(i).morph());
			}			
		}
		while (this.networks.size() < numNetworks) {	//fill in the rest of the networks with random 1stGen.  
			this.networks.add(new Network(this.structure));
		}
		System.out.print(" Score: " + this.theBest.get(0).getScore() + "\r\n");
	}
	
	public static void sendToTheMorgue(ArrayList<Network> bodies){
		if (! this.dir.exists()){
			this.dir.mkdir();
			Final workingMorgueFile = "WorkingMorgue.txt";
			File file = new File(this.dir + "/" + workingMorgueFile);
		} else {
			if (workingMorgueFile == ""){
				int i = 1;
				while ((this.dir + "/" + "WorkingMorgue" + i + ".txt").exists) {
					i++;
				}
				Final workingMorgueFile = "WorkingMorgue" + i + ".txt";
				File file = new File(this.dir + "/" + workingMorgueFile);
			}
		try{
		        FileWriter fw = new FileWriter(file.getAbsoluteFile());
		        BufferedWriter bw = new BufferedWriter(fw);
		        bw.write(bodies.toString());
		        bw.close();
		    }
		    catch (IOException e){
		        e.printStackTrace();
		        System.exit(-1);
		    }
	
	}
		
	public static void closeTheMorgue(){
		// add trial info to the beginning of the file
		// change theMorgue file name to match trialResults file name
	}
	
	 public static String dateAndTime() {
		  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd--HH-mm-ss");  
		  LocalDateTime now = LocalDateTime.now();  

		  return dtf.format(now);
	  }
	 
	 public static String convertNanoTime(long time) {
		 String str = "";
		 int minutes = 0, hours = 0, days = 0;
		 int seconds = (int) (time / 1000000000);
		 if (seconds > 60) {
			 minutes = (int) Math.floor(seconds / 60);
			 seconds = seconds % 60;
		 }
		 if (minutes > 60) {
			 hours = minutes / 60;
			 minutes = minutes % 60;
		 }
		 if (hours > 24) {
			 days = hours / 24;
			 hours = hours % 24;
			 str = days + "d ";
		 }
		 if (hours > 0) {
			 str += hours + "h ";
		 }
		 if (minutes > 0) {
			 str += minutes + "m ";
		 }
		 str += seconds + "s";
		 
		 return str;
	 }

	 public static void writeFile(String value){
		    String directoryName = this.dir;
		    String fileName = Trial.dateAndTime() + ".txt";

		    File directory = new File(directoryName);
		    if (! directory.exists()){
		        directory.mkdir();
		    }

		    File file = new File(directoryName + "/" + fileName);
		    try{
		        FileWriter fw = new FileWriter(file.getAbsoluteFile());
		        BufferedWriter bw = new BufferedWriter(fw);
		        bw.write(value);
		        bw.close();
		    }
		    catch (IOException e){
		        e.printStackTrace();
		        System.exit(-1);
		    }
	  }
	
	//This method takes in a network and it's outputs.  then evaluates those outputs against the answer and updates the network
	public abstract void evaluateAndUpdate(Network net, Double[] SetOfInputs);
	
	
	//This method is the final evaluation.  If score updates after each set, then it can just put in evaluateAndUpdate().
	//  else it can take the state variables and compute a score
	public abstract void finalEvaluateAndScore(Network net, Double[] SetOfInputs);
	
	public abstract void printBestToFile(int numBest);
	//EXAMPLE
//	{
//		numBest = numBest > this.getNumNetworks() * 0.20 ? (int) (this.getNumNetworks() * 0.20) : numBest;
//  String contents = "Color Trial, completed: " + Trial.dateAndTime() + "    Networks Trained for: " + Trial.convertNanoTime(this.getElapsed()) + "\r\n";
//		contents += "   # of Networks : " + this.getNumNetworks() + "\r\n";
//		contents += "     # of Cycles : " + this.getNumCycles() + "\r\n";
//		contents += "Network Structure: " + Arrays.toString(this.getStructure()) + "\r\n";
//		if (this.getMorgue()) {
//			contents += "Dead Networks: KEPT\r\n";
//		} else {
//			contents += "Dead Networks: DISCARDED\r\n";
//		}
//		contents += "Input Legend\r\n";
//		contents += Arrays.toString(this.getInputLegend()) + "\r\n\r\n";
//		contents += "Inputs\r\n";
//		contents += this.stringTrialInputs() + "\r\n\r\n";
//		
//		contents += "THE BEST NETWORKS\r\n";
//		for (int i = 0; i < numBest; i++) {
//			contents += this.getTheBest(i).toString();
//		}
	
}
	



