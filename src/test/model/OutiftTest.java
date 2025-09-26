package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Bottom.BottomCategory;
import model.Top.TopCategory;
import model.Shoe.ShoeCategory;

public class OutiftTest {
    Outfit test;
    Outfit duplicate;
    Outfit different;
    Top top;
    Top differentTop;
    Bottom bottom;
    Bottom differentBottom;
    Shoe shoe;
    Shoe differentShoe;
    
    @BeforeEach
    void runBefore() {
        top = new Top("Old Navy Hoodie", "Blue", TopCategory.Hoodie, "data/top.png");
        differentTop = new Top("Old Navy Short", "Black", TopCategory.Hoodie, "data/top1.png");
        bottom = new Bottom("Nike Sweats", "Gray", BottomCategory.Sweatpants, "data/bottom.png");
        differentBottom = new Bottom("Costco Sweats", "Black", BottomCategory.Sweatpants, "data/bottom1.png");
        shoe = new Shoe("Air Forces", "White", ShoeCategory.Sneakers, "data/shoe.png");
        differentShoe = new Shoe("Air Jordans", "Red", ShoeCategory.Sneakers, "data/shoe1.png");
        test = new Outfit(top, bottom, shoe);
    }

    @Test
    void constructorTest() {
        assertEquals(test.getTop(), top);
        assertEquals(test.getBottom(), bottom);
        assertEquals(test.getShoe(), shoe);
    }

    @Test
    void alreadyExistsTest() {
        duplicate = new Outfit(top, bottom, shoe);
        different = new Outfit(differentTop, bottom, shoe);
        assertTrue(test.alreadyExists(duplicate));
        assertFalse(test.alreadyExists(different));

        duplicate = new Outfit(top, bottom, shoe);
        different = new Outfit(top, differentBottom, shoe);
        assertTrue(test.alreadyExists(duplicate));
        assertFalse(test.alreadyExists(different));

        duplicate = new Outfit(top, bottom, shoe);
        different = new Outfit(top, bottom, differentShoe);
        assertTrue(test.alreadyExists(duplicate));
        assertFalse(test.alreadyExists(different));
    }

    @Test
    void toJsonTest() {
        JSONObject json = test.toJson();

        JSONObject top = json.getJSONObject("top");
        JSONObject bottom = json.getJSONObject("bottom");
        JSONObject shoe = json.getJSONObject("shoe");

        assertEquals("Old Navy Hoodie", top.getString("name"));
        assertEquals("Nike Sweats", bottom.getString("name"));
        assertEquals("Air Forces", shoe.getString("name"));
    }
}
