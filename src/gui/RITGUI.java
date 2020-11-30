package gui;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.RITQTNode;
import ptui.RITCompress;
import ptui.RITUncompress;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RITGUI extends Application
{
    /** The input file to read from */
    private File inputFile = null;

    /** The output file to write to */
    private File outputFile = null;

    /** Text object containing console output */
    private Text consoleOutput = new Text("");

    /** Canvas containing displayed image */
    private Canvas canvas = new Canvas(512, 512);

    /**
     * Returns the operations this program can perform.
     * Compressing, decompressing, image viewing are the main operations.
     * Resetting program to default state and quitting the program are side operations.
     *
     * @return HBox containing various program operations
     */
    public HBox createOperations()
    {
        HBox operations = new HBox();
        operations.setSpacing(10);
        operations.getChildren().add(new Text("     File Operations:     "));

        //Compressing
        Button compress = new Button("Compress");
        compress.setOnAction((event) ->
                {
                    System.out.print("");
                }
        );
        operations.getChildren().add(compress);

        //Decompressing
        Button decompress = new Button("Decompress");
        decompress.setOnAction((event) ->
                {
                    System.out.print("");
                }
        );
        operations.getChildren().add(decompress);

        //View input file
        Button viewInput = new Button("View");
        viewInput.setOnAction((event) ->
                {
                    try
                    {
                        //Reads and converts file to a list of pixels
                        Scanner fileReader = new Scanner(inputFile);

                        ArrayList<Integer> pixels = RITCompress.readFile(fileReader);

                        //Draws pixels onto canvas
                        RITViewer.drawImage(canvas.getGraphicsContext2D(), pixels, (int)Math.sqrt(pixels.size()));
                        consoleOutput.setText("Viewing: " + inputFile.getCanonicalPath());
                    }
                    catch (FileNotFoundException e) { e.printStackTrace(); }
                    catch (IOException e) { e.printStackTrace(); }
                }
        );
        operations.getChildren().add(viewInput);

        //Clear console output
        Button clear = new Button("Clear");
        clear.setOnAction((event) ->
                {
                    inputFile = null;
                    outputFile = null;
                    consoleOutput.setText("\n\n\n\n\n");
                    canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                }
        );
        operations.getChildren().add(clear);

        //Quit program
        Button quit = new Button("Quit");
        quit.setOnAction((event) -> System.exit(0));
        operations.getChildren().add(quit);

        return operations;
    }

    /**
     * Creates the scene layout of application.
     * First row: Various RITGUI operations
     * Second row: Input file FileChooser
     * Third row: Output file FileChooser
     * Fourth row: Fileviewer
     * Fifth row: Console output
     *
     * @param stage application stage
     * @return VBox containing RITGUI layout
     */
    public VBox createLayout(Stage stage)
    {
        VBox layout = new VBox();
        layout.setMinWidth(650);

        //Initializes fileChooser for input/output files
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        //First Row: Various file operations
        HBox operations = createOperations();

        //Second Row: Input file FileChooser
        HBox inputRow = new HBox();
        Button openInput = new Button("Set Input File");
        Text inputLocation = new Text("");
        openInput.setPrefWidth(100);
        openInput.setOnAction((event) ->
            {
                fileChooser.setTitle("Choose input file.");
                inputFile = fileChooser.showOpenDialog(stage);
                if(inputFile != null)
                    inputLocation.setText(inputFile.getAbsolutePath());
            }
        );
        inputRow.getChildren().add(openInput);
        inputRow.getChildren().add(new Separator(Orientation.VERTICAL));
        inputRow.getChildren().add(inputLocation);
        inputRow.setSpacing(20);

        //Third Row: Output file FileChooser
        HBox outputRow = new HBox();
        Button openOutput = new Button("Set Output File");
        Text outputLocation = new Text("");
        openOutput.setPrefWidth(100);
        openOutput.setOnAction((event) ->
                {
                    fileChooser.setTitle("Choose output file.");
                    outputFile = fileChooser.showSaveDialog(stage);
                    if(outputFile != null)
                        outputLocation.setText(outputFile.getAbsolutePath());
                }
        );
        outputRow.getChildren().add(openOutput);
        outputRow.getChildren().add(new Separator(Orientation.VERTICAL));
        outputRow.getChildren().add(outputLocation);
        outputRow.setSpacing(20);

        //Fourth row: Canvas to draw viewed image on
        Group canvasContainer = new Group();
        canvasContainer.getChildren().add(canvas);

        //Fifth row: Text representing console output
        consoleOutput.setText("\n\n\n\n\n");

        //Adds rows to Vbox
        layout.getChildren().add(operations);
        layout.getChildren().add(new Separator(Orientation.HORIZONTAL));
        layout.getChildren().add(inputRow);
        layout.getChildren().add(outputRow);
        layout.getChildren().add(new Separator(Orientation.HORIZONTAL));
        layout.getChildren().add(canvasContainer);
        layout.getChildren().add(new Separator(Orientation.HORIZONTAL));
        layout.getChildren().add(consoleOutput);

        return layout;
    }

    @Override
    public void start(Stage stage)
    {
        //Creates the scene's layout
        VBox controls = createLayout(stage);

        //Displays screen to application
        stage.setTitle("RITGUI");
        stage.setScene(new Scene(controls));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args)
    {
        Application.launch(args);
    }
}
