package persistance;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import model.Bottom;
import model.Bottom.BottomCategory;
import model.Closet;
import model.ClothingItem;
import model.Outfit;
import model.Shoe;
import model.Shoe.ShoeCategory;
import model.Top;
import model.Top.TopCategory;
import ui.ClosetApp;

public class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/nonExistentFile.json", "test");
        try {
            Closet closet = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderNonExistentName() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyCloset.json", "test");
        try {
            Closet closet = reader.read();
            assertNull(closet);
        } catch (IOException e) {
            fail("Should not be an error");
        }
    }

    @Test
    void testReaderEmptyCloset() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyCloset.json", "Rudra");
        try {
            Closet closet = reader.read();
            assertEquals("Rudra", closet.getName());
            assertEquals(0, closet.getClothes().size());
            assertEquals(0, closet.getOutfits().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderFullCloset() {
        JsonReader reader = new JsonReader("./data/testReaderFullCloset.json", "Rudra");
        Closet closet = new Closet("Rudra");
        Top top = new Top("Balenciaga", "Purple", TopCategory.Hoodie, "data/top.png");
        Top top2 = new Top("Arcteryx", "Grey", TopCategory.Jacket, "data/top1.png");
        Bottom bottom = new Bottom("HM", "Black", BottomCategory.Sweatpants, "data/bottom.png");
        Shoe shoe = new Shoe("Forces", "Light Blue", ShoeCategory.Sneakers, "data/shoe.png");
        Outfit outfit = new Outfit(top, bottom, shoe);
        Outfit outfit2 = new Outfit(top2, bottom, shoe);
        closet.addClothing(top, "Top");
        closet.addClothing(top2, "Top");
        closet.addClothing(bottom, "Bottom");
        closet.addClothing(shoe, "Shoe");
        closet.addOutfit(outfit);
        closet.addOutfit(outfit2);
        try {
            Closet newCloset = reader.read();
            assertEquals("Rudra", newCloset.getName());

            List<ClothingItem> expectedClothes = closet.getClothes();
            List<ClothingItem> actualClothes = newCloset.getClothes();
            assertEquals(expectedClothes.size(), actualClothes.size());
            for (int i = 0; i < expectedClothes.size(); i++) {
                ClothingItem expectedItem = expectedClothes.get(i);
                ClothingItem actualItem = actualClothes.get(i);

                assertEquals(expectedItem.getName(), actualItem.getName());
                assertEquals(expectedItem.getColour(), actualItem.getColour());
                assertEquals(expectedItem.getCategory(), actualItem.getCategory());                
                assertEquals(expectedItem.getFilepath(), actualItem.getFilepath());
            }

            List<Outfit> expectedOutfits = closet.getOutfits();
            List<Outfit> actualOutfits = newCloset.getOutfits();
            assertEquals(expectedOutfits.size(), actualOutfits.size());

            for (int i = 0; i < expectedOutfits.size(); i++) {
                Outfit expectedOutfit = expectedOutfits.get(i);
                Outfit actualOutfit = actualOutfits.get(i);

                assertEquals(expectedOutfit.getTop().getName(), actualOutfit.getTop().getName());
                assertEquals(expectedOutfit.getBottom().getName(), actualOutfit.getBottom().getName());
                assertEquals(expectedOutfit.getShoe().getName(), actualOutfit.getShoe().getName());
            }
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderParse() {
        JsonReader reader = new JsonReader("./data/random.json", "Rudra");
        JSONObject json = new JSONObject();
        json.put("name", "T-Shirt");
        json.put("colour", "Blue");
        json.put("filepath", "path/to/image");
        json.put("category", "Casual");

        ClothingItem item = reader.parseClothingItem(json, "InvalidCategory");
        assertNull(item);
    }
}
