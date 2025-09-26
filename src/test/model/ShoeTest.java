package model;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Shoe.ShoeCategory;

public class ShoeTest {
    ClothingItem test;
    ClothingItem duplicate;
    ClothingItem different;
    
    @BeforeEach
    void runBefore() {
        test = new Shoe("Air Forces", "White", ShoeCategory.Sneakers, "img/random.png");
    }

    @Test
    void constructorTest() {
        assertEquals(test.getName(), "Air Forces");
        assertEquals(test.getColour(), "White");
        assertEquals(test.getCategory(), "Sneakers");
        assertEquals(test.getFilepath(), "img/random.png");
    }

    @Test
    void gettersTest() {
        assertEquals("Air Forces", test.getName());
        assertEquals("White", test.getColour());
        assertEquals("Sneakers", test.getCategory());
        assertEquals("img/random.png", test.getFilepath());
    }

    @Test
    void alreadyExistsTest() {
        duplicate = new Shoe("Air Forces", "White", ShoeCategory.Sneakers, "img/random.png");
        different = new Shoe("Air Forces", "Black", ShoeCategory.Sneakers, "img/diff.png");
        assertTrue(test.alreadyExists(duplicate));
        assertFalse(test.alreadyExists(different));
    }

    @Test 
    void toJsonTest() {
        JSONObject json = test.toJson();
        assertEquals("Air Forces", json.getString("name"));
        assertEquals("White", json.getString("colour"));
        assertEquals("Sneakers", json.getString("category"));
        assertEquals("img/random.png", json.getString("filepath"));
    }
}
