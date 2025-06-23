package dungeon.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class GameGUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlUrl = GameGUI.class.getResource("game_gui.fxml");
        if (fxmlUrl == null) {
            throw new IllegalStateException("Cannot find game_gui.fxml");
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        Controller controller = loader.getController();
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Mini Dungeon Game");
        
        controller.onSceneSet();
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}