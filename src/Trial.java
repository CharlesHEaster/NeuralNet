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
	private int cycles, numNetworks, numDead, numHistoryInputs, firstGen;
	private int[] netStructure, inputHistoryStructure;
	private ArrayList<ArrayList<Double>> TrialInputs;
	private boolean fillTheMorgue$;
	private String[][] IOLegend;
	private long Start;
	private String dir, morgueDir, workingFileName, FileName;
	private Double evolveRate, learnRate;
	private Double[][] inputMinMax;

	//constructors
	public Trial(int numNetworks, int numCycles, int[] netStructure, ArrayList<ArrayList<Double>> trialInputs, String[][] ioLegend){
		this.IOLegend = ioLegend;
		this.TrialInputs = trialInputs;
		this.networks = new ArrayList<Network>();
		this.numNetworks = numNetworks;
		this.netStructure = netStructure;
		this.theBest = new ArrayList<Network>();
		this.cycles = numCycles;
		this.fillTheMorgue$ = false;
		this.evolveRate = 0.3;
		this.learnRate = 0.2;
		this.firstGen = 0;
		this.workingFileName = Trial.dateAndTime() + "_Working.txt";
		
	}

	public Trial(int numNetworks, int numCycles, int[] netStructure, ArrayList<ArrayList<Double>> trialInputs, String[][] ioLegend, int numHistoryInputs, int[] inputHistoryStrut){
		this(numNetworks, numCycles, netStructure, trialInputs, ioLegend);
		this.numHistoryInputs = numHistoryInputs;
		this.inputHistoryStructure = inputHistoryStrut;
		this.expandInputs();
		this.networks = new ArrayList<Network>();
		this.firstGen = 0;	
	}

	//Set = A set of inputs
	//Cycle = all sets run once, then compare and cull
	//Trial = all cycles
	public void run() {
		this.createNetworks();
		this.passInputMinMax();
		this.Start = System.nanoTime();
		for(int i = 1; i <= this.cycles; i++) {
			System.out.print("Cycle: " + i + "/" + this.cycles + " :: ");
			this.runCycle();
			String save = this.toSave(i);
			Trial.writeNewFile(this.getDir(), this.getWorkingFileName(), save);
		}
		System.out.println("--Compiling and Publishing Results--");
		this.printTrialResults();
		if (this.fillTheMorgue$) {
			this.closeTheMorgue();
		}
		System.out.println(":::::::::Training Program Complete:::::::::");
	}

	public void runCycle() {
		for (int setNum = 0; setNum < TrialInputs.size() - 1; setNum++) {
			this.runSet(TrialInputs.get(setNum));			
		}
		this.runFinalSet(TrialInputs.get(TrialInputs.size() - 1));
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

	public void runFinalSet(ArrayList<Double> inputs){
		for (int i = 0; i < this.networks.size(); i++){
			this.networks.get(i).resetOutputs();			
			this.networks.get(i).run(inputs);
			this.finalEvaluateAndScore(networks.get(i), inputs);
		}
	}

	public void createNetworks() {
		if (this.numHistoryInputs <= 0) {
			for (int i = 0; i < numNetworks; i++) {
				this.networks.add(new Network(this.netStructure, this.firstGen, this.IOLegend));
				this.firstGen++;
			}		
		} else {
			for (int i = 0; i < numNetworks; i++) {
				this.networks.add(new Network(this.netStructure, this.firstGen, this.IOLegend, this.numHistoryInputs, this.inputHistoryStructure));
				this.firstGen++;
			}
		}
	}

	public void setEvolveRate(Double evolveRate) {
		this.evolveRate = evolveRate;
		this.evolveRate = this.evolveRate > 1 ? 1 : this.evolveRate;
		this.evolveRate = this.evolveRate < 0.000000001 ? 0.000000001 : this.evolveRate;
	}

	public Double getEvolveRate() {
		return this.evolveRate;
	}

	public void setLearnRate(Double learnRate) {
		this.learnRate = learnRate;
		this.learnRate = this.learnRate > 2 ? 2 : this.learnRate;
		this.learnRate = this.learnRate < 0.000000001 ? 0.000000001 : this.learnRate;
	}

	public Double getLearnRate() {
		return this.learnRate;
	}
	
	public void setInputMinMax(Double[][] MinMax) {
		this.inputMinMax = MinMax;
	}
	
	public void passInputMinMax() {
		if (this.inputMinMax.length > 1) {
			for (int i = 0; i < this.getNumNetworks(); i++) {
				this.getNetwork(i).setInputMinMax(this.inputMinMax);
			}
		} else {
			for (int i = 0; i < this.getNumNetworks(); i++) {
				this.getNetwork(i).setInputMinMax(this.inputMinMax[0]);
			}
		}
	}

	public String getWorkingFileName() {
		return workingFileName;
	}

	public void setWorkingFileName(String workingFileName) {
		this.workingFileName = workingFileName;
	}

	public int[] getHistStructure() {
		return this.inputHistoryStructure;
	}

	public String getBaseDir() {
		return System.getProperty("user.dir");
	}
	
	public void setDir(String D) {
		this.dir = D;
		File directory = new File(this.dir);
		if (! directory.exists()){
			directory.mkdir();
		}
	}

	public String getDir() {
		return this.dir;
	}
	
	public void setMorgueDir(String dir) {
		this.morgueDir = dir;
	}
	
	public String getMorgueDir() {
		return this.morgueDir;
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
	
	public void setStructure(int[] structure) {
		this.netStructure = structure;
	}

	public void setTrialInputs(ArrayList<ArrayList<Double>> trialInputs) {
		this.TrialInputs = trialInputs;
	}

	public ArrayList<ArrayList<Double>> getTrialInputs(){
		return this.TrialInputs;
	}

	public void setInputLegend(String[] inputLegend) {
		this.IOLegend[0] = inputLegend;
	}

	public String[] getInputLegend() {
		return this.IOLegend[0];
	}
	
	public void setOutputLegend(String[] outputLegend) {
		this.IOLegend[1] = outputLegend;
	}

	public String[] getOutputLegend() {
		return this.IOLegend[1];
	}
	
	public void setIOLegend(String[][] ioLegend) {
		this.IOLegend = ioLegend;
	}

	public String[][] getIOLegend() {
		return this.IOLegend;
	}

	public ArrayList<Network> getTheBest(){
		return this.theBest;
	}

	public Network getTheBest(int i){
		return this.theBest.get(i);
	}

	public long getElapsed() {
		return  System.nanoTime() - this.Start;
	}

	public ArrayList<Double> getInputSet(int i){
		return this.TrialInputs.get(i);
	} 

	public String stringTrialInputs() {
		String str = "";
		ArrayList<ArrayList<Double>> uniqueInputs = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i < this.getTrialInputs().size(); i++) {
			uniqueInputs.add(Trial.getUniqueInputs(this.getTrialInputs().get(i)));
		}
		for (int i = 0; i < uniqueInputs.size(); i++) {
			str += uniqueInputs.get(i).toString();
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

	public void expandInputs() {
		ArrayList<ArrayList<Double>> trialInputs = new ArrayList<ArrayList<Double>>(this.TrialInputs);
		this.TrialInputs.clear();
		for (int i = 0; i < trialInputs.size(); i++){ //for each set of inputs
			ArrayList<Double> oldInputs = new ArrayList<Double>(trialInputs.get(i));
			ArrayList<Double> expandedInputs = new ArrayList<Double>();
			for (int j = 0; j < trialInputs.get(i).size(); j++) { //for each input in a set
				if (j < this.numHistoryInputs) {
					for (int k = 0; k < this.inputHistoryStructure.length; k++)
						expandedInputs.add(oldInputs.get(j));
				} else {
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
			ArrayList<Network> newBodies = new ArrayList<Network>(this.networks.subList(top20,  this.networks.size() - 1));
			this.addToTheMorgue(newBodies);
			this.numDead += newBodies.size();
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
			if (this.numHistoryInputs == 0) {
				this.networks.add(new Network(this.netStructure, this.firstGen, this.IOLegend));
				this.firstGen++;
			} else {
				this.networks.add(new Network(this.netStructure, this.firstGen, this.IOLegend, this.numHistoryInputs, this.inputHistoryStructure));
				this.firstGen++;
			}
		}
		System.out.print(" High Score: " + this.theBest.get(0).getScore() + "\r\n");
	}

	public void addToTheMorgue(ArrayList<Network> bodies){
		File directory = new File(this.morgueDir);
		if (! directory.exists()){
			directory.mkdir();
		}
		String morgueFile = "1.txt";
		File morgueFull = new File(this.morgueDir + "/" + morgueFile);
		for (int i = 2; morgueFull.exists(); i++) {
			morgueFile = "/" + i + ".txt";
			morgueFull = new File(this.morgueDir +  "/" + morgueFile);
		}
		String str = "";
		for (int i = 0; i < bodies.size(); i++) {
			str += bodies.get(i).toSave();
		}
		Trial.addMorgueFile(this.morgueDir, morgueFile, str);
	}
	
	public void closeTheMorgue() {
		File sourceFile = new File(this.morgueDir);
		File destFile = new File(this.getDir() + "/" + this.FileName.substring(0, this.FileName.length() - 4) + "_Morgue");
		if(sourceFile.renameTo(destFile)) {
			this.setMorgueDir(destFile.getPath());
		}
	}

	public void compileTheMorgue() {
		if (!this.fillTheMorgue$) {
			System.out.println("Cannot compile the morgue because it is not on.");
		} else {
			System.out.println("Compiling Morgue...");
		}
		String theWholeMorgue = "";
		String morgueFileName = "/1.txt";
		File morgueFile = new File(this.morgueDir + morgueFileName);
		for (int i = 2; morgueFile.exists(); i++) {
			theWholeMorgue += Trial.readFile(this.morgueDir, morgueFileName);
			
			morgueFileName = "/" + i + ".txt";
			morgueFile = new File(morgueDir + morgueFileName);
		}
		
		if(Trial.writeNewFile(this.dir, this.FileName.substring(0, this.FileName.length() - 4) + "_Morgue.txt", theWholeMorgue)) {
			System.out.println("Morgue Compiled");
		} else {
			System.out.println("Morgue Compile Failed");
		}
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

	public static boolean addMorgueFile(String dir, String name, String value){
		String directoryName = dir;
		String fileName = name;

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
			return true;
		}
		catch (IOException e){
			e.printStackTrace();
			System.exit(-1);
			return false;
		}
	}

	public static boolean writeNewFile(String dir, String name, String value){
		String directoryName = dir;
		String fileName = name;

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
			return true;
		}
		catch (IOException e){
			e.printStackTrace();
			System.exit(-1);
			return false;
		}
	}

	
	//This method takes in a network and it's outputs.  then evaluates those outputs against the answer and updates the network
	public abstract void evaluateAndUpdate(Network net, ArrayList<Double> SetOfInputs);


	//This method is the final evaluation.  If score updates after each set, then it can just put in evaluateAndUpdate().
	//  else it can take the state variables and compute a score
	public abstract void finalEvaluateAndScore(Network net, ArrayList<Double> SetOfInputs);

	public String toSave(int currentCycle) {
		String save = this.getClass() + ", Copy Saved: " + Trial.dateAndTime();
		save = save.substring(6);
		save += "    Networks Trained for: " + Trial.convertNanoTime(this.getElapsed()) + "\r\n";
		save += "   # of Networks : " + this.getNumNetworks() + "\r\n";
		save += "          Cycles : " + currentCycle + "/" + this.getNumCycles() + "\r\n";
		save += this.toStringMorgue();
		save += "       Learn Rate: " + this.learnRate + "\r\n";
		save += "      Evolve Rate: " + this.evolveRate + "\r\n";
		save += " Starting Network Structure: " + Arrays.toString(this.getStructure()) + "\r\n";
		save += "  FirstGen Networks Created: " + this.firstGen + "\r\n";
		save += this.toStringInputs();
		save += "BestNetworks{\r\n";
		for (int i = 0; i < networks.size(); i++) {
			save += networks.get(i).toSave(); 
		}
		save += "}/BestNetworks";

		return save;
	}
	public void printTrialResults() {
		String contents = getTrialHeader();
		contents += "THE TOP 20% \r\n";
		for (int i = 0; i < this.theBest.size(); i++) {
			contents += this.getTheBest(i).toString();
		}		
		this.FileName = Trial.dateAndTime() + ".txt";
		if (Trial.writeNewFile(this.getDir(), this.FileName, contents)) {
				File working = new File(this.getDir() + "/" + this.getWorkingFileName());
				working.delete();
			}
		}
	

	public String getTrialHeader() {
		String contents = this.getClass() + ", completed: " + Trial.dateAndTime() + "    Networks Trained for: " + Trial.convertNanoTime(this.getElapsed()) + "\r\n";
		contents = contents.substring(6);
		contents += "   # of Networks : " + this.getNumNetworks() + "\r\n";
		contents += "     # of Cycles : " + this.getNumCycles() + "\r\n";
		contents += this.toStringMorgue();
		contents += " Starting Network Structure: " + Arrays.toString(this.getStructure()) + "\r\n";
		contents += this.toStringInputs();

		return contents;
	}

	public String toStringMorgue() {
		String str = "    Dead Networks: ";
		if (this.getMorgue()) {
			str += "KEPT\r\n";
		} else {
			str += "DISCARDED\r\n";
		}

		return str;		
	}

	public String toStringInputs() {
		String inputString = "";
		if (this.numHistoryInputs > 0) {
			inputString += "      Unique History Inputs: " + this.numHistoryInputs + "\r\n";
			inputString += "InputNode History Structure: " + Arrays.toString(this.getInputHistoryStructure()) + "\r\n";
			}
		inputString += "               Input Legend: ";
		inputString += Arrays.toString(this.getInputLegend()) + "\r\n";
		inputString += "Inputs\r\n";
		inputString += this.stringTrialInputs() + "\r\n\r\n";

		return inputString;
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

	public static ArrayList<Double> getUniqueInputs(ArrayList<Double> originalInputs){
		ArrayList<Double> uniqueInputs = new ArrayList<Double>();
		uniqueInputs.add(originalInputs.get(0));
		for (int i = 1; i < originalInputs.size(); i++) {
			if (originalInputs.get(i) != uniqueInputs.get(uniqueInputs.size() - 1)) {
				uniqueInputs.add(originalInputs.get(i));
			}
		}

		return uniqueInputs;
	}
}




