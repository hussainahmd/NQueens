package main;

import java.io.*;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

public class Driver extends Application{

    private static int N = 4; //initial value
    private static int[][] board;
    private static int totalSolutions;
    private static int currentSolution;

    //list for storing solutions in 2D arrays
    private static ArrayList<int[][]> list = new ArrayList<>();

    private BorderPane bPane;
    private GridPane gPane;
    private HBox hBoxTop, hBoxBottom;
    private ChoiceBox<Integer> choiceBox;
    private Background bg, bg2, bg3;
    private ColumnConstraints columnConstraints;
    private RowConstraints rowConstraints;
    private Label l1, l2;
    private Button prevButton, nextButton;
    private Image image1, image2, image3;

    @Override
    public void start(Stage stage) throws IOException {

        bg = new Background(new BackgroundFill(Color.LIGHTGRAY, null, null));
        bg2 = new Background(new BackgroundFill(Color.LIGHTYELLOW, null, null));
        bg3 = new Background(new BackgroundFill(Color.LIGHTCYAN, null, null));

        String s = System.getProperty("user.dir");
        System.out.println("DIR : " + s);

        //image1 = new Image(new FileInputStream("src/resources/q3.png"));
        //using resourceAsStream for input of images instead of above because
        //FileInputStream looks for the file from the root directory of the computer whereas
        //resourceAsStream gets the file from the current parent directory. This approach is
        //necessary for the working as jar file.
        try (InputStream inputStream3 = getClass().getResourceAsStream("/resources/q3.png");
             InputStream inputStream2 = getClass().getResourceAsStream("/resources/q2.png");
             InputStream inputStream1 = getClass().getResourceAsStream("/resources/q1.png");){

            assert inputStream3 != null && inputStream2 != null && inputStream1 != null;
            image1 =  SwingFXUtils.toFXImage(ImageIO.read(inputStream3), null);
            image2 =  SwingFXUtils.toFXImage(ImageIO.read(inputStream2), null);
            image3 =  SwingFXUtils.toFXImage(ImageIO.read(inputStream1), null);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //Column constraints for auto resizing of grid pane
        columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(100);
        rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100);

        choiceBox = new ChoiceBox<>();
        choiceBox.getItems().add(4);
        choiceBox.getItems().add(5);
        choiceBox.getItems().add(6);
        choiceBox.getItems().add(7);
        choiceBox.getItems().add(8);
        choiceBox.setValue(N);
        choiceBox.setFocusTraversable(false);
        choiceBox.setMinSize(50,35);

        choiceBox.setOnAction(e->{

            bPane.setBottom(hBoxBottom);
            drawGrid();
        });

        Label label = new Label("Select the value of N");
        label.setFont(Font.font(15));
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);

        hBoxTop = new HBox(20, label, choiceBox);
        hBoxTop.setAlignment(Pos.CENTER);
        hBoxTop.setBackground(bg);
        hBoxTop.setMinHeight(50);

        Button runButton = new Button("Run");
        runButton.setFont(Font.font(null, FontWeight.SEMI_BOLD, 15));
        runButton.setMinWidth(100);
        runButton.setMinHeight(35);

        hBoxBottom = new HBox(runButton);
        hBoxBottom.setAlignment(Pos.CENTER);
        hBoxBottom.setBackground(bg);
        hBoxBottom.setMinHeight(50);

        prevButton = new Button("Prev");
        prevButton.setFont(Font.font(null, FontWeight.SEMI_BOLD, 15));
        prevButton.setMinWidth(60);
        prevButton.setMinHeight(35);

        nextButton = new Button("Next");
        nextButton.setFont(Font.font(null, FontWeight.SEMI_BOLD, 15));
        nextButton.setMinWidth(60);
        nextButton.setMinHeight(35);

        l1 = new Label();
        l1.setFont(Font.font(15));
        l1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        l1.setAlignment(Pos.CENTER);

        l2 = new Label();
        l2.setFont(Font.font(15));
        l2.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        l2.setAlignment(Pos.CENTER);

        HBox hBoxBottom2 = new HBox(20, l1, l2, prevButton, nextButton);
        hBoxBottom2.setAlignment(Pos.CENTER);
        hBoxBottom2.setBackground(bg);
        hBoxBottom2.setMinHeight(50);

