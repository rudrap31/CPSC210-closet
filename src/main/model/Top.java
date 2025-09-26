package model;

// Represents a Top Clothing item
public class Top extends ClothingItem {
    public enum TopCategory { 
        Hoodie, Jacket, Shirt 
    }

    // EFFECTS: creates a Top clothing item with given name, colour and category
    public Top(String name, String colour, TopCategory type, String filepath) {
        super(name, colour, type.name(), filepath);
    }

}
