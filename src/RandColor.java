
public class RandColor {
	public int[] values;
	public RandColor(){
		this.values = new int[3];
		this.values[0] = (int) (Math.floor(Math.random() * 256));
		int i = (int) (Math.floor(Math.random() * 256));
		while (i == this.values[0]) {
			i = (int) (Math.floor(Math.random() * 256));
		}
		this.values[1] = i;
		i = (int) (Math.floor(Math.random() * 256));
		while (i == this.values[0] || i == this.values[1]) {
			i = (int) (Math.floor(Math.random() * 256));
		}
		this.values[2] = i;
	} 
	
	public int[] getValues() {
		return this.values;
	}
	
	public int getValue(int i) {
		return this.values[i];
	}
}
