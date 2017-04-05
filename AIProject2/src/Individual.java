/**
 * Created by cp10h on 3/8/17.
 */
import java.util.ArrayList;
import java.util.Random;

public class Individual {
    ArrayList<String> bitString = new ArrayList<String>();
    int fitness;
    double probability;

    public Individual() {}

    public Individual (Individual ind)
    {
        this.bitString = ind.bitString;
        this.fitness = ind.fitness;
        this.probability = ind.probability;
    }

    //sets up the random bitStrings for the variables
    public static Individual createIndividual(int num)
    {
        Individual ind = new Individual();
        String temp = "";
        for(int i=0; i<num; i++)
        {
            for(int j=0; j<8; j++)
            {
               Random r = new Random();
               //found this online...some witchcraft but it gets me my random 0s and 1s
               int n = r.nextInt((1-0)+1)+0;
               temp+=n;
            }
            ind.bitString.add(temp);
            temp = "";
        }
        return ind;
    }


}



