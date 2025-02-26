# traffic_lights_system


## Opis projektu
**Traffic Lights System** to aplikacja symulująca działanie sygnalizacji świetlnej na skrzyżowaniach. Projekt ma na celu edukację w zakresie programowania systemów czasu rzeczywistego oraz zarządzania ruchem drogowym.

## Technologie
Projekt został zrealizowany z wykorzystaniem:
- **Java** – wersja 21
- **Gradle** – do automatyzacji budowania projektu

## Opis struktury repozytorium
### W [traffic_lights_system/src/main/java/org/example](https://github.com/FloudMe77/traffic_lights_system/tree/main/src/main/java/org/example) znajdują się klasy projektu:
- W folderze IOHandlers znajdują się klasy obsługujące:
    - Czytanie pliku wejściowego (FileReaderIterator)
    - Zapisywanie wyjścia programu w pliku json (JsonStepLogger)
    - Pomocniczy rekord reprezentujący polecenie na wejściu (Command)
- W folderze LightStrategy znajdują się różne sposoby zarządzania systemem świateł
    - (ILightSystem) to interface dla strategii
    - (SimpleILightMode) prosta strategia działająca na bazie timera, nie biorąca pod uwagę ilości aut na pasach
    - (GreedyLightMode) strategia, która znajduje układ świateł maksymalizujący priorytet każdego z pasów na skrzyżowaniu i odpowiadająca, czy należy zmienić aktualny stan świateł
    - (OptymalizedLightMode) uproszczona (GreedyLightMode) bez obsługi świateł i wybierająca najbardziej oblegany pas jako potrzebny do udrożenienia w następnych cyklach świateł
- W folderze SimulationTools znajdują się klasy odpowiedzialne za przeprowadzenie symulacji ruchu samochodów i pieszych na skrzyżowaniu:
    - (Simulation) główna klasa przeprowadzająca symulacje
    - (SignalColor) enum reprezentujący kolor sygnalizatora
    - (LightPrinter) klasa wypisująca aktualny stan świateł na skrzyżowaniu
    - (LightChanger) klasa odpowiedzialna za zmiane świateł dla pasów (wraz ze strzałkami warunkowymi i światłami dla pieszych)
    Pakiet dodatkowo zawiera podpakiet SimulationUtils, który przechowuje klasy pomocnicze symulacji
    - (Direction) enum reprezentujący kierunki świata
    - (InOutPairDirection) enum reprezentujący skręt z jednego kierunku do drugiego
    - (Lane) reprezentuje pas drogi
    - (Pedestrian) reprezentuje pieszego
    - (Road) reprezentuje całą drogę z jednego kierunku. W jej skład wchodzą pasy.
    - (Vehicle) reprezentuje pojazd dodany do skrzyżowania

- Ponadto w głównym folderze znajdują się:
    - (Main) główna klasa programu
    - (Config) reprezentująca układ pasów na danych skrzyżowaniach
    - (ConfigChecker) sprawdzający, czy instancja klasy Config jest poprawna
    - (IncorrectConfigException) wyjątek jaki wyrzuca (ConfigChecker) w razie nieprawidłowości

### W [traffic_lights_system/src/test/java/org/example](https://github.com/FloudMe77/traffic_lights_system/tree/main/src/test/java/org/example) znajdują się testy wybranych klas

## Funkcjonalność
Program oferuje implementację pieszych strzałek warunkowych, umożliwiających skręt w prawo, oraz dowolny układ pasów z różnymi kierunkami, w jakich możliwe jest kontynuowanie jazdy w obrębie jednej strony świata prowadzącej do skrzyżowania.

## Instalacja i uruchomienie
Aby uruchomić projekt lokalnie, wykonaj następujące kroki:
1. Sklonuj repozytorium:
   ```bash
   git clone https://github.com/FloudMe77/traffic_lights_system.git
   ```
2. Przejdź do katalogu projektu:
   ```bash
   cd traffic_lights_system
   ```
3. Zbuduj projekt za pomocą Gradle:
   ```bash
   ./gradlew build
   ```
4. Uruchom aplikację:
   ```bash
   ./gradlew run --args="fileInName fileOutName"
   ```