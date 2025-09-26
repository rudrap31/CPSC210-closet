package model;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Bottom.BottomCategory;

public class BottomTest {
    ClothingItem test;
    ClothingItem duplicate;
    ClothingItem different;
    
    @BeforeEach
    void runBefore() {
        test = new Bottom("Nike Sweats", "Gray", BottomCategory.Sweatpants, "img/random.png");
    }

    @Test
    void constructorTest() {
        assertEquals(test.getName(), "Nike Sweats");
        assertEquals(test.getColour(), "Gray");
        assertEquals(test.getCategory(), "Sweatpants");
        assertEquals(test.getFilepath(), "img/random.png");
    }

    @Test
    void gettersTest() {
        assertEquals("Nike Sweats", test.getName());
        assertEquals("Gray", test.getColour());
        assertEquals("Sweatpants", test.getCategory());
        assertEquals("img/random.png", test.getFilepath());
    }

    @Test
    void alreadyExistsTest() {
        duplicate = new Bottom("Nike Sweats", "Gray", BottomCategory.Sweatpants, "img/random.png");
        different = new Bottom("Nike Sweats", "Gray", BottomCategory.Jeans, "img/different.png");
        assertTrue(test.alreadyExists(duplicate));
        assertFalse(test.alreadyExists(different));
    }

    @Test 
    void toJsonTest() {
        JSONObject json = test.toJson();
        assertEquals("Nike Sweats", json.getString("name"));
        assertEquals("Gray", json.getString("colour"));
        assertEquals("Sweatpants", json.getString("category"));
        assertEquals("img/random.png", json.getString("filepath"));
    }
}
