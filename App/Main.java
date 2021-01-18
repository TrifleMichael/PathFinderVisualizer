package App;

import Grid.Simulation;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {
    Simulation simulation;

    AnimationState state = AnimationState.WAITING;
    Boolean paused = false;
    SideMenu sideMenu;

    @Override
    public void start(Stage primaryStage) throws Exception{
        double winX = 900;
        double winY = 600;
        double menuOffset = 600;

        HBox root = new HBox();
        primaryStage.setTitle("Search Algorithms Visualisation");
        primaryStage.setScene(new Scene(root, winX, winY));
        primaryStage.show();

        VBox menuStage = new VBox();
        menuStage.setPrefWidth(winX-menuOffset);
        sideMenu = new SideMenu(menuStage, this, winX-menuOffset, winY);

        Pane simulationView = new Pane();
        simulationView.setPrefSize(menuOffset, winY);

        root.getChildren().addAll(simulationView, menuStage);

        simulation = new Simulation(this, simulationView, 50, 50, menuOffset, winY);



        EventHandler<MouseEvent> mousePressed = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                simulation.showVerticeInfo(e.getX(), e.getY());
            }
        };
        simulationView.addEventFilter(MouseEvent.MOUSE_CLICKED, mousePressed);


        Rectangle syncRect = new Rectangle(0,0,1,1);
        TranslateTransition syncAnim = new TranslateTransition(Duration.millis(10));
        syncAnim.setNode(syncRect);
        syncAnim.setOnFinished(e -> playTick(syncAnim));
        syncAnim.play();

    }

    public void passRefreshSignal() {
        simulation.refreshSignal(sideMenu.getXSize(), sideMenu.getYSize(), sideMenu.getObstructionValue()/100);
        state = AnimationState.WAITING;
    }

    public void startDijkstra() {
        if (state == AnimationState.ANIMATIONENDED)
            simulation.refreshSignal(sideMenu.getXSize(), sideMenu.getYSize(), sideMenu.getObstructionValue()/100);
        state = AnimationState.DIJKSTRA;
        simulation.startDijkstra();
    }

    public void startDFS() {
        if (state == AnimationState.ANIMATIONENDED)
            simulation.refreshSignal(sideMenu.getXSize(), sideMenu.getYSize(), sideMenu.getObstructionValue()/100);
        state = AnimationState.DFS;
        simulation.startDFS();
    }

    public void startDFSLabyrinth() {
        if (state == AnimationState.ANIMATIONENDED)
            simulation.refreshSignal(sideMenu.getXSize(), sideMenu.getYSize(), sideMenu.getObstructionValue()/100);
        state = AnimationState.DFSLABYRINTH;
        simulation.startDFSLabyrinth();

    }

    public void playTick(TranslateTransition syncAnimation) {
        syncAnimation.play();
        if (paused) return;

        switch(state) {
            case DFS:
                if(!simulation.playDFSStep())
                    state = AnimationState.FINALPATH;
                break;

            case DIJKSTRA:
                simulation.playDijkstraStep();
                break;

            case FINALPATH:
                if(!simulation.playFinalPathStep())
                    state = AnimationState.ANIMATIONENDED;
                break;

            case DFSLABYRINTH:
                if(!simulation.playDFSStep())
                    state = AnimationState.ANIMATIONENDED;
                break;

            default:
                break;

        }
    }

    public void stoppedPlayingDijkstra() {
        state = AnimationState.WAITING;
    }


    public void showFinalPath() {
        state = AnimationState.FINALPATH;
    }

    public void togglePause() {
        paused = !paused;
    }

    public static void main(String[] args) {
        launch(args);
    }


}
