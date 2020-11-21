package gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RITViewer extends Application
{
    /** The side length of the 2d image*/
    private int sideLength;

    /** The pixel values of provided image*/
    private ArrayList<Integer> pixels = new ArrayList<Integer>();

    /**
     * Reads in the provided square grayscale image.
     * The image is then stored into an array.
     */
    @Override
    public void init() throws Exception {
        super.init();

        try
        {
            //Creates FileReader for provided file in arguments
            List<String> args = getParameters().getRaw();
            Scanner file = new Scanner(new File(args.get(0)));

            //Reads in all pixel values
            while (file.hasNextInt())
            {
                int val = Integer.parseInt(file.next());
                pixels.add(val);

                //Error Check: Pixel value isn't in range 0-255
                if(val < 0 || val > 255)
                {
                    System.out.println("Image file contains invalid pixel value! (Valid pixel value: 0-255)");
                    System.exit(-1);
                }
            }

            //Determines image dimension
            sideLength = (int) Math.sqrt(pixels.size());
        }
        //Error Check: The provided file cannot be found
        catch(FileNotFoundException e)
        {
            System.out.println("Program cannot find the file specified!");
            System.exit(-1);
        }
        //Error Check: Image file contains non-int value\
        catch(NumberFormatException e)
        {
            System.out.println("Image file contains a non-integer!");
            System.exit(-1);
        }
    }

    /***
     * Displays given image to the user.
     *
     * @param stage The stage to display to user
     */
    @Override
    public void start(Stage stage) throws Exception
    {
        //Sets the scene to the image array
        Group root = new Group();
        Canvas canvas = new Canvas(sideLength, sideLength);
        drawImage(canvas.getGraphicsContext2D(), pixels, sideLength);
        root.getChildren().add(canvas);

        //Displays the scene in application
        stage.setTitle("Quadtree");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Draws a square image using given pixel values
     *
     * @param gc The canvas to draw pixels on
     * @param pixels The grayscale pixel values to use
     * @param length The side length of square image
     */
    public static void drawImage(GraphicsContext gc, ArrayList<Integer> pixels, int length)
    {
        //Draws each pixel to its assigned location
        for(int row = 0; row < length; row++)
        {
            for(int col = 0; col < length; col++)
            {
                int pixel = pixels.get(row * length + col);
                Color color = Color.rgb(pixel, pixel, pixel);
                gc.setFill(color);
                gc.setStroke(color);
                gc.fillRect(col, row, col, row);
            }
        }
    }

    public static void main(String[] args)
    {
        //Ensure that there are program arguments
        if(args.length == 0)
        {
            System.out.println("Usage: java RITViewer uncompressed.txt");
            System.exit(-1);
        }

        Application.launch(args);
    }
}
