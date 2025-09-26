package model;

// Represents a Bottom Clothing item
public class Bottom extends ClothingItem {
    public enum BottomCategory { 
        Sweatpants, Jeans, Shorts 
    }

    // EFFECTS: creates a Bottom clothing item with given name, colour and category
    public Bottom(String name, String colour, BottomCategory type, String filepath) {
        super(name, colour, type.name(), filepath);   
    }
}
