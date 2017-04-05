
public class Cell {
	
	private boolean possibleNums[] = new boolean[9];
	private int value;
	private int subGridNum;
	
	public Cell(int iValue, int jValue){
		for(int i = 0; i < possibleNums.length; i++){
			possibleNums[i] = true;
			value = 0;
			if(iValue < 3 && jValue < 3)
				subGridNum = 1;
			else if(iValue < 3 && jValue < 6)
				subGridNum = 2;
			else if(iValue < 3 && jValue < 9)
				subGridNum = 3;
			else if(iValue < 6 && jValue < 3)
				subGridNum = 4;
			else if(iValue < 6 && jValue < 6)
				subGridNum = 5;
			else if(iValue < 6 && jValue < 9)
				subGridNum = 6;
			else if(iValue < 9 && jValue < 3)
				subGridNum = 7;
			else if(iValue < 9 && jValue < 6)
				subGridNum = 8;
			else if(iValue < 9 && jValue < 9)
				subGridNum = 9;
		}
	}
	
	public boolean couldBe(int pNum){
		if(possibleNums[pNum]){
			return true;
		}
		return false;
	}
	
	public void printPossibleNums(){
		for(int i = 0; i < possibleNums.length; i++){
			System.out.print(possibleNums[i] + " ");
		}
		System.out.print("\n");
	}
	
	public int getSubGridNum(){
		return subGridNum;
	}
	
	public void setValue(int pValue){
		value = pValue;
		if(value != 0)
			for(int i = 0; i < possibleNums.length; i++)
				possibleNums[i] = false;
	}
	
	public int getValue(){
		return value;
	}
	
	public void setPosNumFalse(int pNum){
		possibleNums[pNum -1] = false;
	}
	
	public void flipPossibleNums(int pNum){
		if(possibleNums[pNum -1])
			possibleNums[pNum -1] = false;
		else
			possibleNums[pNum -1] = true;
	}
	
	public int[] getPossibleNums(){
		int position = 0;
		int nums = getNumPossibleNums();
		int tempArr[] = new int[nums];
		for(int i = 0; i < possibleNums.length; i++){
			if(possibleNums[i])
				tempArr[position] = i + 1;
				
		}
		return tempArr;
	}
	
	public int getNumPossibleNums(){
		int numsPossible = 0;
		for(int i = 0; i < possibleNums.length; i++){
			if(possibleNums[i])
				numsPossible++;
		}
		return numsPossible;
	}
	
	public void setAllFalse(){
		for(int i = 0; i < possibleNums.length; i++){
			possibleNums[i] = false;
		}
	}
	
	public String toString(){
		String str = this.getValue() + " ";
		return str;
	}
	
}
