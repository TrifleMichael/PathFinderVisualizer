package App;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static javafx.geometry.Pos.CENTER;

public class SideMenu {
    Main parent;
    Slider obstructionSlider;
    Slider xSize;
    Slider ySize;

    public SideMenu(VBox stage, Main parent, double panelX, double panelY) {
        this.parent = parent;


        stage.setPrefSize(panelX, panelY);
        stage.setAlignment(CENTER);
        stage.setSpacing(15);

        stage.getChildren().add(new Text("Percentage of obstructions"));
        Slider obstructionSlider = new Slider();
        obstructionSlider.setMin(0);
        obstructionSlider.setMax(100);
        obstructionSlider.setValue(40);
        obstructionSlider.setShowTickLabels(true);
        obstructionSlider.setShowTickMarks(true);
        obstructionSlider.setMajorTickUnit(50);
        obstructionSlider.setMinorTickCount(5);
        obstructionSlider.setBlockIncrement(10);
        this.obstructionSlider = obstructionSlider;
        stage.getChildren().add(obstructionSlider);


        Button refreshButton = new Button("Refresh grid");
        EventHandler<ActionEvent> refreshGrid = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                parent.passRefreshSignal();
            }
        };
        refreshButton.setLayoutX(panelX/2-30);
        refreshButton.setOnAction(refreshGrid);
        refreshButton.setAlignment(Pos.CENTER);
        stage.getChildren().add(refreshButton);



        Button startDijkstraAnimation = new Button("Dijkstra");
        EventHandler<ActionEvent> playDijkstra = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                parent.startDijkstra();
            }
        };
        startDijkstraAnimation.setLayoutX(panelX/2-30);
        startDijkstraAnimation.setOnAction(playDijkstra);
        startDijkstraAnimation.setAlignment(Pos.CENTER);
        stage.getChildren().add(startDijkstraAnimation);





        Button startDFS = new Button("DFS");
        EventHandler<ActionEvent> playDFS = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                parent.startDFS();
            }
        };
        startDFS.setLayoutX(panelX/2-30);
        startDFS.setOnAction(playDFS);
        startDFS.setAlignment(Pos.CENTER);
        stage.getChildren().add(startDFS);



        Button DFSLabyrinth = new Button("DFS Labyrinth");
        EventHandler<ActionEvent> playDFSLabyrinth = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                parent.startDFS();
            }
        };
        DFSLabyrinth.setLayoutX(panelX/2-30);
        DFSLabyrinth.setOnAction(playDFSLabyrinth);
        DFSLabyrinth.setAlignment(Pos.CENTER);
        stage.getChildren().add(DFSLabyrinth);




        Button pauseButton = new Button("Pause");
        EventHandler<ActionEvent> pause = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                parent.togglePause();

                if (pauseButton.getText().equals("Pause"))
                    pauseButton.setText("Unpause");
                else
                    pauseButton.setText("Pause");
            }
        };
        pauseButton.setLayoutX(panelX/2-30);
        pauseButton.setOnAction(pause);
        pauseButton.setAlignment(Pos.CENTER);
        stage.getChildren().add(pauseButton);



        stage.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));


        stage.getChildren().add(new Text("Change X size."));
        xSize = new Slider();
        xSize.setMin(5);
        xSize.setMax(100);
        xSize.setValue(40);
        xSize.setShowTickLabels(true);
        xSize.setShowTickMarks(true);
        xSize.setMajorTickUnit(50);
        xSize.setMinorTickCount(5);
        xSize.setBlockIncrement(10);
        stage.getChildren().add(xSize);


        stage.getChildren().add(new Text("Change Y size."));
        ySize = new Slider();
        ySize.setMin(5);
        ySize.setMax(100);
        ySize.setValue(40);
        ySize.setShowTickLabels(true);
        ySize.setShowTickMarks(true);
        ySize.setMajorTickUnit(50);
        ySize.setMinorTickCount(5);
        ySize.setBlockIncrement(10);
        stage.getChildren().add(ySize);


        stage.getChildren().add(new Text("Legend:\n"+ColorPalette.legend()));

    }

    public double getObstructionValue() {
        return obstructionSlider.getValue();
    }

    public int getXSize() {
        return (int)xSize.getValue();
    }

    public int getYSize() {
        return (int)ySize.getValue();
    }
}
