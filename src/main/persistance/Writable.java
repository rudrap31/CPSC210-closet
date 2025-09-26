package persistance;

import org.json.JSONObject;

public interface Writable {
    // EFFECTS: returns this as a json object
    JSONObject toJson();
    
}