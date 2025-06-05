
package app.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;

public class MainView {
    //interfejs
    private final BorderPane root;
    private final ComboBox<String> operationSelector = new ComboBox<>();//lista operacji
    private final Button loadButton = new Button("Wczytaj obraz");
    private final Button executeButton = new Button("Wykonaj");
    private final Button rotateLeftButton = new Button("↺ Obroć w lewo");
    private final Button rotateRightButton = new Button("↻ Obroć w prawo");
    private final ImageView originalImageView = new ImageView();
    private final ImageView processedImageView = new ImageView();
    private final Label footer;

    //widok główny
    public MainView() {
        // stopka
        this.footer = new Label("© Politechnika Wrocławska - Aplikacja przetwarzania obrazów");
        footer.setPadding(new Insets(5));
        footer.setStyle("-fx-background-color: #f8f8f8; -fx-border-color: #e0e0e0; -fx-border-width: 1 0 0 0;");

        root = new BorderPane();

        // konfiguracja podglądu obrazów
        originalImageView.setPreserveRatio(true); //zachowanie proporcji
        originalImageView.setFitWidth(350);//stała szerokosc
        originalImageView.setFitHeight(350);//stała wysokość
        originalImageView.setStyle("-fx-border-color: #ccc; -fx-border-width: 1;");

        processedImageView.setPreserveRatio(true);
        processedImageView.setFitWidth(350);
        processedImageView.setFitHeight(350);
        processedImageView.setStyle("-fx-border-color: #ccc; -fx-border-width: 1;");

        // Inicjalizacja przycisków- domyślnie nieaktywne
        executeButton.setDisable(true);
        rotateLeftButton.setDisable(true);
        rotateRightButton.setDisable(true);

        //rozwjana lista z operacjami
        operationSelector.getItems().addAll("Skalowanie", "Negatyw", "Progowanie", "Konturowanie");
        operationSelector.setPromptText("Wybierz operację");

        // budowa interfejsu
        HBox header = createHeader();//header

        // pasek narzedzi
        HBox topBar = createToolbar();

        // obracanie-przyciski
        HBox rotateBox = createRotateButtons();

        // kontener na pasek narzedzi i header
        VBox topContainer = new VBox();
        topContainer.getChildren().addAll(header, topBar, rotateBox);

        // miejsce na obrazy
        HBox imageBox = createImageArea();

        root.setTop(topContainer);
        root.setCenter(imageBox);
        root.setBottom(footer);
    }


    //tworzenie nagłówka
    private HBox createHeader() {

        HBox header = new HBox(10);
        header.setPadding(new Insets(10));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #f8f8f8; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1 0;");

        Image logoImage = new Image(getClass().getResourceAsStream("/logo.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setPreserveRatio(true);
        logoView.setFitHeight(50);

        VBox appInfo = new VBox(5);
        Label appName = new Label("Image Processor");
        appName.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label author = new Label("Autor: Aleksandra Cieciorowska");
        author.setFont(Font.font("Arial", 12));

        appInfo.getChildren().addAll(appName, author);
        header.getChildren().addAll(logoView, appInfo);

        return header;
    }


    //pasek narzedzi
    private HBox createToolbar() {

        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getChildren().addAll(loadButton, operationSelector, executeButton);
        return topBar;
    }


    //przyciski obrotu
    private HBox createRotateButtons() {

        HBox rotateBox = new HBox(10);
        rotateBox.setPadding(new Insets(5, 10, 10, 10));
        rotateBox.setAlignment(Pos.CENTER_LEFT);
        rotateBox.getChildren().addAll(rotateLeftButton, rotateRightButton);
        return rotateBox;
    }


    //pole na obrazy
    private HBox createImageArea() {

        HBox imageBox = new HBox(10);
        imageBox.setPadding(new Insets(10));
        imageBox.setAlignment(Pos.CENTER);
        imageBox.getChildren().addAll(originalImageView, processedImageView);
        return imageBox;
    }

    // Gettery- używane przez kontroler
    public BorderPane getRoot() { return root; }
    public ComboBox<String> getOperationSelector() { return operationSelector; }
    public Button getLoadButton() { return loadButton; }
    public Button getExecuteButton() { return executeButton; }
    public Button getRotateLeftButton() { return rotateLeftButton; }
    public Button getRotateRightButton() { return rotateRightButton; }
    public ImageView getOriginalImageView() { return originalImageView; }
    public ImageView getProcessedImageView() { return processedImageView; }
    public Label getFooter() { return footer; }


    //wyświetlanie komunikatu
    public void showToast(String message) {
        //wyświetlanie komunikatu
        Popup popup = new Popup();
        Label label = new Label(message);
        label.setStyle("-fx-background-color: #444; -fx-text-fill: white; -fx-padding: 10;");
        popup.getContent().add(label);
        popup.setAutoHide(true);
        popup.show(root.getScene().getWindow());

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                if (popup.isShowing()) {
                    popup.hide();
                }
            } catch (InterruptedException ignored) {}
        }).start();
    }
}