        runButton.setOnAction(e->{
            list.clear();
            board = new int[N][N];
            N_Queens(0);
            totalSolutions = list.size();
            currentSolution = 1;
            bPane.setBottom(hBoxBottom2);
            prevButton.setDisable(false);
            nextButton.setDisable(false);
            displaySolution();
        });

        nextButton.setOnAction(e->{
            currentSolution++;
            if(currentSolution == 2)
                prevButton.setDisable(false);
            if(currentSolution == totalSolutions)
                nextButton.setDisable(true);
            drawGrid();
            displaySolution();
        });

        prevButton.setOnAction(e->{
            currentSolution--;
            if(currentSolution == totalSolutions - 1)
                nextButton.setDisable(false);
            if(currentSolution == 1)
                prevButton.setDisable(true);
            drawGrid();
            displaySolution();
        });

        bPane = new BorderPane();
        bPane.setTop(hBoxTop);
        bPane.setBottom(hBoxBottom);
        drawGrid();

        Scene scene = new Scene(bPane, 900, 720);
        stage.setScene(scene);
        stage.setTitle("NQueens");
        stage.setMinHeight(scene.getHeight());
        stage.setMinWidth(scene.getWidth());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    void displaySolution(){

        Image image;
        if(N == 7 || N == 6)
            image = image2;
        else if (N == 8)
            image = image3;
        else
            image = image1;

        int x = 0;
        for (int i = 0; i < N; i++) {

            for (int j = 0; j < N; j++) {

                int val = list.get(currentSolution - 1)[j][i];
                if(val == 1){
                    Label l = (Label)gPane.getChildren().get(x);
                    l.setGraphic((new ImageView(image)));
                }
                x++;
            }
        }

        l1.setText(String.format("Total Solutions: %d", totalSolutions));
        if(currentSolution == 1)
            prevButton.setDisable(true);
        l2.setText("Current solution: " + currentSolution + " / " + totalSolutions);
    }
    void drawGrid() {

        N = choiceBox.getValue();
        gPane = new GridPane();
        bPane.setCenter(gPane);

        int x = 0;
        for (int i = 0; i < N; i++) {

            for (int j = 0; j < N; j++) {

                Label l = new Label();
                l.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                l.setAlignment(Pos.CENTER);

                if(x % 2 == 0)
                    l.setBackground(bg3);
                else
                    l.setBackground(bg2);
                x++;
                if(N % 2 == 0 && j == N - 1)
                    x++;

                gPane.add(l, i, j);
            }
        }

        for(int a = 0; a < N; a++){
            gPane.getColumnConstraints().add(columnConstraints);
            gPane.getRowConstraints().add(rowConstraints);
        }
        gPane.setAlignment(Pos.CENTER);
        gPane.setGridLinesVisible(true);
        gPane.setPadding(new Insets(10, 10, 10, 10));
        gPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    //go to the depth of the board while checking a solution for all rows and columns
    void N_Queens(int row) {

        //a solution has been found
        //add the board into the list
        if (row == N)
        {
            int[][] solution = new int[N][N];
            for(int i = 0; i < N; i++)]
                solution[i] =  board[i].clone();
            //using a new 2d array object instead of original board array
            list.add(solution);
        }
        else
        {
            //check for all the columns of the same row
            for (int col = 0; col < N; col++)
            {
                if (isSafe(row, col))
                {
                    board[row][col] = 1; //place a queen
                    N_Queens(row + 1); //one complete possibility is checked
                    board[row][col] = 0; //replace the cell with 0 to check further solutions
                }
            }
        }
    }

    boolean isSafe(int row, int col) {

        int col_Left = col;
        int col_Right = col;
        while (row >= 0)
        {
            //check the left upper diagonal cell
            if (col_Left >= 0 && board[row][col_Left] == 1)
                return false;

            //check the top cell
            if (board[row][col] == 1)
                return false;

            //check the right upper diagonal cell
            if (col_Right < N && board[row][col_Right] == 1)
                return false;

            row --; //go to previous row
            col_Left --;
            col_Right ++;
        }
        //return true if no threat
        return true;
    }
}
