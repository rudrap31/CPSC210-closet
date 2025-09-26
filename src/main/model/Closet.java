package model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import persistance.Writable;

// Represents a closet with clothes and outfits
public class Closet implements Writable {
    private Map<String, List<ClothingItem>> clothes; // Maps each category (Top, Bottom, Shoe) to its list of clothes
    private List<Outfit> outfits;
    private String name;
    Random rand;

    // EFFECTS: creates a closet with no outfits or clothes
    public Closet(String name) {
        clothes = new HashMap<>();
        clothes.put("Top", new ArrayList<>());
        clothes.put("Bottom", new ArrayList<>());
        clothes.put("Shoe", new ArrayList<>());
        outfits = new ArrayList<>();
        this.name = name;
        rand = new Random();
    }

    // MODIFIES: this
    // EFFECTS: adds given outfit to closet if it doesn't already exist
    public void addOutfit(Outfit newOutfit) {
        for (Outfit outfit: outfits) {
            if (outfit.alreadyExists(newOutfit)) {
                return;
            }
        }
        outfits.add(newOutfit);
        EventLog.getInstance().logEvent(new Event("Added new outfit to closet"));
    }

    // REQUIRES: type is one of "Top", "Bottom", or "Shoe"
    // MODIFIES: this
    // EFFECTS: adds given clothing item to closet
    public void addClothing(ClothingItem clothing, String type) {
        clothes.get(type).add(clothing);
        EventLog.getInstance().logEvent(new Event("Added new " + type + ": " + clothing.getName() + " to closet"));
    }

    // REQUIRES: type is one of "Top", "Bottom", or "Shoe" and clothing is of that type
    // MODIFIES: this
    // EFFECTS: removes given clothing item in closet
    public void removeClothing(ClothingItem clothing, String type) {
        clothes.get(type).remove(clothing);
        EventLog.getInstance().logEvent(new Event("Removed " + type + ": " + clothing.getName() + " from closet"));
    }

    // REQUIRES: type is one of "Top", "Bottom", or "Shoe"
    // EFFECTS: return a list of all clothing items in the closet with given type
    //          returns null if none
    public List<ClothingItem> getClothingType(String type) {
        return clothes.get(type);
    }

    // REQUIRES: type is one of "Top", "Bottom", or "Shoe"
    // EFFECTS: return a list of all clothing items in the closet with same type
    //          returns null if none
    public List<ClothingItem> filterClothingType(String type) {
        EventLog.getInstance().logEvent(new Event("Filtered all of the " + type + " clothes"));
        return clothes.get(type);
    }

    // EFFECTS: returns a new randomly created outfit, return null if not enough clothes
    public Outfit generateRandomOutfit() {
        List<ClothingItem> tops = clothes.get("Top"); 
        List<ClothingItem> bottoms = clothes.get("Bottom"); 
        List<ClothingItem> shoes = clothes.get("Shoe"); 

        if (tops.isEmpty() || bottoms.isEmpty() || shoes.isEmpty()) {
            return null;
        }

        int topIndex = rand.nextInt(tops.size());
        Top newTop = (Top) tops.get(topIndex); 
        
        int bottomIndex = rand.nextInt(bottoms.size());
        Bottom newBottom = (Bottom) bottoms.get(bottomIndex); 
        
        int shoeIndex = rand.nextInt(shoes.size());
        Shoe newShoe = (Shoe) shoes.get(shoeIndex);

        EventLog.getInstance().logEvent(new Event("Created new random outfit"));
        return new Outfit(newTop, newBottom, newShoe);
    }

    // EFFECTS: returns a list of all clothes in the closet
    public List<ClothingItem> getClothes() {
        List<ClothingItem> allClothes = new ArrayList<>();
        for (List<ClothingItem> categorizedClothes: clothes.values()) {
            allClothes.addAll(categorizedClothes);
        }
        return allClothes;
    }

    public List<Outfit> getOutfits() {
        return outfits;
    }

    public String getName() {
        return name;
    }

    // EFFECTS: return closet data in json
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONObject closetData = new JSONObject();
        JSONArray outfitData = new JSONArray();
        JSONObject clothingData = new JSONObject();

        addClothingCategoryToJson(clothingData, "Top");
        addClothingCategoryToJson(clothingData, "Bottom");
        addClothingCategoryToJson(clothingData, "Shoe");
        
        for (Outfit outfit : outfits) {
            outfitData.put(outfit.toJson());
        }

        closetData.put("clothes", clothingData);
        closetData.put("outfits", outfitData);

        json.put(name, closetData);
        return json;
    }

    // REQUIRES: type is one of "Top", "Bottom", or "Shoe"
    // EFFECTS: adds all the clothes in given category to clothingData
    private void addClothingCategoryToJson(JSONObject clothingData, String category) {
        JSONArray clothingArray = new JSONArray();
        for (ClothingItem item : getClothingType(category)) {
            clothingArray.put(item.toJson());
        }
        clothingData.put(category, clothingArray);
    }
}
