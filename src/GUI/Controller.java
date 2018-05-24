package GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.GridPane;
import pl.sdacademy.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {
    @FXML
    Button btnSolve;
    @FXML
    Button generate;
    @FXML
    TextField name;
    @FXML
    Button B1;
    @FXML
    Button B2;
    @FXML
    Button B3;
    @FXML
    Button B4;
    @FXML
    Label infoField;
    @FXML
    GridPane grid;

    private Maze mazeOrigin;
    private Point startpoint;
    private Point myPoint;
    PointType[][] points;
    TextField[][] pieces;

    public void initialize(){
        btnSolve.setDisable(true);
        B1.setDisable(true);
        B2.setDisable(true);
        B3.setDisable(true);
        B4.setDisable(true);
        generate.setOnAction(event -> {
            clearMaze();
            try {
                mazeOrigin = new Maze(name.getText()+".txt");
                points = mazeOrigin.getPoints();
                myPoint = mazeOrigin.getStartPoint();
                pieces = new TextField[points.length][points.length];
                displayMaze();
                btnSolve.setDisable(false);
                B1.setDisable(false);
                B2.setDisable(false);
                B3.setDisable(false);
                B4.setDisable(false);
                infoField.getStyleClass().remove("bad");
                infoField.getStyleClass().add("good");
                infoField.setText("The maze was correctly loaded");
            } catch (MazeCreationException e) {
                infoField.getStyleClass().remove("good");
                infoField.getStyleClass().add("bad");
                infoField.setText("Error reading the file");
            }

        });
        B1.setOnAction(event -> {
            move(0,-1);
        });
        B2.setOnAction(event -> {
            move(-1,0);
        });
        B3.setOnAction(event -> {
            move(0,1);
        });
        B4.setOnAction(event -> {
            move(1,0);
        });
        btnSolve.setOnAction(event -> {
            solve();
        });
    }

    private void clearMaze() {
        if (pieces != null){
            for (int i = 0; i < pieces.length; i++) {
                for (int j = 0; j < pieces[0].length; j++) {
                    grid.getChildren().remove(pieces[j][i]);
                }
            }

        }
        pieces = null;
        points = null;
    }

    private void solve() {
        btnSolve.setDisable(true);
        generate.setDisable(true);
        B1.setDisable(true);
        B2.setDisable(true);
        B3.setDisable(true);
        B4.setDisable(true);
        MazeSolver mazeSolver = new MazeSolver(mazeOrigin,myPoint);
        List<Point> list ;
        list = mazeSolver.solve();

            showMyPoint();
            new Thread(() ->
                    list.forEach(s -> {
                        myPoint = s;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(() -> showMyPoint());
                    })).start();
    }

    private void move(int x,int y){
        if (myPoint.getY()+y >= 0 && myPoint.getY()+y < pieces.length &&
            myPoint.getX()+x >= 0 && myPoint.getX()+x < pieces.length &&
            points[myPoint.getY()+y][myPoint.getX()+x]!=PointType.WALL){
            pieces[myPoint.getY()][myPoint.getX()].getStyleClass().remove("point");
            myPoint = new Point(myPoint.getX()+x,myPoint.getY()+y);
            showMyPoint();
        }
    }
    private void showMyPoint(){
            pieces[myPoint.getY()][myPoint.getX()].getStyleClass().add("point");
            if(myPoint.equals(mazeOrigin.getEndPoint())){
                infoField.setText("You reached the end of the maze");
                generate.setDisable(false);
                btnSolve.setDisable(true);
                B1.setDisable(true);
                B2.setDisable(true);
                B3.setDisable(true);
                B4.setDisable(true);
            }

    }


    private void displayMaze(){
        for (int i = 0;i<points.length;i++){
            for (int j = 0; j<points.length;j++){
                TextField textField = new TextField();
                textField.setMinWidth(20);
                textField.setMinHeight(20);
                textField.setPrefSize(20,20);
                pieces[i][j] = textField;
                if (points[i][j] == PointType.WALL){
                    textField.getStyleClass().add("wall");
                    grid.add(pieces[i][j],j,i);
                }else if(points[i][j] == PointType.PATH){
                    textField.getStyleClass().add("path");
                    grid.add(pieces[i][j],j,i);
                }else if (points[i][j] == PointType.START){
                    textField.getStyleClass().add("start");
                    grid.add(pieces[i][j],j,i);
                }else if (points[i][j] == PointType.END){
                    textField.getStyleClass().add("end");
                    grid.add(pieces[i][j],j,i);
                }
            }
        }
        showMyPoint();
    }
}
