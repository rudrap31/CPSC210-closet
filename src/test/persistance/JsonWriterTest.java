package persistance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.Bottom.BottomCategory;
import model.Shoe.ShoeCategory;
import model.*;
import model.Top.TopCategory;

class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Closet closet = new Closet("Rudra");
            JsonWriter writer = new JsonWriter("./data/randomIllegal/ s::/\\:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testEmptyFile() {
        try {
            PrintWriter eraser = new PrintWriter(new File("./data/testWriterEmptyFile.json"));
            Closet closet = new Closet("Rudra");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyFile.json");
            writer.open();
            writer.write(closet);
            writer.close();
            
            JsonReader reader = new JsonReader("./data/testWriterEmptyFile.json", "Rudra");
            closet = reader.read();
            assertEquals("Rudra", closet.getName());
            assertEquals(0, closet.getClothes().size());
            assertEquals(0, closet.getOutfits().size());
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyCloset() {
        try {
            Closet closet = new Closet("Rudra");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyCloset.json");
            writer.open();
            writer.write(closet);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyCloset.json", "Rudra");
            closet = reader.read();
            assertEquals("Rudra", closet.getName());
            assertEquals(0, closet.getClothes().size());
            assertEquals(0, closet.getOutfits().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterFullCloset() {
        try {
            Closet closet = new Closet("Rudra");
            Top top = new Top("Balenciaga", "Red", TopCategory.Shirt, "data/top.png");
            Bottom bottom = new Bottom("HM", "Blue", BottomCategory.Jeans, "data/bottom.png");
            Shoe shoe = new Shoe("Forces", "White", ShoeCategory.Sneakers, "data/shoe.png");
            Outfit outfit = new Outfit(top, bottom, shoe);
            closet.addClothing(top, "Top");
            closet.addClothing(bottom, "Bottom");
            closet.addClothing(shoe, "Shoe");
            closet.addOutfit(outfit);
            JsonWriter writer = new JsonWriter("./data/testWriterFullCloset.json");
            writer.open();
            writer.write(closet);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterFullCloset.json", "Rudra");
            Closet newCloset = reader.read();
            assertEquals("Rudra", closet.getName());
            
            List<ClothingItem> expectedClothes = closet.getClothes();
            List<ClothingItem> actualClothes = newCloset.getClothes();
            assertEquals(expectedClothes.size(), actualClothes.size());
            for (int i = 0; i < expectedClothes.size(); i++) {
                assertEquals(expectedClothes.get(i).getName(), actualClothes.get(i).getName());
                assertEquals(expectedClothes.get(i).getColour(), actualClothes.get(i).getColour());
                assertEquals(expectedClothes.get(i).getCategory(), actualClothes.get(i).getCategory());
                assertEquals(expectedClothes.get(i).getFilepath(), actualClothes.get(i).getFilepath());
            }

            List<Outfit> expectedOutfits = closet.getOutfits();
            List<Outfit> actualOutfits = newCloset.getOutfits();
            assertEquals(expectedOutfits.size(), actualOutfits.size());
            for (int i = 0; i < expectedOutfits.size(); i++) {
                Outfit expected = expectedOutfits.get(i);
                Outfit actual = actualOutfits.get(i);
                assertEquals(expected.getTop().getName(), actual.getTop().getName());
                assertEquals(expected.getBottom().getName(), actual.getBottom().getName());
                assertEquals(expected.getShoe().getName(), actual.getShoe().getName());
            }

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}