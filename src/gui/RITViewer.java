package gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class RITViewer extends Application
{
    /** The side length of the 2d image*/
    private int sideLength;

    /** The pixel values of provided image*/
    private ArrayList<Integer> pixels = new ArrayList<Integer>();

    /***
     * Displays given image to the user.
     *
     * @param stage The stage to display to user
     */
    @Override
    public void start(Stage stage)
    {
        //Arbitrarily generates image
        for(int i = 0; i < Math.pow(2,15); i++)
            pixels.add(120);
        sideLength = (int)Math.sqrt(pixels.size());

        //Sets the scene to the image array
        Group root = new Group();
        Canvas canvas = new Canvas(500, 500);
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
        Application.launch(args);
    }
}
