/**
 * Created by cp10h on 3/7/17.
 */

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Project
{
    private static int numVar;
    private static int[][] coefficients;
    private static int popSize = 1000;
    private static int MaxGeneration = 100;
    private static double Step = .25;
    private static int fitTotal = 0;

    private static Individual bestInd = null;
    private static Individual avgInd = null;
    private static int bestFit = 0;
    private static int avgFit = 0;




    static Individual ind;
    static ArrayList<Individual> population = new ArrayList<>();
    static ArrayList<Individual> newPop = new ArrayList<>();

    public static void main(String[] args) throws FileNotFoundException
    {
        //creates spaces for a new file, then reads in the entered filename
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter filename: ");
        String fileName = keyboard.nextLine();
        keyboard.close();

        //Method Calls
        File file = createFile(fileName);
        ArrayList<Integer> gridNums = FileToArray(file);
        createGrid(gridNums);
        generatePop();
        for(int i=0; i<MaxGeneration; i++) {
            evaluate();
            fitness();
            adjustFitness();
            calcTotalFit();
            selection();
            mutation();
            population = newPop;
        }
        getMaxFit();
    }

    //takes the inputed text and makes it a file
    public static File createFile(String fileName)
    {
        File file = null;

        try {
            file = new File(fileName);
            if(!file.exists())
            {
                System.out.print("File not found");
                System.exit(0);
            }
        }catch(Exception e){}

        return file;
    }

    //creates the grid that the evaluate function will come from
    public static ArrayList<Integer> FileToArray(File file) throws FileNotFoundException {
        //buffered reader
        Scanner scan = new Scanner(new BufferedReader(new FileReader(file)));

        ArrayList<Integer> gridNums = new ArrayList<Integer>();
        numVar = scan.nextInt();

        while(scan.hasNext())
        {
            if(scan.hasNextInt()) gridNums.add(scan.nextInt());
            else scan.next();
        }
        return gridNums;
    }

    //makes the grid the evaluation function will come from
    public static void createGrid(ArrayList<Integer> gridNums)
    {
        //grid to store variable coefficients
        coefficients = new int[numVar+1][numVar+1];

        for (int i = 0; i < numVar+1; i++) {
            for (int r = 0; r < numVar+1; r++) {
                coefficients[i][r] = gridNums.get(0);
                gridNums.remove(0);
            }
        }

    }

    //generates our population
    public static void generatePop()
    {
        for(int i=0; i<=popSize; i++)
        {
            Individual tempInd = new Individual(Individual.createIndividual(numVar));
            population.add(tempInd);
        }

    }

    //converts the bitStrings to Integers
    public static ArrayList<Integer> convertBitToInt(ArrayList<String> bitString)
    {
        ArrayList<Integer> results = new ArrayList<Integer>();
        for(int i=0; i<bitString.size(); i++)
        {try {
            results.add(Integer.parseInt(bitString.get(i), 2));
        }catch (NumberFormatException nfe){};
        }
        return results;
    }

    //evaluates the individuals with the function we processed from the 2D array
    public static void evaluate()
    {
        ArrayList<Integer> temp = new ArrayList<>();
        for(int r=0; r<popSize; r++) {
            temp = convertBitToInt(population.get(r).bitString);
            int fit = 0;
            int tempVar = numVar;
            //loop through the 2D array
            for (int i = 0; i < numVar; i++) {
                for(int j=0; j<tempVar; j++)
                {
                    fit += coefficients[i][j+i] * temp.get(i) * temp.get(j);
                    j++;
                }
                tempVar--;
            }
            population.get(r).fitness = fit;
        }
    }

    //compare the fitness of the individuals
    public static void fitness()
    {
        Collections.sort(population, new Comparator<Individual>() {
            public int compare(Individual i1, Individual i2) {
                return (int) (i1.fitness- i2.fitness);
            }
        });
    }

    //finds the total fitness of the group
    public static void calcTotalFit()
    {
        for(int i=0; i<popSize; i++)
            fitTotal += population.get(i).fitness;
    }

    //increment the entire population by the lowest fitness
    public static void adjustFitness()
    {
        int incrementVal = population.get(0).fitness*(-1);
        for(int i=0; i<popSize; i++)
            population.get(i).fitness += incrementVal;
    }

    //select members for the new population
    public static void selection()
    {
        for(int i=0; i<popSize; i++)
        {
            double dubFit = (double)(population.get(i).fitness);
            double dubFitTotal = (double)(fitTotal);
            population.get(i).probability = (dubFit/dubFitTotal);
        }

        Individual temp1 = null;
        Individual temp2 = null;

        //array of cumulative probabilities
        double runningTotal = 0.0;
        ArrayList<Double> cumProb = new ArrayList<>();
        for(int i=0; i<popSize; i++)
        {
            runningTotal += population.get(i).probability;
            cumProb.add(runningTotal);
        }

        while(newPop.size() != popSize) {
            //select candidate 1
            Random rnd = new Random();
            Double rndDub = rnd.nextDouble();

            for (int i = 0; i < popSize; i++) {
                if (rndDub > cumProb.get(i) && rndDub < cumProb.get(i + 1)) {
                    temp1 = population.get(i);
                }
            }

            //select candidate 2
            Random rnd2 = new Random();
            Double rndDub2 = rnd.nextDouble();

            for (int i = 0; i < popSize; i++) {
                if (rndDub2 > cumProb.get(i) && rndDub2 < cumProb.get(i + 1)) {
                    temp2 = population.get(i);
                }
            }

            crossover(temp1, temp2);
        }

    }

    //mutates a gene
    public static void mutation()
    {
        Random rnd = new Random();
        for(int i=0; i<popSize; i++)
        {
            int randNum = rnd.nextInt(10+1-1) +1;
            if(randNum == 1) {
                int randString = rnd.nextInt(7 + 1 - 0) + 0;
                for (int r = 0; r < randString; r++) {
                    if (newPop.get(i).bitString.get(0).charAt(r) == '0')
                        newPop.get(i).bitString.get(0).replace('0', '1');
                    else
                        newPop.get(i).bitString.get(0).replace('1', '0');
                }
            }
        }
    }

    //crosses over two gene
    public static void crossover(Individual ind1, Individual ind2)
    {
        Random rn = new Random();

        String temp1 = "";
        String temp2 = "";
        //String temp3 = "";
        //String temp4 = "";

        //System.out.println(ind1.bitString);
        //System.out.println(ind2.bitString);

        //System.out.println(ind1.bitString);
        //System.out.println(ind2.bitString);

        int randomNum = rn.nextInt((numVar-1) + 1 - 0) + 0;
        //System.out.println(randomNum);
        int randomNum2 = rn.nextInt((numVar - 1) + 1 - 0) + 0;
        //System.out.println(randomNum2);

        temp1 = ind1.bitString.get(randomNum);
        //System.out.println(temp1);
        temp2 = ind2.bitString.get(randomNum2);
        //System.out.println(temp2);

        ind1.bitString.remove(randomNum);
        ind1.bitString.add(randomNum, temp2);

        ind2.bitString.remove(randomNum);
        ind2.bitString.add(randomNum, temp1);

//              temp3 = ind1.bit.get(randomNum2);
//              temp4 = ind2.bit.get(randomNum2);
//
//              ind2.bit.add(randomNum2, temp3);
//              ind2.bit.remove(randomNum2+1);
//
        //System.out.println(ind1.bitString);
        //System.out.println(ind2.bitString);
        newPop.add(ind1);
        newPop.add(ind2);

    }

    //finds the maximum fitness
    public static void getMaxFit(){
        int maxFit = population.get(0).fitness;
        for(int i=0;i < popSize;i++){
            if(population.get(i).fitness > maxFit){
                maxFit = population.get(i).fitness;
            }
        }
        System.out.println("The best fitness found was: " + maxFit);
    }

}
