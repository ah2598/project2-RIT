package ptui;

import model.RITQTNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class RITCompress
{
    /**
     * Given an arraylist of pixel values,
     * convert arraylist to a square 2d image array
     *
     * @param values the list of pixel values to be used
     * @return a 2D array of pixel values
     */
    private static int[][] listToImage(ArrayList<Integer> values)
    {
        //Error Check: Provided image is not a square
        double tempSideLength = Math.sqrt(values.size());
        int sideLength = (int) tempSideLength;
        if(tempSideLength != sideLength)
        {
            System.out.println("Image provided is not a square!");
            System.exit(-1);
        }

        int[][] image = new int[sideLength][sideLength];

        for(int row = 0; row < sideLength; row++)
        {
            for(int col = 0; col < sideLength; col++)
            {
                image[row][col] = values.remove(0);
            }
        }

        return image;
    }

    /**
     * Given an uncompressed image text file,
     * converts image into a list of pixel values.
     *
     * @param file Scanner containing file to read from
     * @return list containing each pixel value
     */
    private static ArrayList<Integer> readFile(Scanner file)
    {
        ArrayList<Integer> image = new ArrayList<Integer>();

        //Adds each pixel value to arraylist
        try
        {
            while(file.hasNext())
            {
                int val = Integer.parseInt(file.next());
                image.add(val);

                //Error Check: pixel value isn't in range 0-255
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

        return image;
    }

    public static void main(String[] args)
    {
        if (args.length != 2) {
            System.out.println("Usage: java RITCompress uncompressed-file.txt compressed-file.rit");
            System.exit(-1);
        }

        //Creates file reader for image
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

        //Reads and stores file
        System.out.println("Compressing: " + args[0]);
        ArrayList<Integer> pixels = readFile(input);

        //Converts list to image array
        int[][] image = listToImage(pixels);
        int sideLength = image.length;

        //Converts image array to RIT node
        RITQTNode quadtree = RITQTNode.compress(image, 0, 0, sideLength);
        System.out.println("QTree: " + quadtree.toString());
    }
}
