package persistance;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Bottom;
import model.Closet;
import model.ClothingItem;
import model.Outfit;
import model.Shoe;
import model.Top;
import model.Bottom.BottomCategory;
import model.Shoe.ShoeCategory;
import model.Top.TopCategory;

// Represents a reader that reads closet from JSON data stored in file
// Code based of JsonSerializationDemo
public class JsonReader {
    private String source;
    private String name;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source, String name) {
        this.source = source;
        this.name = name;
    }

    // EFFECTS: reads closet data from file and returns it as a closet object;
    // throws IOException if an error occurs reading data from file
    public Closet read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseCloset(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses closet from JSON object and returns it
    // returns null if given name's data was not found
    private Closet parseCloset(JSONObject jsonObject) {
        if (!jsonObject.has(name)) {
            return null;
        }

        JSONObject closetData = jsonObject.getJSONObject(name);
        Closet closet = new Closet(name);

        JSONObject clothingData = closetData.getJSONObject("clothes");
        parseClothingCategory(clothingData, "Top", closet);
        parseClothingCategory(clothingData, "Bottom", closet);
        parseClothingCategory(clothingData, "Shoe", closet);

        // Parse outfits
        JSONArray outfitsArray = closetData.getJSONArray("outfits");
        for (Object json : outfitsArray) {
            Outfit outfit = parseOutfit((JSONObject) json);
            closet.addOutfit(outfit);
        }
        return closet;
    }

    // REQUIRES: category to be on of Top, Bottom, Shoe
    // EFFECTS: Parses clothing items of a given category and adds them to the
    // closet
    private void parseClothingCategory(JSONObject clothingData, String category, Closet closet) {
        JSONArray clothingArray = clothingData.getJSONArray(category);
        for (Object json : clothingArray) {
            ClothingItem clothingItem = parseClothingItem((JSONObject) json, category);
            closet.addClothing(clothingItem, category);
        }
    }

    // REQUIRES: category to be on of Top, Bottom, Shoe
    // EFFECTS: Parses a clothing item from JSON object and returns it
    public ClothingItem parseClothingItem(JSONObject jsonObject, String category) {
        String name = jsonObject.getString("name");
        String colour = jsonObject.getString("colour");
        String filepath = jsonObject.getString("filepath");


        switch (category) {
            case "Top":
                TopCategory subCategoryTop = TopCategory.valueOf(jsonObject.getString("category"));
                return new Top(name, colour, subCategoryTop, filepath);
            case "Bottom":
                BottomCategory subCategoryBottom = BottomCategory.valueOf(jsonObject.getString("category"));
                return new Bottom(name, colour, subCategoryBottom, filepath);
            case "Shoe":
                ShoeCategory subCategoryShoe = ShoeCategory.valueOf(jsonObject.getString("category"));
                return new Shoe(name, colour, subCategoryShoe, filepath);
        }
        return null; // will never happen

    }

    // EFFECTS: Parses an outfit from JSON object and returns it
    private Outfit parseOutfit(JSONObject jsonObject) {
        Top top = (Top) parseClothingItem(jsonObject.getJSONObject("top"), "Top");
        Bottom bottom = (Bottom) parseClothingItem(jsonObject.getJSONObject("bottom"), "Bottom");
        Shoe shoe = (Shoe) parseClothingItem(jsonObject.getJSONObject("shoe"), "Shoe");

        return new Outfit(top, bottom, shoe);
    }
}
