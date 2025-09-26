package model;

import org.json.JSONObject;
import persistance.Writable;

// Represents an outfit with top, bottom, and shoes
public class Outfit implements Writable {
    private Top top;
    private Bottom bottom;
    private Shoe shoe;

    // EFFECTS: creates an outfit with given, top, bottom, shoe
    public Outfit(Top top, Bottom bottom, Shoe shoe) {
        this.top = top;
        this.bottom = bottom;
        this.shoe = shoe;
    }

    // EFFECTS: returns true if outfit already exists, false otherwise
    public boolean alreadyExists(Outfit outfit) {
        if (outfit.getTop().alreadyExists(top) && outfit.getBottom().alreadyExists(bottom) 
                && outfit.getShoe().alreadyExists(shoe)) {
            return true;
        } else {
            return false;
        }
    }

    public Top getTop() {
        return top;
    }
    
    public Bottom getBottom() {
        return bottom;
    }

    public Shoe getShoe() {
        return shoe;
    }

    // EFFECTS: return outfit data in json
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("top", top.toJson());
        json.put("bottom", bottom.toJson());
        json.put("shoe", shoe.toJson());
        return json;
    }
}
