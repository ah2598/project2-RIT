package ptui;

import model.RITQTNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class RITUncompress
{
    /**
     * Given a text representation of a quadtree,
     * create a Quadtree from said text.
     *
     * @param values Quadtree values
     * @return Quadtree node
     */
    private static RITQTNode parse(ArrayList<Integer> values)
    {
        int val = values.remove(0);

        if(val == -1)
            return new RITQTNode(val, parse(values), parse(values), parse(values), parse(values));
        else
            return new RITQTNode(val);
    }

    /**
     * Given a compressed quadtree file,
     * converts file into a list of values.
     *
     * @param file Scanner containing file to read from
     * @return list containing each pixel file
     */
    private static ArrayList<Integer> readFile(Scanner file)
    {
        ArrayList<Integer> tokens = new ArrayList<Integer>();

        //Adds each pixel value to arraylist
        try
        {
            while(file.hasNext())
            {
                int val = Integer.parseInt(file.next());
                tokens.add(val);

                //Error Check: Quadtree value isn't in range 0-255
                if(val != -1 && (val < 0 || val > 255))
                {
                    System.out.println("Quadtree contains invalid pixel value! (Valid pixel value: 0-255)");
                    System.exit(-1);
                }
            }
        }
        //Error Check: Image file contains non-int value
        catch(NumberFormatException e)
        {
            System.out.println("Image file contains a non-integer!");
            System.exit(-1);
        }

        return tokens;
    }

    public static void main(String[] args)
    {
        if (args.length != 2) {
            System.out.println("Usage: java RITUncompress compressed.rit uncompressed.txt");
            System.exit(-1);
        }

        System.out.println("Uncompressing: " + args[0]);

        //Creates file reader
        Scanner input = null;
        try{
            input = new Scanner(new File(args[0]));
        }
        //Error Check: The provided file cannot be found
        catch(FileNotFoundException e)
        {
            System.out.println("Program cannot find the file specified!");
            System.exit(-1);
        }

        //Reads and store file data
        int sideLength = (int) Math.sqrt(input.nextInt());
        ArrayList<Integer> tokens = readFile(input);

        //Converts arraylist into a quadtree structure and displays quadtree
        RITQTNode quadtree = parse(tokens);
        System.out.println("QTree: " + quadtree);
    }
}