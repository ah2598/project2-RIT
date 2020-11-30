package gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class RITGUI extends Application
{
    /** Text object containing input file location*/
    private Text inputLocation = new Text("");

    /** Text object containing input file location*/
    private Text outputLocation = new Text("");

    /** Text object containing console output*/
    private Text consoleOutput = new Text("");

    /** GraphicsContext containing displayed image */
    private GraphicsContext graphicsContext = null;

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
        layout.setMinWidth(800);

        //Initializes fileChooser for input/output files
        FileChooser fileChooser = new FileChooser();

        //First Row: Various file operations
        HBox operations = new HBox();

        //Compressing
        Button compress = new Button("Compress");
        operations.getChildren().add(compress);

        //Decompressing
        Button decompress = new Button("Decompress");
        operations.getChildren().add(decompress);

        //View input file
        Button viewInput = new Button("View");
        operations.getChildren().add(viewInput);

        //Clear console output
        Button clear = new Button("Compress");
        operations.getChildren().add(clear);

        //Quit program
        Button quit = new Button("Quit");
        operations.getChildren().add(quit);

        //Second Row: Input file FileChooser
        HBox inputRow = new HBox();
        Button openInput = new Button("Set Input File");
        openInput.setOnAction((event) ->
                {
                    File file = fileChooser.showOpenDialog(stage);
                    inputLocation.setText(file.getAbsolutePath());
                }
        );
        inputRow.getChildren().add(openInput);
        inputRow.getChildren().add(inputLocation);
        inputRow.setSpacing(20);

        //Third Row: Output file FileChooser
        HBox outputRow = new HBox();
        Button openOutput = new Button("Set Output File");
        openOutput.setOnAction((event) ->
                {
                    File file = fileChooser.showOpenDialog(stage);
                    outputLocation.setText(file.getAbsolutePath());
                }
        );
        outputRow.getChildren().add(openOutput);
        outputRow.getChildren().add(outputLocation);
        outputRow.setSpacing(20);

        //Fourth row: Canvas to draw viewed image on
        Group canvasContainer = new Group();
        Canvas canvas = new Canvas(700, 700);
        graphicsContext = canvas.getGraphicsContext2D();
        canvasContainer.getChildren().add(canvas);

        //Fifth row: Text representing console output
        consoleOutput.setText("\n\n\n\n\n");

        //Adds rows to Vbox
        layout.getChildren().add(operations);
        layout.getChildren().add(inputRow);
        layout.getChildren().add(outputRow);
        layout.getChildren().add(canvasContainer);
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
