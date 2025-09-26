package model;

// Represents a Shoe Clothing Item
public class Shoe extends ClothingItem {
    public enum ShoeCategory { 
        Sneakers, Slides, Boots 
    }

    // EFFECTS: creates a Shoe clothing item with given name, colour and category
    public Shoe(String name, String colour, ShoeCategory type, String filepath) {
        super(name, colour, type.name(), filepath);
    }
}
