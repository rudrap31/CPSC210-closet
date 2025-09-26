package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Top.TopCategory;

public class TopTest {
    ClothingItem test;
    ClothingItem duplicate;
    ClothingItem different;
    
    @BeforeEach
    void runBefore() {
        test = new Top("Old Navy Hoodie", "Blue", TopCategory.Hoodie, "img/random.png");
    }

    @Test
    void constructorTest() {
        assertEquals(test.getName(), "Old Navy Hoodie");
        assertEquals(test.getColour(), "Blue");
        assertEquals(test.getCategory(), "Hoodie");
        assertEquals(test.getFilepath(), "img/random.png");
    }

    @Test
    void gettersTest() {
        assertEquals("Old Navy Hoodie", test.getName());
        assertEquals("Blue", test.getColour());
        assertEquals("Hoodie", test.getCategory());
        assertEquals("img/random.png", test.getFilepath());
    }

    @Test
    void alreadyExistsTest() {
        duplicate = new Top("Old Navy Hoodie", "Blue", TopCategory.Hoodie, "img/random.png");
        different = new Top("Gap Hoodie", "Blue", TopCategory.Hoodie, "img/diff.png");
        assertTrue(test.alreadyExists(duplicate));
        assertFalse(test.alreadyExists(different));
    }

    @Test 
    void toJsonTest() {
        JSONObject json = test.toJson();
        assertEquals("Old Navy Hoodie", json.getString("name"));
        assertEquals("Blue", json.getString("colour"));
        assertEquals("Hoodie", json.getString("category"));
        assertEquals("img/random.png", json.getString("filepath"));
    }
}
