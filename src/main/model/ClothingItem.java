package model;

import org.json.JSONObject;

import persistance.Writable;

// Represents a clothing item
public abstract class ClothingItem implements Writable {
    private String name;
    private String colour;
    private String category;
    private String filepath;

    // EFFECTS: creates a clothing item with the given name, colour and category
    public ClothingItem(String name, String colour, String category, String filepath) {
        this.name = name;
        this.colour = colour;
        this.category = category;
        this.filepath = filepath;
    }

    // EFFECTS: returns true if s Clothing Item already exists with the same values,
    // false otherwise
    public boolean alreadyExists(ClothingItem clothe) {
        if (clothe.getName().equals(name)
                && clothe.getColour().equals(colour)
                && clothe.getCategory().equals(category)) {
            return true;
        } else {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public String getColour() {
        return colour;
    }

    public String getCategory() {
        return category;
    }

    public String getFilepath() {
        return filepath;
    }

    // EFFECTS: return clothing item data in json
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("colour", colour);
        json.put("category", category);
        json.put("filepath", filepath);
        return json;
    }

}
