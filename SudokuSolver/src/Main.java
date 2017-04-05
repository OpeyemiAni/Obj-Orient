import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
	
	private static Cell sudokuPuzzle[][];
	private static Cell workSpace[][];
	private static final int SUDOKUSIZE = 9;
	private static boolean done = false;
	private static int count = 0;
	private static int numIterations = 0;
	

	public static void main(String[] args) {
		sudokuPuzzle = new Cell[SUDOKUSIZE][SUDOKUSIZE];
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				sudokuPuzzle[i][j] = new Cell(i, j);
			}
		}
		
		System.out.println("Welcome to the program.");
	    Scanner keyboard = new Scanner(System.in);
	    System.out.println("Enter filename: ");
	    String fileName = keyboard.nextLine();
	    keyboard.close();
	    File file = new File(fileName);
	    readyFile(file);
	    for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				System.out.print(sudokuPuzzle[i][j]);
			}
			System.out.print("\n");
		}
	    
	    for(int i = 0; i < sudokuPuzzle.length; i++){
	    	for(int j = 0; j < 9; j++){
				setPossibleValues(i, j, sudokuPuzzle[i][j]);
			}
	    }
	    sudokuPuzzle[5][6].printPossibleNums();
	    while(!done){
	    	onePosCheck();
	    	checkNeeded();
	    	for(int i = 0; i < sudokuPuzzle.length; i++){
		    	for(int j = 0; j < 9; j++){
					setPossibleValues(i, j, sudokuPuzzle[i][j]);
				}
		    }
	    	done = checkDone();
	    	count++;
	    	if(count % 5 == 0)
	    	{	for(int i = 0; i < 9; i++){
					for(int j = 0; j < 9; j++){
						System.out.print(sudokuPuzzle[i][j]);
				}
				System.out.print("\n");
			}
	    	System.out.print("-------------------\n");
	    	}
	    	numIterations++;
	    	
	    }
	    //sudokuPuzzle[5][6].printPossibleNums();
	    System.out.println("Final Answer");
	    for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				System.out.print(sudokuPuzzle[i][j]);
			}
			System.out.print("\n");
		}
	    System.out.println("Number of Iterations to complete: " + numIterations);
		

	}
	
	public static void readyFile(File file){
		try{
			Scanner scn = new Scanner(file);
			
			for(int i = 0; i < SUDOKUSIZE; i++){
				for(int j = 0; j < SUDOKUSIZE; j++){
					sudokuPuzzle[i][j].setValue(scn.nextInt());
				}
			}
			scn.close();
		}
		catch(FileNotFoundException e){
			System.out.println("File could not be found.\nProgram is now ending.");
			System.exit(0);
		}
	}
	
	public static void setPossibleValues(int vPos, int hPos, Cell cell){
		for(int i = 0; i < SUDOKUSIZE; i++){
			if(sudokuPuzzle[i][hPos].getValue() != 0){
				cell.setPosNumFalse((sudokuPuzzle[i][hPos].getValue()));
			}
		}
		for(int j = 0; j < SUDOKUSIZE; j++){
			if(sudokuPuzzle[vPos][j].getValue() != 0){
				cell.setPosNumFalse((sudokuPuzzle[vPos][j].getValue()));
			}
		}
		for(int i = 0; i < SUDOKUSIZE; i++){
			for(int j = 0; j < SUDOKUSIZE; j++)
			if(cell.getSubGridNum() == sudokuPuzzle[i][j].getSubGridNum() && sudokuPuzzle[i][j].getValue() != 0)
				cell.setPosNumFalse((sudokuPuzzle[i][j].getValue()));
		}
	}
	
	public static void onePosCheck(){
		for(int i = 0; i < SUDOKUSIZE; i++){
			for(int j = 0; j < SUDOKUSIZE; j++)
				if(sudokuPuzzle[i][j].getNumPossibleNums() == 1){
					//System.out.println("test");
					sudokuPuzzle[i][j].setValue(sudokuPuzzle[i][j].getPossibleNums()[0]);
				}
		}
	}
	
	public static boolean checkDone(){
		for(int i = 0; i < SUDOKUSIZE; i++){
			for(int j = 0; j < SUDOKUSIZE; j++)
				if(sudokuPuzzle[i][j].getValue() == 0)
					return false;
		}
		return true;
	}
	
	public static void checkNeeded(){
		neededNumsRows();
		neededNumsCols();
		neededNumsSub();
	}
	
	public static void neededNumsRows(){
		boolean needed[] = new boolean[9];
		for(int i = 0; i < 9; i++){
			needed[i] = true;
		}
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if(sudokuPuzzle[i][j].getValue() != 0){
					needed[sudokuPuzzle[i][j].getValue()-1] = false;
				}
			}
			for(int j = 0; j < needed.length; j++){
				int count = 0;
				if(needed[j]){
					for(int k = 0; k < 9; k++){
						if(sudokuPuzzle[i][k].couldBe(j)){
							count++;
						}
					}
				}
				if(count == 1){
					for(int k = 0; k < 9; k++){
						if(sudokuPuzzle[i][k].couldBe(j)){
							sudokuPuzzle[i][k].setValue(j+1);
						}
					}
				}
			}
		}
	}
	
	public static void neededNumsCols(){
		boolean needed[] = new boolean[9];
		for(int i = 0; i < 9; i++){
			needed[i] = true;
		}
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				if(sudokuPuzzle[j][i].getValue() != 0){
					needed[sudokuPuzzle[j][i].getValue()-1] = false;
				}
			}
			for(int j = 0; j < needed.length; j++){
				int count = 0;
				if(needed[j]){
					for(int k = 0; k < 9; k++){
						if(sudokuPuzzle[k][i].couldBe(j)){
							count++;
						}
					}
				}
				if(count == 1){
					for(int k = 0; k < 9; k++){
						if(sudokuPuzzle[k][i].couldBe(j)){
							sudokuPuzzle[k][i].setValue(j+1);
						}
					}
				}
			}
		}
	}
	
	public static void neededNumsSub(){
		
		Cell subGroup[] = new Cell[9];
		boolean needed[] = new boolean[9];
		for(int i = 0; i < 9; i++)
			needed[i] = true;
		for(int i = 0; i < 9; i++){
			int openSpace = 0;
			for(int j = 0; j < 9; j++){
				for(int k = 0; k < 9; k++){
					if(sudokuPuzzle[j][k].getSubGridNum() == (i+1)){
						if(sudokuPuzzle[j][k].getValue() != 0)
							needed[sudokuPuzzle[j][k].getValue() - 1] = false;
						subGroup[openSpace] = sudokuPuzzle[j][k];
						openSpace++;
					}
				}
			}
			for(int j = 0; j < 9; j++){
				int count = 0;
				if(needed[j]){
					for(int k = 0; k < 9; k++){
						if(subGroup[k].couldBe(j)){
							count++;
						}
			
					}
		
				}
				if(count == 1){
					for(int k = 0; k < 9; k++){
						if(subGroup[k].couldBe(j)){
							subGroup[k].setValue(j+1);
						}
					}
				}

			}
			
		}
	}
}
