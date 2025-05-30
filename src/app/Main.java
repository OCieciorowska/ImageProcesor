package java.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.app.view.MainView;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        MainView mainView = new MainView();
        Scene scene = new Scene(mainView.getRoot(), 800, 600);
        primaryStage.setTitle("Image Processor - Politechnika Wrocławska");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
