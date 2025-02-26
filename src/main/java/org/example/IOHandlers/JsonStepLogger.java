package org.example.IOHandlers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.example.SimulationTools.SimulationUtils.Vehicle;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonStepLogger {
    private final String file_path;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    public JsonStepLogger(String file_path) {
        this.file_path = file_path;
    }
    // Odczytuje istniejący JSON lub zwraca pustą strukturę, jeśli plik nie istnieje lub jest pusty
    List<Map<String, List<String>>> readStepStatuses() {
        try {
            // Jeśli plik nie istnieje, zwróć pustą listę
            if (!Files.exists(Paths.get(file_path))) {
                return new ArrayList<>();
            }

            // Wczytaj całą zawartość pliku jako tekst
            String json = Files.readString(Paths.get(file_path));

            // Jeśli plik jest pusty, zwróć pustą listę
            if (json.isBlank()) {
                return new ArrayList<>();
            }

            // Wczytaj listę kroków
            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            return gson.fromJson(jsonObject.get("stepStatuses"), new TypeToken<List<Map<String, List<String>>>>() {}.getType());

        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            return new ArrayList<>(); // W razie błędu zwracamy pustą listę
        }
    }


    // Dodaje nowy krok do JSON
    public void appendStep(List<Vehicle> leftVehicles) {
        List<Map<String, List<String>>> stepStatuses = readStepStatuses();

        // Tworzymy nowy krok w formacie JSON
        Map<String, List<String>> step = new HashMap<>();
        step.put("leftVehicles", leftVehicles.stream().map(Vehicle::getId).toList());
        stepStatuses.add(step);

        // Tworzymy finalny obiekt JSON
        Map<String, Object> output = new HashMap<>();
        output.put("stepStatuses", stepStatuses);

        // Zapisujemy do pliku
        try (FileWriter writer = new FileWriter(file_path)) {
            gson.toJson(output, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
