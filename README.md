# 🖼️ Aplikacja do Przetwarzania Obrazów w JavaFX
Autor: Aleksandra Cieciorowska 275412

Uczelnia: Politechnika Wrocławska 🎓
## Cel projektu

Celem projektu było stworzenie graficznej aplikacji desktopowej umożliwiającej użytkownikowi:
* wczytywanie obrazów z dysku,
* wykonywanie podstawowych operacji przetwarzania obrazu (skalowanie, negatyw, progowanie, konturowanie),
* obracanie obrazu w lewo i w prawo,
* wyświetlanie oryginalnego i przetworzonego obrazu w graficznym interfejsie użytkownika.

 ## Technologie
  * Język programowania: Java
  * GUI: JavaFX

 ## Struktura projektu

Projekt podzielony został na trzy główne pakiety:

* app – zawiera klasę startową Main.
* app.controller – logika sterowania aplikacją (MainController).
* app.view – definiuje interfejs graficzny (MainView).

  
## Opis działania programu

### Klasa Main
Dziedziczy po klasie Application. Tworzy obiekt MainView, przekazuje go do MainController. Tworzy główną scenę (800x600 px) i wyświetla ją w oknie aplikacji.

### MainView – Interfejs graficzny
Zawiera komponenty:

* Pasek narzędzi: przycisk wczytywania obrazu, lista operacji, przycisk wykonania.
* Przycisk obracania w lewo/prawo.
* Dwa ImageView: dla obrazu oryginalnego i przetworzonego.
* Pasek stopki z informacją o autorze i uczelni.
* Logo aplikacji.
* Metoda showToast(String) – tymczasowe powiadomienia dla użytkownika.
  
### MainController – logika aplikacji
Obsługuje wszystkie działania użytkownika:

*  Wczytywanie obrazu

  - Otwiera okno wyboru pliku (FileChooser).
  - Wczytuje obraz do originalImage oraz currentImage.
  -  Aktywuje przyciski operacji.

### Operacje na obrazie

Wybierane z ComboBox, po kliknięciu przycisku „Wykonaj”.

- Skalowanie
Otwiera dialog z polami do podania nowych wymiarów (1–3000 px).
Tworzy nowy BufferedImage i skaluje oryginalny obraz.
Obsługuje przywrócenie pierwotnych wymiarów.
- Negatyw
Dla każdego piksela oblicza negatyw: 255 - kolor.
Tworzy nowy obraz w oparciu o zmodyfikowane wartości RGB.
- Progowanie
Przekształca obraz na czarno-biały (binaryzacja).
Użytkownik podaje próg (0–255).
Piksele jaśniejsze niż próg → biały; ciemniejsze → czarny.
- Konturowanie
Wykrywa krawędzie obrazu (prosty operator gradientu).
Porównuje intensywność bieżącego piksela z sąsiadami.
  - Obracanie

Przycisk „Obróć w lewo” lub „w prawo” obraca obraz o 90°.
Obrót realizowany poprzez Graphics2D.rotate.
## Interfejs użytkownika

Interfejs jest przejrzysty i intuicyjny:

- Operacje logicznie pogrupowane.
- Wyraźna separacja obrazu oryginalnego i przetworzonego.
- Komunikaty błędów i powodzeń informują użytkownika o stanie działania.
