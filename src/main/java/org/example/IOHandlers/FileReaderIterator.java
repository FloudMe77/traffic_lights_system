package org.example.IOHandlers;

import com.google.gson.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class FileReaderIterator implements Iterable<Command> {
    private final Iterator<JsonElement> iterator;
    private final Gson gson;

    public FileReaderIterator(String filePath) {
        this.gson = new Gson();
        List<JsonElement> commands = loadCommands(filePath);
        this.iterator = commands.iterator();
    }

    private List<JsonElement> loadCommands(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray commandsArray = jsonObject.getAsJsonArray("commands");
            return commandsArray.asList(); // Konwersja na listę dla iteratora
        } catch (IOException e) {
            throw new RuntimeException("Błąd wczytywania pliku: " + filePath, e);
        }
    }

    @Override
    public Iterator<Command> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Command next() {
                JsonObject commandObject = iterator.next().getAsJsonObject();
                String type = commandObject.get("type").getAsString();

                if ("addVehicle".equals(type)) {
                    return gson.fromJson(commandObject, Command.class);
                } else if ("step".equals(type)) {
                    return new Command("step");
                } else {
                    throw new IllegalArgumentException("Nieznana komenda: " + type);
                }
            }
        };
    }
}
