package java.app.view;



import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.Node;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainView {
    private final BorderPane root;
    private final ComboBox<String> operationSelector;
    private final Button executeButton;
    private final Button loadButton;
    private final ImageView originalImageView;
    private final ImageView processedImageView;
    private final Label footer;

    public MainView() {
        root = new BorderPane();
        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(15));
        contentBox.setAlignment(Pos.TOP_CENTER);

        // Logo
        ImageView logo = null;
        try {
            logo = new ImageView(new Image(new FileInputStream("resources/logo.png")));
            logo.setFitHeight(80);
            logo.setPreserveRatio(true);
        } catch (FileNotFoundException e) {
            System.out.println("Brak logo.");
        }

        Label title = new Label("Image Processor");
        title.setFont(new Font("Arial", 24));

        Label welcome = new Label("Witaj w aplikacji do przetwarzania obrazów");
        welcome.setFont(new Font("Arial", 16));

        // Lista rozwijana
        operationSelector = new ComboBox<>();
        operationSelector.setPromptText("Wybierz operację");
        operationSelector.getItems().addAll("Skalowanie", "Konwersja do szarości", "Negatyw", "Progowanie");

        // Przyciski
        executeButton = new Button("Wykonaj");
        loadButton = new Button("Wczytaj obraz");

        // Obrazki
        originalImageView = new ImageView();
        originalImageView.setFitWidth(300);
        originalImageView.setPreserveRatio(true);
        processedImageView = new ImageView();
        processedImageView.setFitWidth(300);
        processedImageView.setPreserveRatio(true);

        HBox imageBox = new HBox(20, originalImageView, processedImageView);
        imageBox.setAlignment(Pos.CENTER);

        // Stopka
        footer = new Label("Autor: Jan Kowalski, Politechnika Wrocławska");
        footer.setFont(new Font(10));

        // Toasty – na razie tymczasowe (komunikaty terminalowe)
        executeButton.setOnAction(e -> {
            if (operationSelector.getValue() == null) {
                showToast("Nie wybrano operacji do wykonania");
            } else {
                showToast("Wykonywanie operacji: " + operationSelector.getValue());
            }
        });

        contentBox.getChildren().addAll(logo, title, welcome, operationSelector, executeButton, loadButton, imageBox);
        root.setCenter(contentBox);
        root.setBottom(footer);
        BorderPane.setAlignment(footer, Pos.CENTER);
        BorderPane.setMargin(footer, new Insets(10));
    }

    public BorderPane getRoot() {
        return root;
    }

    public void showToast(String message) {
        System.out.println("TOAST: " + message); // Na razie tylko konsola
    }

    public Button getLoadButton() {
        return loadButton;
    }

    public Button getExecuteButton() {
        return executeButton;
    }

    public ComboBox<String> getOperationSelector() {
        return operationSelector;
    }

    public ImageView getOriginalImageView() {
        return originalImageView;
    }

    public ImageView getProcessedImageView() {
        return processedImageView;
    }
}
