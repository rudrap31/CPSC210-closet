package persistance;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.JSONObject;

import model.Closet;

// Represents a writer that writes JSON representation of a closet to a file
// Code based of JsonSerializationDemo
public class JsonWriter {
    private static final int TAB = 4;
    private String destination;
    private JSONObject existingData;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
        existingData = new JSONObject();
    }

    // MODIFIES: this
    // EFFECTS: opens writer and reads existing file data or throws and exception if
    // file cannot be read
    public void open() throws IOException {
        File file = new File(destination);
        if (!file.canRead()) {
            throw new IOException("Cannot read file: " + destination);
        }
        if (file.length() > 0) {
            StringBuilder contentBuilder = new StringBuilder();
            try (Stream<String> stream = Files.lines(Paths.get(destination), StandardCharsets.UTF_8)) {
                stream.forEach(s -> contentBuilder.append(s));
            }
            existingData = new JSONObject(contentBuilder.toString());
        } else {
            existingData = new JSONObject();
        }
    }

    // MODIFIES: this
    // EFFECTS: adds new closet data to existing closet data withou overwriting
    // other users
    public void write(Closet closet) {
        JSONObject newClosetData = closet.toJson();
        existingData.put(closet.getName(), newClosetData.getJSONObject(closet.getName()));
    }

    // MODIFIES: this
    // EFFECTS: saves all data to file and closes the writer
    public void close() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(destination))) {
            writer.print(existingData.toString(TAB));
        }
    }

}
