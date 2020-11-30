package ptui;

import model.RITQTNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
    public static RITQTNode parse(ArrayList<Integer> values)
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
    public static ArrayList<Integer> readFile(Scanner file)
    {
        ArrayList<Integer> tokens = new ArrayList<Integer>();

        //Adds each quadtree value to arraylist
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

    /**
     * Given an 2D pixel array, writes image to given file.
     *
     * @param image the 2D pixel array
     * @param file the file to write to
     */
    public static void writeImage(int[][] image, File file)
    {
        String path = "";

        try
        {
            //Checks whether or not file is already there
            if(!file.createNewFile())
            {
                System.out.println("Uncompressed file already exists!");
                System.exit(-1);
            }

            //Writes image to file
            path = file.getCanonicalPath();
            FileWriter writer = new FileWriter(file);
            for(int row = 0; row < image.length; row++)
            {
                for(int col = 0; col < image[0].length; col++)
                {
                    writer.write(Integer.toString(image[row][col]));

                    //Add new line for every value but the last
                    if(row != image.length - 1 || col != image.length - 1)
                        writer.write("\n");
                }
            }

            writer.close();
        }
        catch(IOException e){e.printStackTrace();}

        System.out.println("Output file: " + path);
    }

    public static void main(String[] args)
    {
        if (args.length != 2) {
            System.out.println("Usage: java RITUncompress compressed.rit uncompressed.txt");
            System.exit(-1);
        }

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

        //Begins reading file
        System.out.println("Uncompressing: " + args[0]);

        //Error Check: Image is a square
        double tempSideLength = Math.sqrt(input.nextInt());
        int sideLength = (int) tempSideLength;
        if(tempSideLength != sideLength)
        {
            System.out.println("Image provided is not a square!");
            System.exit(-1);
        }

        //Reads and store file data
        ArrayList<Integer> tokens = readFile(input);

        //Converts arraylist into a quadtree structure and displays quadtree
        RITQTNode quadtree = parse(tokens);
        System.out.println("QTree: " + quadtree);

        //Uncompress quadtree into 2D image array and prints it out
        int[][] image = new int[sideLength][sideLength];
        image = quadtree.uncompress(image, sideLength);

        //Pixel array is written to file
        File file = new File(args[1]);
        writeImage(image, file);
    }
}