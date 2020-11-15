import java.util.ArrayList;

public class ColorTrial extends Trial {
	

	public ColorTrial(int numNetworks, int numCycles, int[] netStructure, int numColors) {
		super(numNetworks, numCycles, netStructure, new ArrayList<ArrayList<Double>>(), new String[2][]);
		this.setTrialInputs(ColorTrial.makeSomeColors(numColors));
		this.setInputLegend(new String[] {"Red", "Green", "Blue"});
		String[] outputLegend = new String[] {"Black", "Dark Grey", "Grey", "Light Grey", "White", "Red", "Green", "Blue", "Cyan", "Magenta", "Yellow"};
		this.setOutputLegend(outputLegend);
		this.setDir(this.getBaseDir() + "/ColorTrialResults");
		netStructure[0] = 3;
		netStructure[netStructure.length - 1] = 11;
		this.setStructure(netStructure);
		Double[][] minmax = new Double[][]{{0.0, 255.0}};
		this.setInputMinMax(minmax);
		this.setDir(this.getBaseDir() + "/ColorTrialResults");
		this.setMorgueDir(this.getDir() + "/" + this.getWorkingFileName().substring(0, this.getWorkingFileName().length() - 4) + "_Morgue");
	}
	
	public static ArrayList<ArrayList<Double>> makeSomeColors(int num){
		ArrayList<ArrayList<Double>> arrayListOfColors = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i < num; i++) {
			arrayListOfColors.add(ColorTrial.randColor());
		}
		return arrayListOfColors;
	}
	@Override
	//This method takes in a network and it's outputs.  then evaluates those outputs against the answer and updates the network (score or state variables)
	public void evaluateAndUpdate(Network net, ArrayList<Double> inputColor, int setNum) {
		OutputNode highestOutput = Network.findHighestOutputNode(net);
		String NetworkOut = highestOutput.getOutputLabel();
		
		ArrayList<Double> color = new ArrayList<Double>(inputColor);
		if (this.getNumHistoryInputs() != 0) {
			color = Trial.getUniqueInputs(color);
		}
		
		String answer = ColorTrial.defineColor(color);

		if (NetworkOut.equals(answer)) {
			net.incScore();
		}
		//System.out.println("Out: " + NetworkOut + "\nAns: " + answer + "\nInc? " + NetworkOut.equals(answer) + "\n-----");
	}
	
	@Override
	public void finalEvaluateAndScore(Network net, ArrayList<Double> SetInputs, int setNum) {
		this.evaluateAndUpdate(net, SetInputs, setNum);
	}
	
	public static String defineColor(ArrayList<Double> color) {
		int R = (int)(Math.floor(color.get(0)));
		int G = (int)(Math.floor(color.get(1)));
		int B = (int)(Math.floor(color.get(2)));
		return ColorTrial.defineColor(R, G, B);	
	}
	
	public static String defineColor(int R, int G, int B) {
		if (ColorTrial.isGrayScale(R, G, B)) {
			return ColorTrial.defineGrayScale(R, G, B);
		} else if (ColorTrial.isSubtractivePrimary(R, G, B)) {
			return ColorTrial.defineSubtractivePrimary(R, G, B);
		} else {
			return ColorTrial.definePrimary(R, G, B);
		}
	}
	
	public static Boolean isGrayScale(int R, int G, int B) {
		double avg = ( R + G + B ) / 3;
		double avgMax = avg + 25.5;
		double avgMin = avg - 25.5;
		int[] color = {R, G, B};
		for (int V : color) {
			if (V < avgMin || V > avgMax) {
				return false;
			}
		}
		return true;
	}
	
	public static Boolean isSubtractivePrimary(int R, int G, int B) {
		int[] color = {R, G, B};
		double Max = 0;
		for (int V : color) {
			Max = Max < V ? V : Max;
		}
		int count = 0;
		for (int V : color) {
			if ( V >= Max - 25.5) {
				count++;
			}
		}
		if (count == 2) {
			return true;
		}
		return false;
	}

	public static String defineGrayScale(int R, int G, int B) {
		int total = R + G + B;
		if (total < 153) {
			return "Black";
		} else if (total < 306) {
			return "Dark Grey";
		} else if (total < 459) {
			return "Grey";
		} else if (total < 612) {
			return "Light Grey";
		} else {
			return "White";
		}	
	}
	
	public static String defineSubtractivePrimary(int R, int G, int B) {
		int[] color = {R, G, B};
		double Max = 0;
		for (int V : color) {
			Max = Max < V ? V : Max;
		}
		if (R < Max - 25.5) {
			return "Cyan";
		} else if (G < Max - 25.5) {
			return "Magenta";
		} else if (B < Max - 25.5) {
			return "Yellow";
		}
		return "Really Fuck";
	}
	
	public static String definePrimary(int R, int G, int B) {
		if (R > G && R > B) {
			return "Red";
		} else 	if (G > B && G > R) {
			return "Green";
		} else 	if (B > G && B > R) {
			return "Blue";
		}
		return "Fuck Again";
	}

	public static ArrayList<Double> randColor(){
		ArrayList<Double> color = new ArrayList<Double>();
		for (int i = 0; i < 3; i++) {
			color.add(Math.floor(Math.random() * 256));
		}
		double rando = Math.random();
		if (rando < 0.025) {
			for (int i = 0; i < 3; i++) {
				color.add(255.0);
			}
		} else if (rando < 0.05) {
			for (int i = 0; i < 3; i++) {
				color.add(0.0);
			}
		}
		return color;
	}
	
	public static ColorTrial load(String trialFile) {
		String Full = Trial.readFile(trialFile);
		String[] values = Full.split(Trial.marker());
		int i = 1;
		String trialClass = values[i]; 
		System.out.println(values[i]);
		i = i + 2;
		System.out.println(values[i]);
		Long trainedFor = Long.parseLong(values[i]);
		i = i + 2;
		Integer numNetworks = Integer.parseInt(values[i]);
		i = i + 2;
		Integer currentCycle = Integer.parseInt(values[i]);
		i = i + 2;
		Integer numCycles = Integer.parseInt(values[i]);
		i = i + 2;
		Boolean morgue$ = values[i].equals("KEPT");
		i = i + 2;
		Double learnRate = Double.parseDouble(values[i]);
		i = i + 2;
		Double evolveRate = Double.parseDouble(values[i]);
		i = i + 2;
		Integer[] strut = Trial.convertToIntegerArray(Trial.unstringToInteger(Trial.unpackArrayList(values[i])));
		int[] netStructure = new int[strut.length];
		for (int j = 0; j < strut.length; j++) {
			netStructure[j] = strut[j].intValue();
		}
		i = i + 2;
		Integer firstGen = Integer.parseInt(values[i]);		 
		i = i + 2;
		String[] inputLegend = Trial.convertToStringArray(Trial.unpackArrayList(values[i]));
		i = i + 2;
		String[] outputLegend = Trial.convertToStringArray(Trial.unpackArrayList(values[i]));
		i = i + 2;
		String[][] IOLegend = new String[2][];
		IOLegend[0] = inputLegend;
		IOLegend[1] = outputLegend;		 
		ArrayList<ArrayList<Double>> inputs = Trial.unstringArrayList2d(values[i]);
		int numColors = inputs.size();
		i = i + 2;
		String strTheBest = values[i];
		i = i + 2;
		ArrayList<Network> theBest = Trial.extractNetworks(strTheBest);
		String strTheRest = values[i];
		ArrayList<Network> theRest = Trial.extractNetworks(strTheRest);
		
		ColorTrial CT = new ColorTrial(numNetworks, numCycles, netStructure, numColors);
		CT.setCurrentCycle(currentCycle);
		CT.setMorgue(morgue$);
		CT.setLearnRate(learnRate);
		CT.setEvolveRate(evolveRate);
		CT.setFirstGen(firstGen);
		for (Network N : theBest) {
			N.setIOLegend(IOLegend);
		}
		for (Network N : theRest) {
			N.setIOLegend(IOLegend);
		}
		CT.setTheBest(theBest);
		CT.setNetworks(theRest);
		CT.setTrainedFor(trainedFor);
		CT.setTrialInputs(inputs);
		
		return CT;
	}
	

}
