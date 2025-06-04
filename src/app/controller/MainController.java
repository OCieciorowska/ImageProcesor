package app.controller;

import app.view.MainView;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class MainController {
    private final MainView view;//widok
    private Image originalImage;//orginalny obraz
    private Image currentImage;//przetwarzany obraz

    public MainController(MainView view) {
        this.view = view;
        setupHandlers();
    }

    private void setupHandlers() {
        //obsługa zdarzeń dla przycisków
        view.getLoadButton().setOnAction(e -> loadImage());
        view.getExecuteButton().setOnAction(e -> executeOperation());
        view.getRotateLeftButton().setOnAction(e -> rotateImage(-90));
        view.getRotateRightButton().setOnAction(e -> rotateImage(90));
    }

    private void loadImage() {
        //wczytywanie obrazu z dysku
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz obraz");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pliki graficzne", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                //wczytywanie obrazu z pliku

                originalImage = new Image(new FileInputStream(selectedFile));
                currentImage = originalImage;
                updateImageViews(); //aktualizacja podglądu
                enableImageOperations(true);//aktywacja przycisków
                view.showToast("Obraz wczytany pomyślnie");
            } catch (IOException e) {
                view.showToast("Błąd podczas wczytywania obrazu");
            }
        }
    }

    private void enableImageOperations(boolean enable) {
        //funkcja do włączania i wyłączania przycisków

        view.getExecuteButton().setDisable(!enable);
        view.getRotateLeftButton().setDisable(!enable);
        view.getRotateRightButton().setDisable(!enable);
    }

    private void updateImageViews() {
        //aktualizacja podglądu obrazu w interfejscie
        view.getOriginalImageView().setImage(originalImage);
        view.getProcessedImageView().setImage(currentImage);
    }

    private void executeOperation() {
        //wykonywanie wybranej operacji
        String operation = view.getOperationSelector().getValue();
        if (operation == null) {
            view.showToast("Wybierz operację z listy");
            return;
        }

        switch (operation) {
            //wybór operacji
            case "Skalowanie" -> showScaleDialog();
            case "Negatyw" -> applyNegative();
            case "Progowanie" -> showThresholdDialog();
            case "Konturowanie" -> applyContour();
            default -> view.showToast("Nieobsługiwana operacja");
        }
    }

    //Skalowanie obrazu
    private void showScaleDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Skalowanie obrazu");

        TextField widthField = new TextField();
        widthField.setPromptText("Szerokość (1–3000)");
        TextField heightField = new TextField();
        heightField.setPromptText("Wysokość (1–3000)");

        ButtonType resizeButton = new ButtonType("Zmień rozmiar", ButtonBar.ButtonData.OK_DONE);
        ButtonType restoreButton = new ButtonType("Przywróć oryginalny", ButtonBar.ButtonData.LEFT);
        dialog.getDialogPane().getButtonTypes().addAll(resizeButton, ButtonType.CANCEL, restoreButton);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Szerokość:"), 0, 0);
        grid.add(widthField, 1, 0);
        grid.add(new Label("Wysokość:"), 0, 1);
        grid.add(heightField, 1, 1);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == resizeButton) {
                try {
                    int width = Integer.parseInt(widthField.getText());
                    int height = Integer.parseInt(heightField.getText());

                    if (width < 1 || width > 3000 || height < 1 || height > 3000) {
                        view.showToast("Wymiary muszą być w zakresie 1-3000");
                        return null;
                    }

                    BufferedImage input = SwingFXUtils.fromFXImage(currentImage, null);
                    BufferedImage output = new BufferedImage(width, height, input.getType());

                    java.awt.Graphics2D g2d = output.createGraphics();
                    g2d.drawImage(input, 0, 0, width, height, null);
                    g2d.dispose();

                    currentImage = SwingFXUtils.toFXImage(output, null);
                    view.getProcessedImageView().setImage(currentImage);
                    view.showToast("Skalowanie zakończone sukcesem");
                } catch (NumberFormatException e) {
                    view.showToast("Wprowadź prawidłowe liczby");
                }
            } else if (dialogButton == restoreButton) {
                currentImage = originalImage;
                view.getProcessedImageView().setImage(currentImage);
                view.showToast("Przywrócono oryginalny rozmiar");
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void applyNegative() {
        try {
            // Konwersja obrazu JavaFX do BufferedImage (AWT)
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(currentImage, null);

            // Przetwarzanie każdego piksela
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                for (int y = 0; y < bufferedImage.getHeight(); y++) {
                    int rgba = bufferedImage.getRGB(x, y);
                    int a = (rgba >> 24) & 0xff;
                    int r = 255 - ((rgba >> 16) & 0xff);//negatyw dla czerwonego
                    int g = 255 - ((rgba >> 8) & 0xff);//negatyw dla zielonego
                    int b = 255 - (rgba & 0xff);//negatyw dla niebieskiegio
                    //złożenie kolorow
                    bufferedImage.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
                }
            }
            //konwesja z powrotem, aktualizacja widoku
            currentImage = SwingFXUtils.toFXImage(bufferedImage, null);
            view.getProcessedImageView().setImage(currentImage);
            view.showToast("Negatyw został wygenerowany pomyślnie!");
        } catch (Exception e) {
            view.showToast("Nie udało się wykonać negatywu.");
        }
    }

    //progowanie
    private void showThresholdDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Progowanie obrazu");
        dialog.initModality(Modality.APPLICATION_MODAL);

        TextField thresholdField = new TextField();
        thresholdField.setPromptText("Próg (0–255)");

        ButtonType executeBtn = new ButtonType("Wykonaj progowanie", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(executeBtn, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(new HBox(10, new Label("Próg:"), thresholdField));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == executeBtn) {
                try {
                    int threshold = Integer.parseInt(thresholdField.getText());
                    if (threshold < 0 || threshold > 255) {
                        view.showToast("Próg musi być w zakresie 0–255");
                        return null;
                    }

                    BufferedImage input = SwingFXUtils.fromFXImage(currentImage, null);
                    BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

                    for (int x = 0; x < input.getWidth(); x++) {
                        for (int y = 0; y < input.getHeight(); y++) {
                            int gray = (input.getRGB(x, y) >> 16) & 0xff;
                            int bin = gray < threshold ? 0 : 255;
                            int rgb = (255 << 24) | (bin << 16) | (bin << 8) | bin;
                            output.setRGB(x, y, rgb);
                        }
                    }

                    currentImage = SwingFXUtils.toFXImage(output, null);
                    view.getProcessedImageView().setImage(currentImage);
                    view.showToast("Progowanie zostało przeprowadzone pomyślnie!");
                } catch (Exception e) {
                    view.showToast("Nie udało się wykonać progowania.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    //wykrywanie konturu na obrazie
    private void applyContour() {
        try {
            BufferedImage input = SwingFXUtils.fromFXImage(currentImage, null);
            BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

            //przetwarzanie pikseli bez brzegów
            for (int x = 1; x < input.getWidth() - 1; x++) {
                for (int y = 1; y < input.getHeight() - 1; y++) {
                    int gray = (input.getRGB(x, y) >> 16) & 0xff;//aktualny piksel
                    int grayX = (input.getRGB(x + 1, y) >> 16) & 0xff;//sąsiad poziomy
                    int grayY = (input.getRGB(x, y + 1) >> 16) & 0xff;//sąsiad pionowy
                    //różnica- wykrycie krawędzi
                    int edge = Math.abs(gray - grayX) + Math.abs(gray - grayY);
                    edge = Math.min(255, edge);//ograniczenie zakresu
                    int rgb = (255 << 24) | (edge << 16) | (edge << 8) | edge;
                    output.setRGB(x, y, rgb);
                }
            }

            currentImage = SwingFXUtils.toFXImage(output, null);
            view.getProcessedImageView().setImage(currentImage);
            view.showToast("Konturowanie zostało przeprowadzone pomyślnie!");
        } catch (Exception e) {
            view.showToast("Nie udało się wykonać konturowania.");
        }
    }

    private void rotateImage(double angle) {
        try {
            BufferedImage input = SwingFXUtils.fromFXImage(currentImage, null);
            int width = input.getWidth();
            int height = input.getHeight();

            BufferedImage output;
            // Dla obrotów niebędących wielokrotnością 180 stopni zamieniamy szerokość i wysokość
            if (angle % 180 != 0) {
                output = new BufferedImage(height, width, input.getType());
            } else {
                output = new BufferedImage(width, height, input.getType());
            }

            //obrót
            java.awt.Graphics2D g2d = output.createGraphics();
            g2d.rotate(Math.toRadians(angle), output.getWidth() / 2.0, output.getHeight() / 2.0);
            g2d.drawImage(input, (output.getWidth() - width) / 2, (output.getHeight() - height) / 2, null);
            g2d.dispose();

            currentImage = SwingFXUtils.toFXImage(output, null);
            view.getProcessedImageView().setImage(currentImage);
            view.showToast("Obrót wykonany pomyślnie");
        } catch (Exception e) {
            view.showToast("Nie udało się obrócić obrazu");
        }
    }
}