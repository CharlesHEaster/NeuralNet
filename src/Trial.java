import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public abstract class Trial {


	private ArrayList<Network> networks, theBest;
	private int cycles, numNetworks, numDead, numHistoryInputs;
	private int[] netStructure, inputHistoryStructure;
	private ArrayList<ArrayList<Double>> TrialInputs;
	private boolean fillTheMorgue$;
	private String[] InputLegend;
	private long Start, Elapsed;
	private String workingMorgueFile = "";
	private String dir, FileName;
	private Double evolveRate, learnRate;

	//constructors
	public Trial(int numNetworks, int numCycles, int[] netStructure, ArrayList<ArrayList<Double>> trialInputs, String[] inputLegend){
		this.InputLegend = inputLegend;
		this.TrialInputs = trialInputs;
		this.networks = new ArrayList<Network>();
		this.numNetworks = numNetworks;
		this.netStructure = netStructure;
		this.theBest = new ArrayList<Network>();
		this.cycles = numCycles;
		this.fillTheMorgue$ = false;
		this.evolveRate = 0.3;
		this.learnRate = 0.2;
	}

	public Trial(int numNetworks, int numCycles, int[] netStructure, ArrayList<ArrayList<Double>> trialInputs, String[] inputLegend, int numHistoryInputs, int[] inputHistoryStrut){
		this(numNetworks, numCycles, netStructure, trialInputs, inputLegend);
		this.numHistoryInputs = numHistoryInputs;
		this.inputHistoryStructure = inputHistoryStrut;
		this.TrialInputs = new ArrayList<ArrayList<Double>>();
		this.expandInputs();
		this.networks = new ArrayList<Network>();
		
	}

	public void setEvolveRate(Double evolveRate) {
		this.evolveRate = evolveRate;
		this.evolveRate = this.evolveRate > 1 ? 1 : this.evolveRate;
		this.evolveRate = this.evolveRate < 0 ? 0 : this.evolveRate;
	}

	public void createNetworks() {
		if (this.numHistoryInputs == 0) {
			for (int i = 0; i < numNetworks; i++) {
				this.networks.add(new Network(this.netStructure));
			}		
		} else {
			for (int i = 0; i < numNetworks; i++) {
				this.networks.add(new Network(this.netStructure, this.numHistoryInputs, this.inputHistoryStructure));
			}
		}
	}
	
	public Double getEvolveRate() {
		return this.evolveRate;
	}
	
	public void setLearnRate(Double learnRate) {
		this.learnRate = learnRate;
		this.learnRate = this.learnRate > 2 ? 2 : this.learnRate;
		this.learnRate = this.learnRate < 0 ? 0 : this.learnRate;
	}

	public Double getLearnRate() {
		return this.learnRate;
	}
	
	public int[] getHistStructure() {
		return this.inputHistoryStructure;
	}
	
	public void setDir(String directory) {
		this.dir = directory;
	}

	public String getDir() {
		return this.dir;
	}

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
		return this.netStructure;
	}

	public void setTrialInputs(ArrayList<ArrayList<Double>> trialInputs) {
		this.TrialInputs = trialInputs;
	}

	public ArrayList<ArrayList<Double>> getTrialInputs(){
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

	public ArrayList<Double> getInputSet(int i){
		return this.TrialInputs.get(i);
	} 

	public String stringTrialInputs() {
		String str = "";
		for (int i = 0; i < this.getTrialInputs().size(); i++) {
			str += this.getInputSet(i).toString();
		}
		return str;
	}

	public int[] getInputHistoryStructure(){
		return this.inputHistoryStructure;
	}

	public int getNumHistoryInputs(){
		return this.numHistoryInputs;
	}

	public Network getNetwork(int i) {
		return this.networks.get(i);
	}

	
	//Set = A set of inputs
	//Cycle = all sets run once, then compare and cull
	//Trial = all cycles


	public void run() {
		
		this.checkInputsExpanded();
		this.createNetworks();
		this.Start = System.nanoTime();
		for(int i = 0; i < this.cycles; i++) {
			System.out.print("Cycle: " + i + "/" + this.cycles + " :: ");
			this.runCycle();
		}
		this.Elapsed = System.nanoTime() - this.Start;
		System.out.println("--Compiling and Publishing Results--");
		//this.printTrialResults();
		System.out.println(":::::::::Training Program Complete:::::::::");
	}

	public void runCycle() {
		for (int setNum = 0; setNum < TrialInputs.size(); setNum++) {
			this.runSet(TrialInputs.get(setNum));			
		}
		this.compare();
		this.cullAndCreate();
	}

	public void runSet(ArrayList<Double> inputs){
		// run a set of inputs
		for (int i = 0; i < this.networks.size(); i++){
			this.networks.get(i).resetOutputs();			
			this.networks.get(i).run(inputs);
			this.evaluateAndUpdate(networks.get(i), inputs);
		}
	}										

	public void checkInputsExpanded() {
		if (this.numHistoryInputs > 0 && 
				this.TrialInputs.get(0).get(0) != this.TrialInputs.get(0).get(1)) {
			this.expandInputs();
			
		}
	}
	
	public void expandInputs() {
		ArrayList<ArrayList<Double>> trialInputs = new ArrayList<ArrayList<Double>>(this.TrialInputs);
		this.TrialInputs.clear();
		for (int i = 0; i < trialInputs.size(); i++){ //for each set of inputs
			ArrayList<Double> oldInputs = trialInputs.get(i);
			ArrayList<Double> expandedInputs = new ArrayList<Double>();
			for (int j = 0; j < trialInputs.get(i).size(); j++) { //for each input in a set
				if (j < this.numHistoryInputs) {
					for (int k = 0; k < this.inputHistoryStructure.length; k++)
						expandedInputs.add(oldInputs.get(j));
				}
				if (j >= this.numHistoryInputs) {
					expandedInputs.add(oldInputs.get(j));
				}
			}
			this.TrialInputs.add(expandedInputs);		
		}
	}
	
	public void compare(){
		this.networks.addAll(this.theBest);
		Collections.sort(this.networks, Collections.reverseOrder());
	}

	public void cullAndCreate() {
		this.theBest.clear();
		int top1 = (int)(Math.floor(this.numNetworks * 0.01));
		int top10 = (int)(Math.floor(this.numNetworks * 0.10));
		int top20 = (int)(Math.floor(this.numNetworks * 0.20));
		this.theBest.addAll(this.networks.subList(0,  top20)); // grab the top 20% and put them into 'theBest'
		if (this.fillTheMorgue$) {
			ArrayList<Network> morgue = new ArrayList<Network>(this.networks.subList(top20,  this.networks.size() - 1));
			this.sendToTheMorgue(morgue);
			this.numDead += morgue.size();
		}
		this.networks.clear();
		for (int i = 0; i < this.theBest.size(); i++) { // roll through those top 20%
			if (i < top1) {	
				for (int j = 0; j < 10; j++) {			// top 0% - 1% get 10 children
					this.networks.add(this.theBest.get(i).morph(this.evolveRate, this.learnRate));
				}
			} else if (i < top10) {
				for (int j = 0; j < 5; j++) {			// top 1% - 10% get 5 children
					this.networks.add(this.theBest.get(i).morph(this.evolveRate, this.learnRate));
				}
			} else {									// top 10% - 20% get 2 children
				this.networks.add(this.theBest.get(i).morph(this.evolveRate, this.learnRate));
				this.networks.add(this.theBest.get(i).morph(this.evolveRate, this.learnRate));
			}			
		}
		while (this.networks.size() < numNetworks) {	//fill in the rest of the networks with random 1stGen. 
			//TODO make a networks.add for historyInputs
			this.networks.add(new Network(this.netStructure));
		}
		System.out.print(" Score: " + this.theBest.get(0).getScore() + "\r\n");
	}

	public void sendToTheMorgue(ArrayList<Network> bodies){
		File directory = new File(this.getDir());
		File file = new File("");
		if (! directory.exists()){
			directory.mkdir();
			this.workingMorgueFile = "WorkingMorgue.txt";
			file = new File(directory + "/" + this.workingMorgueFile);
		} else {
			if (this.workingMorgueFile == ""){
				this.workingMorgueFile = "WorkingMorgue.txt";
				file = new File(directory + "/" + this.workingMorgueFile);
				for (int i = 2; file.exists(); i++) {
					this.workingMorgueFile = "WorkingMorgue" + i + ".txt";
					file = new File(directory + "/" + this.workingMorgueFile);
				}
			}
		}
		String str = "";
		for (int i = 0; i < bodies.size(); i++) {
			str += bodies.get(i).toString();
		}
		Trial.writeFile(this.getDir(), this.workingMorgueFile, str);
	}


	public void closeTheMorgue(){
		String str = this.getTrialHeader();
		str += "Networks in Morgue: " + this.numDead + "\r\n\r\n";
		str += Trial.readFile(this.getDir(), this.workingMorgueFile);
		Trial.writeFile(this.getDir(), this.FileName + "--Morgue.txt", str);
		File oldMorgue = new File(this.getDir() + "/" + this.workingMorgueFile);
		oldMorgue.delete();
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

	public static void writeFile(String dir, String name, String value){
		String directoryName = dir;
		String fileName = name;

		File directory = new File(directoryName);
		if (! directory.exists()){
			directory.mkdir();
		}

		File file = new File(directoryName + "/" + fileName);
		if (file.exists()) {
			try{
				FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(value);
				bw.close();
			}
			catch (IOException e){
				e.printStackTrace();
				System.exit(-1);
			}
		}

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
	public abstract void evaluateAndUpdate(Network net, ArrayList<Double> SetOfInputs);


	//This method is the final evaluation.  If score updates after each set, then it can just put in evaluateAndUpdate().
	//  else it can take the state variables and compute a score
	public abstract void finalEvaluateAndScore(Network net, ArrayList<Double> SetOfInputs);

	public void printTrialResults() {
		String contents = getTrialHeader();
		contents += this.toStringMorgue();
		contents += this.toStringInputs();
		contents += "THE TOP 20% \r\n";
		for (int i = 0; i < this.theBest.size(); i++) {
			contents += this.getTheBest(i).toString();
		}		
		this.FileName = Trial.dateAndTime();
		Trial.writeFile(this.getDir(), this.FileName + ".txt", contents);
		if (this.fillTheMorgue$){
			this.closeTheMorgue(); 
		}
	}

	public String getTrialHeader() {
		String contents = this.getClass() + ", completed: " + Trial.dateAndTime() + "    Networks Trained for: " + Trial.convertNanoTime(this.getElapsed()) + "\r\n";
		contents += "   # of Networks : " + this.getNumNetworks() + "\r\n";
		contents += "     # of Cycles : " + this.getNumCycles() + "\r\n";
		contents += "Network Structure: " + Arrays.toString(this.getStructure()) + "\r\n";

		return contents;
	}

	public String toStringMorgue() {
		String str = "";
		if (this.getMorgue()) {
			str += "Dead Networks: KEPT\r\n";
		} else {
			str += "Dead Networks: DISCARDED\r\n";
		}

		return str;		
	}

	public String toStringInputs() {
		String contents = "Input Legend\r\n";
		contents += Arrays.toString(this.getInputLegend()) + "\r\n\r\n";
		contents += "Inputs\r\n";
		contents += this.stringTrialInputs() + "\r\n\r\n";

		return contents;
	}

	public static String readFile(String directory, String fileName) {
		String file = "";
		BufferedReader objReader = null;
		try {
			String strCurrentLine;
			objReader = new BufferedReader(new FileReader(directory + "/" + fileName));

			while ((strCurrentLine = objReader.readLine()) != null) {

				file += strCurrentLine + "\r\n";
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {
				if (objReader != null)
					objReader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return file;

	}
}




