package app;

import app.controller.MainController;
import app.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainView view = new MainView();//tworze widok
        new MainController(view); //tworzenie kontrolera i przekazanie mu widoku

        Scene scene = new Scene(view.getRoot(), 800, 600);
        primaryStage.setTitle("Aplikacja do przetwarzania obrazów");
        primaryStage.setScene(scene);
        primaryStage.show();//wyswietlanie okna
    }

    public static void main(String[] args) {
        launch(args);//uruchomienie apki
    }
}

